package net.niage.engine.camera;

import java.nio.FloatBuffer;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.system.MemoryUtil;

public class PerspectiveCamera extends Camera {

    private int viewportWidth, viewportHeight;
    private Matrix4f projection = new Matrix4f().identity();

    public PerspectiveCamera(float FOV, float zNear, float zFar, int viewportWidth, int viewportHeight) {
        super(FOV, zNear, zFar);
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    @Override
    void create() {
        projection.setPerspective(
                (float) Math.toRadians(FOV),
                (float) viewportWidth / (float) viewportHeight,
                zNear,
                zFar);
    }

    @Override
    void update() {
        right.set(front).cross(worldUp).normalize();
        up.set(right).cross(front).normalize();

        view.identity().lookAt(position, new Vector3f(position).add(front), up);

        updateUBO();
    }

    @Override
    void updateAspectRatio(int width, int height) {
        projection.setPerspective(
                (float) Math.toRadians(FOV),
                (float) viewportWidth / (float) viewportHeight,
                zNear,
                zFar);
    }

    @Override
    void lookAt(Vector3f target) {
        front.set(target).sub(position).normalize();

    }

    @Override
    void lookAt(float x, float y, float z) {
        front.set(x, y, z).sub(position).normalize();
    }

    private int cameraUBO;
    private final int uboSize = 16 * 4 + 16 * 4 + 3 * 4;
    private FloatBuffer buffer = BufferUtils.createFloatBuffer(uboSize / Float.BYTES);;

    @Override
    void createUBO() {
        cameraUBO = GL15.glGenBuffers();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUBO);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, uboSize, GL15.GL_DYNAMIC_DRAW);
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, cameraUBO);
    }

    @Override
    void updateUBO() {
        buffer.clear();
        buffer.put(projection.get(new float[16]));
        buffer.put(view.get(new float[16]));
        buffer.put(new float[] { position.x, position.y, position.z });
        buffer.flip();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buffer);

        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            throw new RuntimeException("ERROR::CAMERA::PERSPECTIVE::UBO::UPDATE\\n" + error);
        }
    }

    @Override
    void dispose() {
        MemoryUtil.memFree(buffer);
        GL15.glDeleteBuffers(cameraUBO);
    }

    public Matrix4f projection() {
        return projection;
    }

}
