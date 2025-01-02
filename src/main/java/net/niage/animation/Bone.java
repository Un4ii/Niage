package net.niage.animation;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Bone {

    private Vector3f position;
    private Quaternionf rotation;
    private Vector3f scale;
    private Matrix4f globalTransformation;
    private Bone parent;
    private List<Bone> children;
    private String name;

    public Bone(String name, Vector3f position, Quaternionf rotation, Vector3f scale) {
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.children = new ArrayList<>();
        this.globalTransformation = new Matrix4f();
    }

    public Vector3f position() {
        return position;
    }

    public Quaternionf rotation() {
        return rotation;
    }

    public Vector3f scale() {
        return scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setParent(Bone parent) {
        this.parent = parent;
    }

    public void addChild(Bone child) {
        this.children.add(child);
    }

    public Matrix4f calculateLocalTransformation() {
        Matrix4f transformation = new Matrix4f();
        transformation.translate(position)
                .rotate(rotation)
                .scale(scale);
        return transformation;
    }

    public Matrix4f calculateGlobalTransformation() {
        if (parent != null) {
            globalTransformation.set(parent.calculateGlobalTransformation())
                    .mul(calculateLocalTransformation());
        } else {
            globalTransformation.set(calculateLocalTransformation());
        }
        return globalTransformation;
    }

    public Matrix4f globalTransformation() {
        return globalTransformation;
    }

    public List<Bone> children() {
        return children;
    }

    public Bone parent() {
        return parent;
    }

    public String name() {
        return name;
    }
}
