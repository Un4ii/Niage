package net.niage.engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {

    protected final Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

    protected Vector3f position = new Vector3f(0.0f);
    protected Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    protected Vector3f right = new Vector3f();
    protected Vector3f up = new Vector3f();

    protected Matrix4f view = new Matrix4f().identity();
    protected Matrix4f projection = new Matrix4f().identity();

    protected float FOV, zNear, zFar;

    public Camera(float FOV, float zNear, float zFar) {
        this.FOV = FOV;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    protected abstract void create();

    public abstract void update();

    public abstract void updateAspectRatio(int width, int height);

    public abstract void lookAt(Vector3f target);

    public abstract void lookAt(float x, float y, float z);

    protected abstract void createUBO();

    protected abstract void updateUBO();

    public abstract void dispose();

    public Vector3f position() {
        return position;
    }

    public Vector3f front() {
        return front;
    }

    public Vector3f right() {
        return right;
    }

    public Vector3f up() {
        return up;
    }

    public Matrix4f view() {
        return view;
    }

    public float FOV() {
        return FOV;
    }

    public float zNear() {
        return zNear;
    }

    public float zFar() {
        return zFar;
    }
}
