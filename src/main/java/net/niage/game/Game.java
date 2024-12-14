package net.niage.game;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import net.niage.engine.camera.PerspectiveCamera;
import net.niage.engine.core.Engine;
import net.niage.engine.graphics.Material;
import net.niage.engine.graphics.Mesh;
import net.niage.engine.graphics.Model;
import net.niage.engine.graphics.Renderer;
import net.niage.engine.graphics.Shader;

public class Game extends Engine {

    public Game() throws Exception {
        super();
    }

    private PerspectiveCamera camera;
    private Shader shader;
    private Renderer renderer;

    private Material cubeMaterial;
    private Mesh cubeMesh;
    private Model cube;

    @Override
    protected void create() throws Exception {
        camera = new PerspectiveCamera(70, 0.01f, 100.0f, window.getWidth(), window.getHeight());
        camera.position().set(5.0f, 5.0f, -5f);
        camera.lookAt(0, 0, 0);
        camera.update();

        shader = new Shader("assets/cube.vs", "assets/cube.fs");
        renderer = new Renderer(shader);

        cubeMaterial = new Material(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f), 0);
        cubeMesh = new Mesh(vertices, indices, cubeMaterial);
        cube = new Model(cubeMesh);
    }

    @Override
    protected void render() {
        Renderer.glClear(Renderer.COLOR_BUFFER_BIT | Renderer.DEPTH_BUFFER_BIT);
        Renderer.glClearColor(0, 0, 0, 0);

        renderer.start();
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

    float[] vertices = {
            // Posición // Normal // Textura
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 0: Izquierda-abajo-atrás
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, // 1: Derecha-abajo-atrás
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // 2: Derecha-arriba-atrás
            -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, // 3: Izquierda-arriba-atrás
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, // 4: Izquierda-abajo-delante
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, // 5: Derecha-abajo-delante
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // 6: Derecha-arriba-delante
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f // 7: Izquierda-arriba-delante
    };

    int[] indices = {
            // Cara frontal
            4, 5, 6, 6, 7, 4,

            // Cara trasera
            0, 1, 2, 2, 3, 0,

            // Cara izquierda
            0, 4, 7, 7, 3, 0,

            // Cara derecha
            1, 5, 6, 6, 2, 1,

            // Cara superior
            3, 7, 6, 6, 2, 3,

            // Cara inferior
            0, 4, 5, 5, 1, 0
    };

}
