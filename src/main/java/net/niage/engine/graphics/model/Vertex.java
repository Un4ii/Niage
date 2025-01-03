package net.niage.engine.graphics.model;

import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import net.niage.engine.graphics.animation.BoneInfluence;

public class Vertex {

    private final Vector3f position;
    private final Vector3f normal;
    private final Vector2f textCoords;
    private final List<BoneInfluence> boneInfluences;

    public Vertex(Vector3f position, Vector3f normal, Vector2f textCoords, List<BoneInfluence> boneInfluences) {
        this.position = position;
        this.normal = normal;
        this.textCoords = textCoords;
        this.boneInfluences = boneInfluences;
    }

    public Vector3f position() {
        return position;
    }

    public Vector3f normal() {
        return normal;
    }

    public Vector2f textCoords() {
        return textCoords;
    }

    public List<BoneInfluence> boneInfluences() {
        return boneInfluences;
    }

}
