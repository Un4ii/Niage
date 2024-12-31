package net.niage.engine.utils;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import net.niage.engine.graphics.Material;
import net.niage.engine.graphics.Mesh;
import net.niage.engine.graphics.Model;
import net.niage.engine.texture.Texture2D;

public class ModelUtils {

    public static Model loadModel(String modelPath) throws IOException {
        final AIScene scene = Assimp.aiImportFile(FileUtils.getFullPath(modelPath),
                Assimp.aiProcess_CalcTangentSpace
                        | Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices
                        | Assimp.aiProcess_SortByPType);

        if (scene == null) {
            throw new IOException("ERROR::MODEL::LOADING::IMPORT_FAILED\n" + Assimp.aiGetErrorString());
        }

        List<Mesh> meshes = new ArrayList<>();

        for (int i = 0; i < scene.mNumMeshes(); i++) {

            AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));

            try {
                meshes.add(new Mesh(processVertices(mesh), processIndices(mesh),
                        processMaterial(scene, mesh, modelPath)));
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new IOException("ERROR::MESH::PROCESSING_FAILED at mesh " + i + ":\n" + e);
            }
        }

        return new Model(meshes);
    }

    private static float[] processVertices(AIMesh mesh) {
        float[] vertices = new float[mesh.mNumVertices() * 8];

        AIVector3D.Buffer vertexPos = mesh.mVertices();
        AIVector3D.Buffer normals = mesh.mNormals();
        AIVector3D.Buffer textCoords = mesh.mTextureCoords(0);

        int index = 0;

        for (int i = 0; i < mesh.mNumVertices(); i++) {
            // Vertex position
            vertices[index++] = vertexPos.get(i).x();
            vertices[index++] = vertexPos.get(i).y();
            vertices[index++] = vertexPos.get(i).z();

            // Normals
            vertices[index++] = normals.get(i).x();
            vertices[index++] = normals.get(i).y();
            vertices[index++] = normals.get(i).z();

            // Texture coords
            if (textCoords != null) {
                vertices[index++] = textCoords.get(i).x();
                vertices[index++] = textCoords.get(i).y();
            } else {
                vertices[index++] = 0.0f;
                vertices[index++] = 0.0f;
            }
        }

        return vertices;
    }

    private static int[] processIndices(AIMesh mesh) {
        int[] indices = new int[mesh.mNumFaces() * 3];

        AIFace.Buffer faces = mesh.mFaces();

        for (int i = 0; i < mesh.mNumFaces(); i++) {
            AIFace face = faces.get(i);
            for (int j = 0; j < face.mNumIndices(); j++) {
                indices[i * 3 + j] = face.mIndices().get(j);
            }
        }

        return indices;
    }

    private static Material processMaterial(AIScene scene, AIMesh mesh, String modelPath) throws IOException {
        AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));

        // Colors
        // Diffuse color
        AIColor4D diffuseColor = AIColor4D.create().set(1, 1, 1, 1);
        Assimp.aiGetMaterialColor(material, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0,
                diffuseColor);

        // Specular color
        AIColor4D specularColor = AIColor4D.create().set(1, 1, 1, 1);
        Assimp.aiGetMaterialColor(material, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0,
                specularColor);

        // Shininess
        float[] shininessStrenght = new float[1];
        Assimp.aiGetMaterialFloatArray(material, Assimp.AI_MATKEY_SHININESS, Assimp.aiTextureType_NONE, 0,
                shininessStrenght,
                new int[shininessStrenght.length]);

        // Convertions
        Vector3f diffuse = new Vector3f(diffuseColor.r(), diffuseColor.g(), diffuseColor.b());
        Vector3f specular = new Vector3f(specularColor.r(), specularColor.g(), specularColor.b());
        float shininess = shininessStrenght[0];

        // Textures
        // Diffuse texture
        Texture2D diffuseTexture = new Texture2D();
        boolean useDiffuseTexture = false;
        if (Assimp.aiGetMaterialTextureCount(material, Assimp.aiTextureType_DIFFUSE) > 0) {
            AIString texturePath = AIString.create();
            Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DIFFUSE, 0, texturePath, (IntBuffer) null,
                    null,
                    null,
                    null,
                    null, null);
            diffuseTexture = new Texture2D(FileUtils.getFolderPath(modelPath) + "/" + texturePath.dataString());
            useDiffuseTexture = true;
        }

        // Specular texture
        Texture2D specularTexture = new Texture2D();
        boolean useSpecularTexture = false;
        if (Assimp.aiGetMaterialTextureCount(material, Assimp.aiTextureType_SPECULAR) > 0) {
            AIString texturePath = AIString.create();
            Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_SPECULAR, 0, texturePath, (IntBuffer) null,
                    null,
                    null,
                    null,
                    null, null);
            specularTexture = new Texture2D(FileUtils.getFolderPath(modelPath) + "/" + texturePath.dataString());
            useSpecularTexture = true;
        }

        // Create material
        return new Material(diffuse, specular, shininess, diffuseTexture, specularTexture, useDiffuseTexture,
                useSpecularTexture);
    }
}
