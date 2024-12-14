package net.niage.engine.texture;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import net.niage.engine.utils.FileUtils;

public class TextureManager {

    // Texture 2D
    public static int loadImage2D(File image) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        loadImage(image.toPath().toString(), (data, width, height, nrChannels) -> {
            int format = (nrChannels == 4) ? GL11.GL_RGBA : GL11.GL_RGB;
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, data);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        });

        return texture;
    }

    public static void activate2D(Texture2D texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + texture.ID());
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.ID());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    public static void deactivate2D(Texture2D texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + texture.ID());
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    // Texture Cubemap
    public static int loadImageCubemap(File... images) {
        int texture = GL15.glGenTextures();
        GL15.glBindTexture(GL15.GL_TEXTURE_CUBE_MAP, texture);

        GL15.glTexParameteri(GL15.GL_TEXTURE_CUBE_MAP, GL15.GL_TEXTURE_MAG_FILTER, GL15.GL_LINEAR);
        GL15.glTexParameteri(GL15.GL_TEXTURE_CUBE_MAP, GL15.GL_TEXTURE_MIN_FILTER, GL15.GL_LINEAR);
        GL15.glTexParameteri(GL15.GL_TEXTURE_CUBE_MAP, GL15.GL_TEXTURE_WRAP_S, GL15.GL_CLAMP_TO_EDGE);
        GL15.glTexParameteri(GL15.GL_TEXTURE_CUBE_MAP, GL15.GL_TEXTURE_WRAP_T, GL15.GL_CLAMP_TO_EDGE);
        GL15.glTexParameteri(GL15.GL_TEXTURE_CUBE_MAP, GL15.GL_TEXTURE_WRAP_R, GL15.GL_CLAMP_TO_EDGE);

        int[] cubemapFaces = {
                GL15.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
                GL15.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
                GL15.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
                GL15.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
                GL15.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
                GL15.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
        };

        for (int index = 0; index < images.length; index++) {
            int face = cubemapFaces[index];
            loadImage(images[index].toPath().toString(), (data, width, height, nrChannels) -> {
                int format = (nrChannels == 4) ? GL11.GL_RGBA : GL11.GL_RGB;
                GL15.glTexImage2D(face, 0, format, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, data);
            });
        }

        return texture;
    }

    public static void activateCubemap(TextureCubemap texture) {
        GL15.glActiveTexture(GL15.GL_TEXTURE0 + texture.ID());
        GL15.glBindTexture(GL15.GL_TEXTURE_CUBE_MAP, texture.ID());
        GL15.glActiveTexture(GL15.GL_TEXTURE0);
    }

    public static void deactivateCubemap(TextureCubemap texture) {
        GL15.glActiveTexture(GL15.GL_TEXTURE0 + texture.ID());
        GL15.glBindTexture(GL15.GL_TEXTURE_CUBE_MAP, 0);
        GL15.glActiveTexture(GL15.GL_TEXTURE0);
    }

    // Dispose
    public static void dispose(Texture2D texture) {
        GL11.glDeleteTextures(texture.ID());
    }

    public static void dispose(TextureCubemap texture) {
        GL11.glDeleteTextures(texture.ID());
    }

    // Load image util
    private static void loadImage(String filePath, ImageCallback callback) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer nrChannels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(false);
            ByteBuffer data = STBImage.stbi_load(filePath, width, height, nrChannels, 0);
            if (data != null) {
                callback.process(data, width.get(), height.get(), nrChannels.get());

                STBImage.stbi_image_free(data);
            } else {
                System.out.println("ERROR::ASSET::IMAGE::LOAD_FAILURE\nImage: " + filePath);
            }
        }
    }

    @FunctionalInterface
    private static interface ImageCallback {
        void process(ByteBuffer data, int width, int height, int nrChannels);
    }

    // Dummy texture
    public static int dummyTexture() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(4); // 1 píxel, 4 componentes (RGBA)
        buffer.put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255); // Blanco (1.0, 1.0, 1.0, 1.0)
        buffer.flip();

        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 1, 1, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Configurar parámetros básicos
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        return textureID;
    }

}
