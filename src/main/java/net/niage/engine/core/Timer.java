package net.niage.engine.core;

public class Timer {

    private double deltaTime;
    private double lastTime;

    private double frameRate = -1;
    private double frameInterval = 1.0 / frameRate;

    private double lastUpdateTime = 0.0;
    private double lastRenderTime = 0.0;

    public Timer() {
        this.lastTime = System.nanoTime() / 1_000_000_000.0;
    }

    public void update() {
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
    }

    public boolean shouldUpdate() {
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        if (currentTime - lastUpdateTime >= frameInterval) {
            lastUpdateTime = currentTime;
            return true;
        }
        return false;
    }

    public boolean shouldRender() {
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        if (currentTime - lastRenderTime >= frameInterval) {
            lastRenderTime = currentTime;
            return true;
        }
        return false;
    }

    public void waitForNextFrame() {
        double targetTime = System.nanoTime() / 1_000_000_000.0 + frameInterval;

        // Spinlock to wait until the target time is reached
        while (System.nanoTime() / 1_000_000_000.0 < targetTime) {
            // Do nothing, just wait for the next frame
        }
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
        this.frameInterval = 1.0 / frameRate;
    }

    public double getFrameRate() {
        return frameRate;
    }
}