package net.niage.engine.graphics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

public class Model {

    private final List<Mesh> meshes;
    private Matrix4f transform = new Matrix4f().translation(0.0f, 0.0f, 0.0f);

    public Model(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    public Model(Mesh mesh) {
        this.meshes = new ArrayList<>();
        this.meshes.add(mesh);
    }

    public void dispose() {
        meshes.forEach(mesh -> mesh.dispose());
    }

    public List<Mesh> meshes() {
        return meshes;
    }

    public Matrix4f transform() {
        return transform;
    }
}
