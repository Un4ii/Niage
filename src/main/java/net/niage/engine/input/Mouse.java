package net.niage.engine.input;

import org.lwjgl.glfw.GLFW;

import net.niage.game.Game;

public class Mouse {

    private boolean[] pressedBtns = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private double xPos = 0, yPos = 0;
    private double deltaX = 0, deltaY = 0;
    private boolean cursorCatched = false;

    public Mouse() {
    }

    public void btnCallback(long window, int button, int action, int mods) {
        if (button >= 0 && button < GLFW.GLFW_MOUSE_BUTTON_LAST) {
            pressedBtns[button] = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
        }
    }

    public void posCallback(long window, double xpos, double ypos) {
        deltaX = xpos - xPos;
        deltaY = ypos - yPos;
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

    public double deltaX() {
        return deltaX;
    }

    public double deltaY() {
        return deltaY;
    }

    public void resetDeltas() {
        deltaX = 0;
        deltaY = 0;
    }

    public void catchCursor(boolean catchCursor) {
        if (catchCursor != cursorCatched) {
            if (catchCursor) {
                GLFW.glfwSetInputMode(Game.window().getWindow(), GLFW.GLFW_CURSOR,
                        GLFW.GLFW_CURSOR_DISABLED);
            } else {
                GLFW.glfwSetInputMode(Game.window().getWindow(), GLFW.GLFW_CURSOR,
                        GLFW.GLFW_CURSOR_NORMAL);
            }
            cursorCatched = catchCursor;
        }
    }

    public boolean cursorCatched() {
        return cursorCatched;
    }

    public boolean isAnyPressed() {
        for (boolean pressed : pressedBtns) {
            if (pressed) {
                return true;
            }
        }
        return false;
    }

}
