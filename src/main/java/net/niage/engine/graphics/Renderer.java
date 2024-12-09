package net.niage.engine.graphics;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    private final Shader shader;

    public Renderer(Shader shader) {
        this.shader = shader;
    }

    public void start() {
        shader.bind();
    }

    public void render(Model model) {
        model.meshes().forEach(mesh -> {
            mesh.material().diffuseTexture().activate();
            GL30.glBindVertexArray(mesh.VAO());

            shader.setMat4("model.transform", model.transform());
            setMeshMaterial(mesh);
            GL20.glDrawElements(GL20.GL_TRIANGLES, mesh.indicesLenght(), GL20.GL_UNSIGNED_INT, 0);

            mesh.material().diffuseTexture().deactivate();
        });
        GL30.glBindVertexArray(0);
    }

    private void setMeshMaterial(Mesh mesh) {
        shader.setVec3("model.material.diffuseColor", mesh.material().diffuseColor());
        shader.setVec3("model.material.specularColor", mesh.material().specularColor());
        shader.setInt("model.material.diffuseTexture", mesh.material().diffuseTexture().ID());
        shader.setInt("model.material.specularTexture", mesh.material().specularTexture().ID());
        shader.setFloat("model.material.shininess", mesh.material().shininess());
        shader.setBool("model.material.useTextures", mesh.material().useTextures());
    }

    public void end() {
        Shader.unbind();
    }
}
