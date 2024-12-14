package net.niage.engine.camera;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.system.MemoryUtil;

public class OrthographicCamera extends Camera {

    private float left, right, bottom, top;

    public OrthographicCamera(float left, float right, float bottom, float top, float zNear, float zFar) {
        super(0, zNear, zFar);
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        create();
        createUBO();
        update();
    }

    @Override
    protected void create() {
        projection.setOrtho(left, right, bottom, top, zNear, zFar);
    }

    @Override
    public void update() {
        super.right.set(front).cross(worldUp).normalize();
        up.set(right).cross(front).normalize();

        view.identity().lookAt(position, new Vector3f(position).add(front), up);

        updateUBO();
    }

    @Override
    public void updateAspectRatio(int width, int height) {
        float aspect = (float) width / height;
        float verticalSize = top - bottom;

        float horizontalSize = right - left;

        if (aspect > 1) {
            top = verticalSize * 0.5f;
            bottom = -top;
            right = top * aspect;
            left = -right;
        } else {
            right = horizontalSize * 0.5f;
            left = -right;
            top = right / aspect;
            bottom = -top;
        }

        projection.setOrtho(left, right, bottom, top, zNear, zFar);
    }

    @Override
    public void lookAt(Vector3f target) {
        front.set(target).sub(position).normalize();
    }

    @Override
    public void lookAt(float x, float y, float z) {
        front.set(x, y, z).sub(position).normalize();
    }

    private int cameraUBO;
    private final int uboSize = 16 * 4 + 16 * 4 + 3 * 4;
    private FloatBuffer buffer;

    @Override
    protected void createUBO() {
        cameraUBO = GL15.glGenBuffers();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUBO);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, uboSize, GL15.GL_DYNAMIC_DRAW);
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, cameraUBO);

        buffer = BufferUtils.createFloatBuffer(uboSize / Float.BYTES);
    }

    @Override
    protected void updateUBO() {
        buffer.clear();
        buffer.put(projection.get(new float[16]));
        buffer.put(view.get(new float[16]));
        buffer.put(new float[] { position.x, position.y, position.z });
        buffer.flip();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, cameraUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buffer);

        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            throw new RuntimeException("ERROR::CAMERA::ORTHOGRAPHIC::UBO::UPDATE\n" + error);
        }
    }

    @Override
    public void dispose() {
        MemoryUtil.memFree(buffer);
        GL15.glDeleteBuffers(cameraUBO);
    }

    public Matrix4f projection() {
        return projection;
    }

}
