#version 150
in vec2 inPosition;

uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPosition;
uniform float time;
uniform int mode;
uniform mat4 lightVP;

out vec4 depthTexCoord;
out vec2 texCoord;
out vec3 normal;
out vec3 light;
out vec3 viewDirection;
out vec3 NdotL;

const float PI = 3.14;

// ohnutí gridu do podoby koule
vec3 getSphere(vec2 paramPos) {
    float az = paramPos.x * 2 * PI;
    float ze = paramPos.y * PI;
    float r = 1;

    return vec3(
        r * sin(ze) * cos(az) * 2,
        r * sin(ze) * sin(az) * 2,
        r * cos(ze) * 2
    );
}

vec3 getSphereNormal(vec2 xy) {
    vec3 u = getSphere(xy + vec2(0.001, 0)) - getSphere(xy - vec2(0.001, 0));
    vec3 v = getSphere(xy + vec2(0, 0.001)) - getSphere(xy - vec2(0, 0.001));
    return cross(u, v);
}

void main() {
    vec2 pos = inPosition * 2 - 1;
    vec3 finalPos;

    finalPos = getSphere(pos);
    gl_Position = projection * view * vec4(finalPos, 1.0);

    normal = getSphereNormal(pos);
    light = lightPosition - finalPos;
    NdotL = vec3(dot(normal, light));

    // získání pozice kamery z view matice
    // (kamera je pohled třetí osoby a tudíž její pozice je v počátku - proto nutné použít view matici)
    mat4 invView = inverse(view);
    vec3 eyePosition = vec3(invView[3][0], invView[3][1], invView[3][2]);

    viewDirection = eyePosition - finalPos;

    depthTexCoord = lightVP * vec4(finalPos, 1.0);
    depthTexCoord.xyz = (depthTexCoord.xyz + 1) / 2; // obrazovka má rozsahy <-1;1>
}
