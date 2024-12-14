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
out attribInfo aInfo;

void main() {
    aInfo.aPos = aPos;
    aInfo.aNormal = aNormal;
    aInfo.aTexCoord = aTexCoord;
    gl_Position = perspectiveProjection * cameraView * model.transform * vec4(aPos, 1.0);
}