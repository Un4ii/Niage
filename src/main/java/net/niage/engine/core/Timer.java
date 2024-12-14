package net.niage.engine.core;

public class Timer {

    private double deltaTime;
    private double lastTime;

    private double frameRate = 60.0;
    private double frameInterval = 1.0 / frameRate;

    private final double tickRate = 20;
    private final double tickInterval = 1.0 / tickRate;

    private double lastUpdateTime = 0.0;
    private double lastRenderTime = 0.0;

    private int frameCount = 0;
    private double fpsTime = 0.0;
    private int fps = 0;

    private int tickCount = 0;
    private int tps = 0;

    public Timer() {
        this.lastTime = System.nanoTime() / 1_000_000_000.0;
    }

    public void update() {
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        updateMetrics();
    }

    public boolean shouldUpdate() {
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        if (currentTime - lastUpdateTime >= tickInterval) {
            lastUpdateTime = currentTime;
            tickCount++;
            return true;
        }
        return false;
    }

    public boolean shouldRender() {
        double currentTime = System.nanoTime() / 1_000_000_000.0;
        if (currentTime - lastRenderTime >= frameInterval) {
            lastRenderTime = currentTime;
            frameCount++;
            return true;
        }
        return false;
    }

    // Método que espera hasta el siguiente frame (Spinlock)
    public void waitForNextFrame() {
        double targetTime = System.nanoTime() / 1_000_000_000.0 + frameInterval;

        // Spinlock para esperar hasta el siguiente frame
        while (System.nanoTime() / 1_000_000_000.0 < targetTime) {
            // Hacer nada, solo esperar
        }
    }

    public double deltaTime() {
        return deltaTime;
    }

    public void updateMetrics() {
        fpsTime += deltaTime;
        if (fpsTime >= 1.0) {
            fps = frameCount;
            frameCount = 0;
            fpsTime -= 1.0;

            tps = tickCount;
            tickCount = 0;
        }
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
        this.frameInterval = 1.0 / frameRate;
    }

    public double frameRate() {
        return frameRate;
    }

    public int tps() {
        return tps;
    }

    public int fps() {
        return fps;
    }
}
