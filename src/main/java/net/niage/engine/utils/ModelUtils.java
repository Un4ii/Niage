package net.niage.engine.utils;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.Assimp;

import net.niage.animation.Animation;
import net.niage.animation.Bone;
import net.niage.animation.BoneAnimation;
import net.niage.animation.KeyFrame;
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
        processNode(scene.mRootNode(), scene, meshes, new Matrix4f().identity(), modelPath);
        return new Model(meshes);
    }

    private static void processNode(AINode node, AIScene scene, List<Mesh> meshes, Matrix4f parentTransform,
            String modelPath) throws IOException {
        // Convierte la transformación local del nodo a Matrix4f (JOML)
        Matrix4f nodeTransform = convertMatrix(node.mTransformation());
        Matrix4f globalTransform = new Matrix4f(parentTransform).mul(nodeTransform);

        // Procesa las mallas asociadas al nodo
        for (int i = 0; i < node.mNumMeshes(); i++) {
            int meshIndex = node.mMeshes().get(i);
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(meshIndex));

            try {
                Mesh processedMesh = new Mesh(
                        processVertices(mesh),
                        processIndices(mesh),
                        processMaterial(scene, mesh, modelPath),
                        globalTransform,
                        processAnimations(scene, mesh),
                        processBones(mesh));
                meshes.add(processedMesh);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.err.println("ERROR::MESH::PROCESSING_FAILED at mesh " + i + ":\n" + e);
            }
        }

        // Procesa los nodos hijos recursivamente
        for (int i = 0; i < node.mNumChildren(); i++) {
            processNode(AINode.create(node.mChildren().get(i)), scene, meshes, globalTransform, modelPath);
        }
    }

    private static Matrix4f convertMatrix(AIMatrix4x4 aiMatrix) {
        // Convierte una matriz de Assimp (AIMatrix4x4) a JOML (Matrix4f)
        return new Matrix4f(
                aiMatrix.a1(), aiMatrix.b1(), aiMatrix.c1(), aiMatrix.d1(),
                aiMatrix.a2(), aiMatrix.b2(), aiMatrix.c2(), aiMatrix.d2(),
                aiMatrix.a3(), aiMatrix.b3(), aiMatrix.c3(), aiMatrix.d3(),
                aiMatrix.a4(), aiMatrix.b4(), aiMatrix.c4(), aiMatrix.d4());
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

    private static List<Bone> processBones(AIMesh mesh) throws IOException {
        List<Bone> bones = new ArrayList<>();

        // if (mesh.mNumBones() == 0)
        // throw new IOException("ERROR::MESH::NO_BONES_FOUND");

        for (int i = 0; i < mesh.mNumBones(); i++) {
            // Cada hueso de la malla
            AIBone aiBone = AIBone.create(mesh.mBones().get(i));

            AIMatrix4x4 boneTransform = aiBone.mOffsetMatrix();
            Matrix4f transform = new Matrix4f(
                    boneTransform.a1(), boneTransform.b1(), boneTransform.c1(), boneTransform.d1(),
                    boneTransform.a2(), boneTransform.b2(), boneTransform.c2(), boneTransform.d2(),
                    boneTransform.a3(), boneTransform.b3(), boneTransform.c3(), boneTransform.d3(),
                    boneTransform.a4(), boneTransform.b4(), boneTransform.c4(), boneTransform.d4());

            Vector3f position = new Vector3f();
            Quaternionf rotation = new Quaternionf();
            Vector3f scale = new Vector3f();

            transform.getTranslation(position);
            transform.getNormalizedRotation(rotation);
            transform.getScale(scale);

            bones.add(new Bone(aiBone.mName().dataString(), position, rotation, scale));

        }

        return bones;
    }

    private static List<BoneAnimation> processBoneAnimations(AIAnimation aiAnimation, AIMesh mesh) throws IOException {
        List<BoneAnimation> boneAnimations = new ArrayList<>();
        List<Bone> bones = processBones(mesh);

        for (int i = 0; i < aiAnimation.mNumChannels(); i++) {
            AINodeAnim aiNodeAnim = AINodeAnim.create(aiAnimation.mChannels().get(i));

            List<KeyFrame> keyFrames = new ArrayList<>();
            int totalKeyFrames = Math.max(aiNodeAnim.mNumPositionKeys(), Math.max(
                    aiNodeAnim.mNumScalingKeys(), aiNodeAnim.mNumRotationKeys()));

            for (int j = 0; j < totalKeyFrames; j++) {
                AIVectorKey pos = aiNodeAnim.mPositionKeys().get(i);
                AIQuatKey rot = aiNodeAnim.mRotationKeys().get(i);
                AIVectorKey scl = aiNodeAnim.mScalingKeys().get(i);

                Vector3f position = new Vector3f(pos.mValue().x(), pos.mValue().y(), pos.mValue().z());
                Quaternionf rotation = new Quaternionf(rot.mValue().x(), rot.mValue().y(), rot.mValue().z(),
                        rot.mValue().w());
                Vector3f scale = new Vector3f(scl.mValue().x(), scl.mValue().y(), scl.mValue().z());

                keyFrames.add(new KeyFrame(j, position, rotation, scale));
            }

            String boneName = aiNodeAnim.mNodeName().dataString();
            Bone bone = null;
            bones.forEach(b -> {
                if (b.name().equals(boneName))
                    b = bone;
            });

            boneAnimations.add(new BoneAnimation(bone, keyFrames));
        }

        return boneAnimations;
    }

    private static List<Animation> processAnimations(AIScene scene, AIMesh mesh) throws IOException {
        List<Animation> animations = new ArrayList<>();

        for (int i = 0; i < scene.mNumAnimations(); i++) {
            AIAnimation aiAnimation = AIAnimation.create(scene.mAnimations().get(i));

            List<BoneAnimation> boneAnimations = processBoneAnimations(aiAnimation, mesh);

            AINodeAnim aiNodeAnim = AINodeAnim.create(aiAnimation.mChannels().get(i));
            int totalKeyFrames = Math.max(aiNodeAnim.mNumPositionKeys(), Math.max(
                    aiNodeAnim.mNumScalingKeys(), aiNodeAnim.mNumRotationKeys()));

            animations.add(new Animation(aiAnimation.mName().dataString(), boneAnimations, totalKeyFrames));
        }

        return animations;
    }

}
