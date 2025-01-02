package net.niage.animation;

import org.joml.Vector3f;
import org.joml.Quaternionf;

public class KeyFrame {

    private float time;

    private Vector3f position;
    private Quaternionf rotation;
    private Vector3f scale;

    public KeyFrame(float time, Vector3f position, Quaternionf rotation, Vector3f scale) {
        this.time = time;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f interpolatePosition(KeyFrame next, float time) {
        float t = (time - this.time()) / (next.time() - this.time());
        return new Vector3f().lerp(this.position(), t, next.position());
    }

    public Quaternionf interpolateRotation(KeyFrame next, float time) {
        float t = (time - this.time()) / (next.time() - this.time());
        return this.rotation().slerp(next.rotation(), t);
    }

    public Vector3f interpolateScale(KeyFrame next, float time) {
        float t = (time - this.time()) / (next.time() - this.time());
        return new Vector3f().lerp(this.scale(), t, next.scale());
    }

    public float time() {
        return time;
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

}
