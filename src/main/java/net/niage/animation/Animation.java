package net.niage.animation;

import java.util.ArrayList;
import java.util.List;

public class Animation {

    private String name;
    private List<BoneAnimation> boneAnimations;
    private float duration;
    private float currentTime;
    private boolean loop;
    private boolean animating;

    public Animation(String name, float duration) {
        this.name = name;
        this.boneAnimations = new ArrayList<>();
        this.duration = duration;
        this.currentTime = 0;
        this.loop = false;
        this.animating = false;
    }

    public Animation(String name, List<BoneAnimation> boneAnimations, float duration) {
        this.name = name;
        this.boneAnimations = boneAnimations;
        this.duration = duration;
        this.currentTime = 0;
        this.loop = false;
        this.animating = false;
    }

    public void update(double deltaTime) {
        if (animating) {
            currentTime += deltaTime;

            if (currentTime > duration) {
                if (loop) {
                    currentTime = 0;
                } else {
                    animating = false;
                    currentTime = 0;
                }
            }

            for (BoneAnimation boneAnimation : boneAnimations) {
                Bone bone = boneAnimation.bone();
                KeyFrame current = getCurrentKeyFrame(boneAnimation);
                KeyFrame next = getNextKeyFrame(boneAnimation);

                bone.setPosition(current.interpolatePosition(next, (float) deltaTime));
                bone.setRotation(current.interpolateRotation(next, (float) deltaTime));
                bone.setScale(current.interpolateScale(next, (float) deltaTime));
            }
        }
    }

    private KeyFrame getCurrentKeyFrame(BoneAnimation boneAnimation) {
        List<KeyFrame> keyFrames = boneAnimation.keyFrames();

        KeyFrame currentKeyFrame = null;
        for (KeyFrame keyFrame : keyFrames) {
            if (keyFrame.time() >= currentTime) {
                currentKeyFrame = keyFrame;
                break;
            }
        }
        return currentKeyFrame != null ? currentKeyFrame : keyFrames.get(0);

    }

    private KeyFrame getNextKeyFrame(BoneAnimation boneAnimation) {
        List<KeyFrame> keyFrames = boneAnimation.keyFrames();

        KeyFrame nextKeyFrame = null;
        for (int i = 0; i < keyFrames.size(); i++) {
            if (keyFrames.get(i).time() > currentTime) {
                nextKeyFrame = keyFrames.get(i);
                break;
            }
        }

        return nextKeyFrame != null ? nextKeyFrame : keyFrames.get(keyFrames.size() - 1);
    }

    public String name() {
        return name;
    }

    public void pause() {
        animating = false;
    }

    public void resume() {
        animating = true;
    }

    public void stop() {
        animating = false;
        currentTime = 0;
    }

    public boolean animating() {
        return animating;
    }

    public boolean loop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

}
