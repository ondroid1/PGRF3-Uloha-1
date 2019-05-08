#version 150
in vec2 inPosition; // input from the vertex buffer

uniform mat4 viewLight;
uniform mat4 projLight;
uniform int mode;

const float PI = 3.1415;

// ohnutí gridu do podoby elipsoidu
vec3 getSphere(vec2 xy) {
    float az = xy.x * PI;
    float ze = xy.y * PI/2; // máme od -1 do 1 a chceme od -PI/2 do PI/2
    float r = 1;

    float x = cos(az)*cos(ze)*r;
    float y = 2*sin(az)*cos(ze)*r;
    float z = 0.5*sin(ze)*r;
    return vec3(x, y, z);
}

void main() {
    vec2 pos = inPosition * 2 - 1; // máme od 0 do 1 a chceme od -1 do 1 (funkce pro ohyb gridu s tím počítají)
    vec3 finalPos;
    if (mode == 0) { // mode 0 je stínící plocha
        finalPos = vec3(pos, 1.0); // posuneme po ose "z" o 1
    } else { // mode 1 je koule
        finalPos = getSphere(pos);
    }
	gl_Position = projLight * viewLight * vec4(finalPos, 1.0);
} 
