package net.niage.engine.texture;

import net.niage.engine.utils.FileUtils;

public class Texture2D {

    private final int ID;

    public Texture2D(String imagePath) {
        this.ID = TextureManager.loadImage2D(FileUtils.getFile(imagePath));
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
