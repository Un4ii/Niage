package net.niage.engine.graphics;

import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINodeAnim;

public class Animation {

    private AIAnimation aiAnimation;
    private double currentTime = 0f;

    public Animation(AIAnimation aiAnimation) {
        this.aiAnimation = aiAnimation;
    }

    public String name() {
        return aiAnimation.mName().dataString();
    }

    public void update(double deltaTime) {
        currentTime += deltaTime;

        if (currentTime > aiAnimation.mDuration()) {
            currentTime = 0f;
        }

        for (int i = 0; i < aiAnimation.mNumChannels(); i++) {
            AINodeAnim channel = AINodeAnim.create(aiAnimation.mChannels().get(i));
            // Interpolate pos
        }
    }

}