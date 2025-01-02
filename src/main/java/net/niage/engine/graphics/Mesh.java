package net.niage.engine.graphics;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.niage.animation.Animation;
import net.niage.animation.Bone;

public class Mesh {

    private int VAO, VBO, EBO;
    private int indicesLenght;

    private Material material;
    private Matrix4f transform;

    private List<Animation> animations;
    private List<Bone> bones;

    public Mesh(float[] vertices, int[] indices, Material material, Matrix4f transform, List<Animation> animations,
            List<Bone> bones) {
        this.material = material;
        this.indicesLenght = indices.length;
        this.transform = transform;
        this.animations = animations;

        init(vertices, indices);
    }

    private void init(float[] vertices, int[] indices) {
        // Create VAO, VBO & EBO
        VAO = GL30.glGenVertexArrays();
        VBO = GL15.glGenBuffers();
        EBO = GL15.glGenBuffers();

        // Use VAO for the following bindings
        GL30.glBindVertexArray(VAO);

        // Bind the VBO to the VAO and put the vertex info into the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

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

    public List<Animation> animations() {
        return this.animations;
    }

    public List<Bone> bones() {
        return bones;
    }
}
