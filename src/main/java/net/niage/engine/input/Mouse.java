package net.niage.engine.input;

import org.lwjgl.glfw.GLFW;

public class Mouse {

    private boolean[] pressedBtns = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private double xPos = 0, yPos = 0;

    public Mouse() {
    }

    public void btnCallback(long window, int button, int action, int mods) {
        if (button >= 0 && button < GLFW.GLFW_MOUSE_BUTTON_LAST) {
            pressedBtns[button] = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
        }
    }

    public void posCallback(long window, double xpos, double ypos) {
        xPos = xpos;
        yPos = ypos;
    }

    public boolean isPressed(int glfwBtn) {
        return pressedBtns[glfwBtn];
    }

    public double xPos() {
        return xPos;
    }

    public double yPos() {
        return yPos;
    }
}
