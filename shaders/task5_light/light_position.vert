#version 150
in vec2 inPosition; // input from the vertex buffer

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// zdroj svetla
vec3 getLightSource(vec2 xy) {
    float az = xy.x * PI;
    float ze = xy.y * PI / 2;
    float r = 1;

    float x = cos(az) * cos(ze) * r;
    float y = 2 * sin(az) * cos(ze) * r;
    float z = 0.5 * sin(ze) * r;
    return vec3(x, y, z) / 8;
}

void main() {
    vec2 pos = inPosition * 2 - 5;
    vec3 finalPos = getLightSource(pos);
    gl_Position = proj * view * vec4(finalPos, 1.0);
}
