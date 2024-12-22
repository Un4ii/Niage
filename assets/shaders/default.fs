#version 330 core
out vec4 FragColor;

layout (std140) uniform Lighting {
    vec3 sunDirection;
    float shadowStrenght;
    vec3 sunColor;
};

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
    vec3 fragPos;
};

uniform Model model;
in attribInfo aInfo;

void main() {

    // Diffuse color
    vec3 diffuseColor = vec3(0.0);
    if (model.material.useDiffuseTexture) {
        diffuseColor = texture(model.material.diffuseTexture, aInfo.aTexCoord).rgb;
    } else {
        diffuseColor = model.material.diffuseColor;
    }

    FragColor = vec4(diffuseColor, 1.0);
}
