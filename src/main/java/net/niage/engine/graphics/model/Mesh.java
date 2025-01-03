package net.niage.engine.graphics.model;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.niage.engine.graphics.Material;
import net.niage.engine.graphics.animation.Bone;
import net.niage.engine.graphics.animation.BoneInfluence;

public class Mesh {

    private int VAO, VBO, EBO;

    private final List<Vertex> vertices;
    private final int[] indices;
    private final int indicesLenght;

    private Material material;
    private Matrix4f transform;

    private final List<Bone> bones;

    public Mesh(List<Vertex> vertices, int[] indices, Material material, Matrix4f transform, List<Bone> bones) {
        this.vertices = vertices;
        this.indices = indices;
        this.indicesLenght = indices.length;
        this.material = material;
        this.transform = transform;
        this.bones = bones;

        init();
    }

    private void init() {
        // Parse vertices into array
        float[] vertexData = new float[vertices.size() * 8];
        int index = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);

            // Vertex position
            vertexData[index++] = vertex.position().x();
            vertexData[index++] = vertex.position().y();
            vertexData[index++] = vertex.position().z();

            // Normals
            vertexData[index++] = vertex.normal().x();
            vertexData[index++] = vertex.normal().y();
            vertexData[index++] = vertex.normal().z();

            // Texture coords
            vertexData[index++] = vertex.textCoords().x();
            vertexData[index++] = vertex.textCoords().y();
        }

        // Create VAO, VBO & EBO
        VAO = GL30.glGenVertexArrays();
        VBO = GL15.glGenBuffers();
        EBO = GL15.glGenBuffers();

        // Use VAO for the following bindings
        GL30.glBindVertexArray(VAO);

        // Bind the VBO to the VAO and put the vertex info into the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        // Bind the EBO to the VAO and put the indices info into the EBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Set the first 3 floats of the buffer as the vertices
        GL20.glVertexAttribPointer(0, 3, GL15.GL_FLOAT, false, 8 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        // Set the 3 following floats as the normals
        GL20.glVertexAttribPointer(1, 3, GL20.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // Set the last 2 following floats as the texture coords
        GL20.glVertexAttribPointer(2, 2, GL20.GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);

        // Unbind VAO / Stop using it for the calls
        GL30.glBindVertexArray(0);
    }

    public void dispose() {
        GL15.glDeleteBuffers(VBO);
        GL15.glDeleteBuffers(EBO);
        GL30.glDeleteVertexArrays(VAO);

        material.diffuseTexture().dispose();
        material.specularTexture().dispose();
    }

    public Material material() {
        return material;
    }

    public int VAO() {
        return VAO;
    }

    public int indicesLenght() {
        return indicesLenght;
    }

    public Matrix4f transform() {
        return transform;
    }
}
