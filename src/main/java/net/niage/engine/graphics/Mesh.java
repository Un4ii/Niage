package net.niage.engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {

    private int VAO, VBO, EBO;
    private int indicesLenght;

    private Material material;

    public Mesh(float[] vertices, int[] indices, Material material) {
        this.material = material;
        this.indicesLenght = indices.length;

        init(vertices, indices);
    }

    private void init(float[] vertices, int[] indices) {
        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        try {
            FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
            vertexBuffer.put(vertices).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        } catch (Exception e) {
            throw new RuntimeException("ERROR::MESH::VBO::BUFFER\n" + e);
        }

        EBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
        try {
            IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
            indexBuffer.put(indices).flip();
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        } catch (Exception e) {
            throw new RuntimeException("ERROR::MESH::EBO::BUFFER\n" + e);
        }

        // Set the first 3 floats of the buffer as the vertices
        GL20.glVertexAttribPointer(0, 3, GL15.GL_FLOAT, false, 8 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        // Set the 3 following floats as the normals
        GL20.glVertexAttribPointer(1, 3, GL20.GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // Set the last 2 following floats as the texture coords
        GL20.glVertexAttribPointer(2, 2, GL20.GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);

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
}
