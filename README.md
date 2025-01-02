# UBOs

0. Perspective Camera -- "PerspectiveCamera"

    - mat4 projection
    - mat4 view
    - vec3 pos

1. Orthographic Camera -- "OrthographicCamera"
    - mat4 projection
    - mat4 view
    - vec3 pos

# Material struct

-   vec3 diffuseColor
-   vec3 specularColor
-   sampler2D diffuseTexture
-   sampler2D specularTexture
-   float shininess
-   bool useDiffuseTexture
-   bool useSpecularTexture

# Model struct

-   mat4 transform
-   Material material
