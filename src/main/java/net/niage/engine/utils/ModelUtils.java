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

    public static Model loadModel(String modelPath) throws Exception {
        try {

            final AIScene scene = Assimp.aiImportFile(FileUtils.getFullPath(modelPath),
                    Assimp.aiProcess_CalcTangentSpace
                            | Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices
                            | Assimp.aiProcess_SortByPType);

            if (scene == null) {
                String errorMsg = "ERROR::MODEL::LOADING::IMPORT_FAILED\n" + Assimp.aiGetErrorString();
                System.err.println(errorMsg);
                throw new IOException(errorMsg);
            }

            List<Mesh> meshes = new ArrayList<>();

            for (int i = 0; i < scene.mNumMeshes(); i++) {

                AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));

                try {
                    meshes.add(new Mesh(processVertices(mesh), processIndices(mesh),
                            processMaterial(scene, mesh, modelPath)));
                } catch (Exception e) {
                    System.err.println("ERROR::MESH::PROCESSING_FAILED at mesh " + i + ": " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Liberamos el mesh para evitar fugas de memoria
                    mesh.free();
                }
            }

            return new Model(meshes); // Asumí que hay un constructor en `Model` que toma una lista de `Mesh`
        } catch (Exception e) {
            System.err.println("ERROR::MODEL::LOADING_FAILED: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzamos la excepción para que el error no se quede silenciado
        }
    }

    private static float[] processVertices(AIMesh mesh) {
        try {

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

            // Liberamos buffers de Assimp
            vertexPos.free();
            normals.free();
            if (textCoords != null)
                textCoords.free();

            return vertices;
        } catch (Exception e) {
            System.err.println("ERROR::VERTICES::PROCESSING_FAILED: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzamos la excepción
        }
    }

    private static int[] processIndices(AIMesh mesh) {
        try {

            int[] indices = new int[mesh.mNumFaces() * 3];

            AIFace.Buffer faces = mesh.mFaces();

            for (int i = 0; i < mesh.mNumFaces(); i++) {
                AIFace face = faces.get(i);
                for (int j = 0; j < face.mNumIndices(); j++) {
                    indices[i * 3 + j] = face.mIndices().get(j); // Corregir el acceso a los índices
                }
            }

            return indices;
        } catch (Exception e) {
            System.err.println("ERROR::INDICES::PROCESSING_FAILED: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzamos la excepción
        }
    }

    private static Material processMaterial(AIScene scene, AIMesh mesh, String modelPath) {
        try {

            AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));

            // Colors
            // Diffuse color
            AIColor4D diffuseColor = AIColor4D.create();
            Assimp.aiGetMaterialColor(material, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0,
                    diffuseColor);

            // Specular color
            AIColor4D specularColor = AIColor4D.create();
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
            Texture2D diffuseTexture = null;
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
            Texture2D specularTexture = null;
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

            // Liberamos el material
            material.free();

            // Create material
            return new Material(diffuse, specular, shininess, diffuseTexture, specularTexture, useDiffuseTexture,
                    useSpecularTexture);
        } catch (Exception e) {
            System.err.println("ERROR::MATERIAL::PROCESSING_FAILED: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzamos la excepción
        }
    }
}
