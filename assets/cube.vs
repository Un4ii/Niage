#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

layout (std140) uniform PerspectiveCamera {
    mat4 perspectiveProjection;
    mat4 cameraView;
    vec3 cameraPosition;
    float padding1;
};

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

uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = perspectiveProjection * cameraView * model.transform * vec4(aPos, 1.0);
}