package net.niage.game;

import net.niage.engine.camera.PerspectiveCamera;
import net.niage.engine.core.Engine;
import net.niage.engine.graphics.Model;
import net.niage.engine.graphics.Renderer;
import net.niage.engine.graphics.Shader;
import net.niage.engine.utils.ModelUtils;

public class Game extends Engine {

    public Game() throws Exception {
        super();
    }

    private PerspectiveCamera camera;
    private Shader shader;
    private Renderer renderer;

    private Model cube;

    @Override
    protected void create() throws Exception {
        camera = new PerspectiveCamera(70, 0.01f, 100.0f, window.getWidth(), window.getHeight());
        camera.position().set(2.0f, 2.0f, -2f);
        camera.lookAt(0, 0, 0);
        camera.update();

        shader = new Shader("assets/cube.vs", "assets/cube.fs");
        renderer = new Renderer();

        cube = ModelUtils.loadModel("assets/models/cube/untitled.gltf");
    }

    @Override
    protected void render() {
        Renderer.glClear(Renderer.COLOR_BUFFER_BIT | Renderer.DEPTH_BUFFER_BIT);
        Renderer.glClearColor(0, 0, 0, 0);

        renderer.start(shader);
        renderer.render(cube);
        renderer.end();
    }

    @Override
    protected void update(double deltaTime) {
        super.window.setTitle(super.timer.fps() + "  -  " + super.timer.tps());
    }

    @Override
    protected void resize(int width, int height) {

    }

    @Override
    protected void cleanup() {
        shader.dispose();
        cube.dispose();
        camera.dispose();
    }

    // float vertices[] = {
    // // Posiciones // Normales // Textura
    // -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 0 (frontal)
    // 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, // 1 (frontal)
    // 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, // 2 (frontal)
    // -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, // 3 (frontal)
    // -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 4 (trasera)
    // 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, // 5 (trasera)
    // 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, // 6 (trasera)
    // -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f // 7 (trasera)
    // };

    // int indices[] = {
    // // Cara frontal (CCW)
    // 0, 3, 2, 2, 1, 0,
    // // Cara trasera (CCW)
    // 4, 5, 6, 6, 7, 4,
    // // Cara izquierda (CCW)
    // 4, 7, 3, 3, 0, 4,
    // // Cara derecha (CCW)
    // 1, 2, 6, 6, 5, 1,
    // // Cara inferior (CCW)
    // 0, 1, 5, 5, 4, 0,
    // // Cara superior (CCW)
    // 3, 7, 6, 6, 2, 3
    // };

}
