package net.niage.engine.graphics.animation;

import java.util.List;

import org.joml.Matrix4f;

public class Bone {

    private final String name;
    private final Bone parent;
    private final List<Bone> children;
    private Matrix4f localTransform;
    private Matrix4f globalTransform;

    public Bone(String name, Bone parent, List<Bone> children, Matrix4f localTransform, Matrix4f globalTransform) {
        this.name = name;
        this.parent = parent;
        this.children = children;
        this.localTransform = localTransform;
        this.globalTransform = globalTransform;
    }

    public String name() {
        return name;
    }

    public Bone parent() {
        return parent;
    }

    public List<Bone> children() {
        return children;
    }

    public Matrix4f localTransform() {
        return localTransform;
    }

    public Matrix4f globalTransform() {
        return globalTransform;
    }

}
