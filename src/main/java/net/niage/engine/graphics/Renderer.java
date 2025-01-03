package net.niage.engine.graphics;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.niage.engine.graphics.model.Mesh;
import net.niage.engine.graphics.model.Model;

public class Renderer {

    private Shader shader;

    public Renderer() {
    }

    public void start(Shader shader) {
        this.shader = shader;
        this.shader.bind();
    }

    public void render(Model model) {
        model.meshes().forEach(mesh -> {
            mesh.material().diffuseTexture().activate();
            mesh.material().specularTexture().activate();
            GL30.glBindVertexArray(mesh.VAO());

            shader.setMat4("model.transform", new Matrix4f(model.transform()).mul(mesh.transform()));
            setMeshMaterial(mesh);
            GL20.glDrawElements(GL20.GL_TRIANGLES, mesh.indicesLenght(), GL20.GL_UNSIGNED_INT, 0);

            mesh.material().diffuseTexture().deactivate();
            mesh.material().specularTexture().deactivate();
        });
        GL30.glBindVertexArray(0);

    }

    private void setMeshMaterial(Mesh mesh) {
        shader.setVec3("model.material.diffuseColor", mesh.material().diffuseColor());
        shader.setVec3("model.material.specularColor", mesh.material().specularColor());
        shader.setInt("model.material.diffuseTexture", mesh.material().diffuseTexture().ID());
        shader.setInt("model.material.specularTexture", mesh.material().specularTexture().ID());
        shader.setFloat("model.material.shininess", mesh.material().shininess());
        shader.setBool("model.material.useDiffuseTexture", mesh.material().useDiffuseTexture());
        shader.setBool("model.material.useSpecularTexture", mesh.material().useSpecularTexture());
    }

    public void end() {
        Shader.unbind();
    }

    public static int COLOR_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT;
    public static int DEPTH_BUFFER_BIT = GL11.GL_DEPTH_BUFFER_BIT;
    public static int STENCIL_BUFFER_BIT = GL11.GL_STENCIL_BUFFER_BIT;
    public static int ACCUM_BUFFER_BIT = GL11.GL_ACCUM_BUFFER_BIT;

    public static void glClear(int mask) {
        GL11.glClear(mask);
    }

    public static void glClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }
}
