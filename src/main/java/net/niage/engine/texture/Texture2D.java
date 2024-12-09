package net.niage.engine.texture;

import java.io.File;

public class Texture2D {

    private final int ID;

    public Texture2D(File image) {
        this.ID = TextureManager.loadImage2D(image);
    }

    public Texture2D() {
        this.ID = TextureManager.dummyTexture();
    }

    public void activate() {
        TextureManager.activate2D(this);
    }

    public void deactivate() {
        TextureManager.deactivate2D(this);
    }

    public void dispose() {
        TextureManager.dispose(this);
    }

    public int ID() {
        return ID;
    }

}
