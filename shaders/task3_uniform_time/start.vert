#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;
uniform float time;

const float PI = 3.14;

// ohnutí gridu do podoby elipsoidu
vec3 getSphere(vec2 xy) {
	float az = xy.x * PI;
	float ze = xy.y * PI / 2; // máme od -1 do 1 a chceme od -PI/2 do PI/2
	float r = 1;

	float x = cos(az) * cos(ze) * r;
	float y = 2*sin(az) * cos(ze) * r;
	float z = 0.5 * sin(ze) * r;

	return vec3(x, y, z);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos = getSphere(pos);

    vertColor = finalPos;

	finalPos.x += 0.1;
	finalPos.y += cos(finalPos.x + time);

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
