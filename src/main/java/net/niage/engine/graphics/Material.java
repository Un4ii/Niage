package net.niage.engine.graphics;

import org.joml.Vector3f;

import net.niage.engine.texture.Texture2D;

public class Material {

    private Vector3f diffuseColor;
    private Vector3f specularColor;
    private Texture2D diffuseTexture;
    private Texture2D specularTexture;
    private float shininess;
    private final boolean useTextures;

    public Material(Vector3f diffuseColor, Vector3f specularColor, float shininess) {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.shininess = shininess;
        this.diffuseTexture = new Texture2D();
        this.specularTexture = new Texture2D();
        this.useTextures = false;
    }

    public Material(Texture2D diffuseTexture, Texture2D specularTexture, float shininess) {
        this.diffuseColor = new Vector3f(0.0f);
        this.specularColor = new Vector3f(0.0f);
        this.shininess = shininess;
        this.diffuseTexture = diffuseTexture;
        this.specularTexture = specularTexture;
        this.useTextures = true;
    }

    public Vector3f diffuseColor() {
        return diffuseColor;
    }

    public Vector3f specularColor() {
        return specularColor;
    }

    public Texture2D diffuseTexture() {
        return diffuseTexture;
    }

    public Texture2D specularTexture() {
        return specularTexture;
    }

    public float shininess() {
        return shininess;
    }

    public boolean useTextures() {
        return useTextures;
    }

}
