package net.niage.engine.texture;

import java.io.File;

public class TextureCubemap {

    public final int ID;

    public TextureCubemap(String assetsFolder) throws Exception {
        String path = assetsFolder.endsWith("/") ? assetsFolder : assetsFolder + "/";
        String extensions[] = { ".png", ".jpg", ".jpeg" };

        File[] images = new File[6];
        for (String ext : extensions) {
            File right = new File(path + "right" + ext);
            if (right.exists()) {
                images[0] = right;
            }

            File left = new File(path + "left" + ext);
            if (left.exists()) {
                images[1] = left;
            }

            File top = new File(path + "top" + ext);
            if (top.exists()) {
                images[2] = top;
            }

            File bottom = new File(path + "bottom" + ext);
            if (bottom.exists()) {
                images[3] = bottom;
            }

            File front = new File(path + "front" + ext);
            if (front.exists()) {
                images[4] = front;
            }

            File back = new File(path + "back" + ext);
            if (back.exists()) {
                images[5] = back;
            }
        }

        for (File image : images) {
            if (image == null) {
                throw new RuntimeException("ERROR::TEXTURE::CUBEMAP::MISSING_TEXTURES");
            }
        }

        this.ID = TextureManager.loadImageCubemap(images);
    }

    public void activate() {
        TextureManager.activateCubemap(this);
    }

    public void deactivate() {
        TextureManager.deactivateCubemap(this);
    }

    public void dispose() {
        TextureManager.dispose(this);
    }

    public int ID() {
        return ID;
    }

}
