#version 330 core
out vec4 FragColor;

struct Material {
    vec3 diffuseColor;
    vec3 specularColor;
    sampler2D diffuseTexture;
    sampler2D specularTexture;
    float shininess;
    bool useTextures;
};

struct Model {
    mat4 transform;
    Material material;
};

uniform Model model;

void main() {
    FragColor = vec4(vec3(1.0), 1.0);
}
