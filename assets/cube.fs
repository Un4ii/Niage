#version 330 core
out vec4 FragColor;

struct Material {
    vec3 diffuseColor;
    vec3 specularColor;
    sampler2D diffuseTexture;
    sampler2D specularTexture;
    float shininess;
    bool useDiffuseTexture;
    bool useSpecularTexture;
};

struct Model {
    mat4 transform;
    Material material;
};

struct attribInfo {
    vec3 aPos;
    vec3 aNormal;
    vec2 aTexCoord;
};

uniform Model model;
in attribInfo aInfo;

void main() {

    vec3 diffuse = vec3(0.0);

    if (model.material.useDiffuseTexture) {
        diffuse = texture(model.material.diffuseTexture, aInfo.aTexCoord).rgb;
    } else {
        diffuse = model.material.diffuseColor;
    }

    FragColor = vec4(diffuse, 1.0);
}
