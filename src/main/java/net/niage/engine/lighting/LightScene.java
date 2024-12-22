package net.niage.engine.lighting;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class LightScene {

    private Vector3f sunDirection = new Vector3f(1.0f, -1.0f, -1.0f);
    private float shadowStrenght = 0.1f;
    private Vector3f sunColor = new Vector3f(1.0f);

    // sunDirection shadowStrenght sunColor padding
    private final int uboSize = (3 * 4) + (1 * 4) + (3 * 4) + (1 * 4);
    private int lightingUBO;

    private FloatBuffer buffer;

    public LightScene() {
        createUBO();
        updateUBO();
    }

    private void createUBO() {
        lightingUBO = GL15.glGenBuffers();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, lightingUBO);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, uboSize, GL15.GL_DYNAMIC_DRAW);
        GL30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 2, lightingUBO);

        buffer = BufferUtils.createFloatBuffer(uboSize / Float.BYTES);
    }

    public void updateUBO() {
        buffer.clear();
        buffer.put(new float[] { sunDirection.x, sunDirection.y, sunDirection.z });
        buffer.put(shadowStrenght);
        buffer.put(new float[] { sunColor.x, sunColor.y, sunColor.z });
        buffer.put(0f);
        buffer.flip();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, lightingUBO);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, buffer);

        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            throw new RuntimeException("ERROR::LIGHT_SCENE::UBO::UPDATE\n" + error);
        }
    }

    public Vector3f sunDirection() {
        return sunDirection;
    }

    public float shadowStrenght() {
        return shadowStrenght;
    }

    public void setShadowStrenght(float shadowStrenght) {
        this.shadowStrenght = shadowStrenght;
    }

    public Vector3f sunColor() {
        return sunColor;
    }
}
