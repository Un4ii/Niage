package net.niage.engine.input;

import org.lwjgl.glfw.GLFW;

public class Keyboard {

    private boolean[] pressedKeys = new boolean[GLFW.GLFW_KEY_LAST];

    public Keyboard() {
    }

    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key < GLFW.GLFW_KEY_LAST) {
            pressedKeys[key] = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
        }
    }

    public boolean isPressed(int glfwKey) {
        return pressedKeys[glfwKey];
    }
}
