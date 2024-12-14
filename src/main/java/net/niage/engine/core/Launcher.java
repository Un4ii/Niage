package net.niage.engine.core;

import net.niage.game.Game;

public class Launcher {

    public static void main(String[] args) {
        try {
            new Game().run();
        } catch (Exception e) {
            throw new RuntimeException("ERROR::LAUNCHER::START\n" + e);
        }
    }

}
