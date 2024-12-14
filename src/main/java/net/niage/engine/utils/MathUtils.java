package net.niage.engine.utils;

public class MathUtils {
    public static float clampf(float value, float min, float max) {
        value = value > max ? max : value;
        value = value < min ? min : value;

        return value;
    }

    public static int clampi(int value, int min, int max) {
        value = value > max ? max : value;
        value = value < min ? min : value;

        return value;
    }

    public static double clampd(double value, double min, double max) {
        value = value > max ? max : value;
        value = value < min ? min : value;

        return value;
    }

    public static float wrapf(float value, float min, float max) {
        value = value > max ? min : value;
        value = value < min ? max : value;

        return value;
    }

    public static int wrapi(int value, int min, int max) {
        value = value > max ? min : value;
        value = value < min ? max : value;

        return value;
    }

    public static double wrapd(double value, double min, double max) {
        value = value > max ? min : value;
        value = value < min ? max : value;

        return value;
    }
}
