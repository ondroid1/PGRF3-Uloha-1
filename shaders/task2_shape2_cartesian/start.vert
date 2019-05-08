#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// ohnutí gridu do jiného tvaru
vec3 getConus(vec2 xy) {
	float ze = xy.y * PI / 2;

	float x = xy.x * 2;
	float y = xy.y;
	float z = ze;

	return vec3(x, y, z);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getConus(pos);
	vertColor = finalPos / 4;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
