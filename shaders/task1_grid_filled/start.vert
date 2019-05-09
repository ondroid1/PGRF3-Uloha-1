#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// ohnutí gridu do podoby elipsoidu
vec3 getSphere(vec2 xy) {
	float az = xy.x * PI;
	float ze = xy.y * PI/2; // máme od -1 do 1 a chceme od -PI/2 do PI/2
	float r = 2;

	float x = cos(az)*cos(ze)*r;
	float y = 2*sin(az)*cos(ze)*r;
	float z = 0.5*sin(ze)*r;
	return vec3(x, y, z);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getSphere(pos);

	vertColor = finalPos;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
