package net.niage.engine.core;

import org.lwjgl.glfw.GLFW;

import net.niage.engine.input.InputHandler;

public abstract class Engine {

    protected static Window window;
    protected static Timer timer;

    public Engine() throws Exception {
        window = new Window();
        timer = new Timer();
    }

    public void run() throws Exception {
        GLFW.glfwSetWindowSizeCallback(window.getWindow(), (_window, width, height) -> {
            window.resize(width, height);
            resize(width, height);
        });

        GLFW.glfwSetKeyCallback(window.getWindow(), (_window, key, scancode, action, mods) -> {
            InputHandler.keyboard.keyCallback(_window, key, scancode, action, mods);
        });

        GLFW.glfwSetMouseButtonCallback(window.getWindow(), (_window, button, action, mods) -> {
            InputHandler.mouse.btnCallback(_window, button, action, mods);
        });

        GLFW.glfwSetCursorPosCallback(window.getWindow(), (_window, xpos, ypos) -> {
            InputHandler.mouse.posCallback(_window, xpos, ypos);
        });

        create();

        while (!window.windowShouldClose()) {
            timer.update();

            if (timer.shouldUpdate()) {
                update(timer.deltaTime()); // Lógica del juego
            }

            if (timer.shouldRender()) {
                render(timer.deltaTime()); // Renderizado
            }

            timer.waitForNextFrame(); // Espera hasta el siguiente fotograma

            window.update();
        }

        window.dispose();
        cleanup();
    }

    protected abstract void create() throws Exception;

    protected abstract void render(double deltaTime);

    protected abstract void update(double deltaTime);

    protected abstract void resize(int width, int height);

    protected abstract void cleanup();

    public static Window window() {
        return window;
    }

    public static Timer timer() {
        return timer;
    }
}
