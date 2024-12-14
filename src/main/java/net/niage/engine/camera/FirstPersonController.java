package net.niage.engine.camera;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import net.niage.engine.input.InputHandler;
import net.niage.engine.utils.MathUtils;

public class FirstPersonController {

    private final Camera camera;

    private final float lookRadius = 5.0f;
    private Vector3f lookAt = new Vector3f();
    private double pitch, pitch_rad;
    private double yaw, yaw_rad;
    private float sensitivity = 1f;

    private Vector3f movement = new Vector3f();
    private float walkVelocity = 2.0f;
    private float runVelocity = 5.0f;
    private float currentVel = walkVelocity;

    private int FORWARD = GLFW.GLFW_KEY_W;
    private int BACKWARD = GLFW.GLFW_KEY_S;
    private int LEFT = GLFW.GLFW_KEY_A;
    private int RIGHT = GLFW.GLFW_KEY_D;
    private int UP = GLFW.GLFW_KEY_SPACE;
    private int DOWN = GLFW.GLFW_KEY_LEFT_CONTROL;
    private int RUN = GLFW.GLFW_KEY_LEFT_SHIFT;

    public FirstPersonController(Camera camera) {
        this.camera = camera;
        pitch = Math.toDegrees(Math.asin(camera.front().y));
        yaw = Math.toDegrees(Math.atan2(camera.front().z, camera.front().x));
        pitch_rad = Math.toRadians(pitch);
        yaw_rad = Math.toRadians(yaw);
    }

    public void update(double deltaTime) {
        if (InputHandler.mouse.cursorCatched()) {
            if (InputHandler.keyboard.isPressed(GLFW.GLFW_KEY_ESCAPE))
                InputHandler.mouse.catchCursor(false);

            updateLookAt();
            updatePos(deltaTime);
        } else if (InputHandler.mouse.isAnyPressed()) {
            InputHandler.mouse.catchCursor(true);
        }
    }

    private void updateLookAt() {
        pitch += -InputHandler.mouse.deltaY() * sensitivity;
        yaw += InputHandler.mouse.deltaX() * sensitivity;

        pitch = MathUtils.clampd(pitch, -89, 89);
        yaw = MathUtils.wrapd(yaw, 0, 359);

        pitch_rad = Math.toRadians(pitch);
        yaw_rad = Math.toRadians(yaw);

        lookAt.set(
                lookRadius * Math.cos(pitch_rad) * Math.cos(yaw_rad),
                lookRadius * Math.sin(pitch_rad),
                lookRadius * Math.sin(yaw_rad) * Math.cos(pitch_rad));

        camera.lookAt(lookAt.add(camera.position));
        camera.up.set(0, 1, 0);

        InputHandler.mouse.resetDeltas();
    }

    private void updatePos(double deltaTime) {
        Vector3f front = new Vector3f(camera.front.x, 0, camera.front.z).normalize();
        Vector3f up = new Vector3f(0, 1, 0).normalize();

        if (InputHandler.keyboard.isPressed(FORWARD)) {
            movement.add(front);
        }
        if (InputHandler.keyboard.isPressed(BACKWARD)) {
            movement.sub(front);
        }
        if (InputHandler.keyboard.isPressed(LEFT)) {
            movement.sub(camera.right);
        }
        if (InputHandler.keyboard.isPressed(RIGHT)) {
            movement.add(camera.right);
        }
        if (InputHandler.keyboard.isPressed(UP)) {
            movement.add(up);
        }
        if (InputHandler.keyboard.isPressed(DOWN)) {
            movement.sub(up);
        }

        currentVel = InputHandler.keyboard.isPressed(RUN) ? runVelocity : walkVelocity;

        if (movement.length() > 0)
            movement.normalize();

        camera.position.add(movement.mul(currentVel * (float) deltaTime));
        movement.set(0.0f, 0.0f, 0.0f);
    }

    public float walkVelocity() {
        return walkVelocity;
    }

    public void setWalkVelocity(float newVel) {
        walkVelocity = newVel;
    }

    public float runVelocity() {
        return runVelocity;
    }

    public void setRunVelocity(float newVel) {
        runVelocity = newVel;
    }

    public int FORWARD() {
        return FORWARD;
    }

    public int BACKWARD() {
        return BACKWARD;
    }

    public int LEFT() {
        return LEFT;
    }

    public int RIGHT() {
        return RIGHT;
    }

    public int UP() {
        return UP;
    }

    public int DOWN() {
        return DOWN;
    }

    public int RUN() {
        return RUN;
    }

    public void setFORWARD(int newFORWARD) {
        FORWARD = newFORWARD;
    }

    public void setBACKWARD(int newBACKWARD) {
        BACKWARD = newBACKWARD;
    }

    public void setLEFT(int newLEFT) {
        LEFT = newLEFT;
    }

    public void setRIGHT(int newRIGHT) {
        RIGHT = newRIGHT;
    }

    public void setUP(int newUP) {
        UP = newUP;
    }

    public void setDOWN(int newDOWN) {
        DOWN = newDOWN;
    }

    public void setRUN(int newRUN) {
        RUN = newRUN;
    }
}
