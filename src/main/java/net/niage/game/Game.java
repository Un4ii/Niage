package net.niage.game;

import net.niage.engine.camera.FirstPersonController;
import net.niage.engine.camera.PerspectiveCamera;
import net.niage.engine.core.Engine;
import net.niage.engine.graphics.Model;
import net.niage.engine.graphics.Renderer;
import net.niage.engine.graphics.Shader;
import net.niage.engine.lighting.LightScene;
import net.niage.engine.utils.ModelUtils;

public class Game extends Engine {

    public Game() throws Exception {
        super();
    }

    private PerspectiveCamera camera;
    private FirstPersonController controller;

    private Renderer renderer;
    private LightScene lightScene;

    private Shader objectShader;
    private Model cube;

    @Override
    protected void create() throws Exception {
        timer.setFrameRate(-1);
        window.setvSync(false);
        camera = new PerspectiveCamera(70, 0.01f, 100.0f, window.getWidth(), window.getHeight());
        camera.position().set(0f, 3.0f, -3f);
        camera.lookAt(0, 0, 0);
        camera.update();

        controller = new FirstPersonController(camera);
        controller.setWalkVelocity(5f);
        controller.setRunVelocity(10f);

        renderer = new Renderer();
        lightScene = new LightScene();
        lightScene.sunDirection().set(1, 0.5, 0.2);

        objectShader = new Shader("assets/shaders/default.vs", "assets/shaders/default.fs");
        cube = ModelUtils.loadModel("assets/models/animation/animation.gltf");

        // cube.startAnimation("TorusAction");
    }

    @Override
    protected void render(double deltaTime) {
        controller.update(deltaTime);
        camera.update();

        Renderer.glClear(Renderer.COLOR_BUFFER_BIT | Renderer.DEPTH_BUFFER_BIT);

        renderer.start(objectShader, deltaTime);
        renderer.render(cube);
        renderer.end();
    }

    @Override
    protected void update(double deltaTime) {
        window.setTitle(timer.fps() + "  -  " + timer.tps());
    }

    @Override
    protected void resize(int width, int height) {
        camera.updateAspectRatio(width, height);
    }

    @Override
    protected void cleanup() {
        objectShader.dispose();
        cube.dispose();
        camera.dispose();
    }
}
