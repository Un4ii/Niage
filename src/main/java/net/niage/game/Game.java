package net.niage.game;

import net.niage.engine.core.Engine;

public class Game extends Engine {

    public Game() throws Exception {
        super();
    }

    @Override
    protected void create() throws Exception {

    }

    @Override
    protected void render() {

    }

    @Override
    protected void update(double deltaTime) {
        super.window.setTitle("" + super.timer.FPS());
    }

    @Override
    protected void resize(int width, int height) {

    }

    @Override
    protected void cleanup() {

    }

}
