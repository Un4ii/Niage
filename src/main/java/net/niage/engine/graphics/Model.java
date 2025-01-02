package net.niage.engine.graphics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import net.niage.animation.Animation;

public class Model {

    private final List<Mesh> meshes;
    private Matrix4f transform = new Matrix4f().identity();

    public Model(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    public Model(Mesh mesh) {
        this.meshes = new ArrayList<>();
        this.meshes.add(mesh);
    }

    public void dispose() {
        meshes.forEach(mesh -> mesh.dispose());
    }

    public List<Mesh> meshes() {
        return meshes;
    }

    public Matrix4f transform() {
        return transform;
    }

    public void startAnimation(String animationName) {
        meshes.forEach(mesh -> {
            mesh.animations().forEach(anim -> {
                if (anim.name().equals(animationName))
                    anim.start();
            });
        });
    }

    public void pauseAnimation(String animationName) {
        meshes.forEach(mesh -> {
            mesh.animations().forEach(anim -> {
                if (anim.name().equals(animationName))
                    anim.pause();
            });
        });
    }

    public void resumeAnimation(String animationName) {
        meshes.forEach(mesh -> {
            mesh.animations().forEach(anim -> {
                if (anim.name().equals(animationName))
                    anim.resume();
            });
        });
    }

    public void stopAnimation(String animationName) {
        meshes.forEach(mesh -> {
            mesh.animations().forEach(anim -> {
                if (anim.name().equals(animationName))
                    anim.stop();
            });
        });
    }

    public void loopAnimation(String animationName, boolean loop) {
        meshes.forEach(mesh -> {
            mesh.animations().forEach(anim -> {
                if (anim.name().equals(animationName))
                    anim.setLoop(loop);
            });
        });
    }

    public boolean isLoopAnimation(String animationName) {
        for (Mesh mesh : meshes) {
            for (Animation anim : mesh.animations()) {
                if (anim.name().equals(animationName) && anim.loop() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAnimating(String animationName) {
        for (Mesh mesh : meshes) {
            for (Animation anim : mesh.animations()) {
                if (anim.name().equals(animationName) && anim.animating() == true) {
                    return true;
                }
            }
        }
        return false;
    }

}
