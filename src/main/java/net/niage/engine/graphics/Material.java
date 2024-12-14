package net.niage.engine.graphics;

import org.joml.Vector3f;

import net.niage.engine.texture.Texture2D;

public class Material {

    private Vector3f diffuseColor;
    private Vector3f specularColor;
    private Texture2D diffuseTexture;
    private Texture2D specularTexture;
    private float shininess;
    private final boolean useDiffuseTexture;
    private final boolean useSpecularTexture;

    public Material(Vector3f diffuseColor, Vector3f specularColor, float shininess) {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.shininess = shininess;
        this.diffuseTexture = new Texture2D();
        this.specularTexture = new Texture2D();
        this.useDiffuseTexture = false;
        this.useSpecularTexture = false;
    }

    public Material(Texture2D diffuseTexture, Texture2D specularTexture, float shininess) {
        this.diffuseColor = new Vector3f(0.0f);
        this.specularColor = new Vector3f(0.0f);
        this.shininess = shininess;
        this.diffuseTexture = diffuseTexture;
        this.specularTexture = specularTexture;
        this.useDiffuseTexture = true;
        this.useSpecularTexture = true;
    }

    public Material(Vector3f diffuseColor, Vector3f specularColor, float shininess, Texture2D diffuseTexture,
            Texture2D specularTexture, boolean useDiffuseTexture, boolean useSpecularTexture) {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.shininess = shininess;
        this.diffuseTexture = diffuseTexture;
        this.specularTexture = specularTexture;
        this.useDiffuseTexture = useDiffuseTexture;
        this.useSpecularTexture = useSpecularTexture;
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

    public boolean useDiffuseTexture() {
        return useDiffuseTexture;
    }

    public boolean useSpecularTexture() {
        return useSpecularTexture;
    }

}
