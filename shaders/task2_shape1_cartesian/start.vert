#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// ohnutí gridu do kónického tvaru
vec3 getConus(vec2 xy) {
	float az = xy.x * PI;
	float ze = xy.y * 4 * PI; // máme od -1 do 1 a chceme od 0 do 2PI            -PI/2 do PI/2

	float x = ze * cos(az);
	float y = ze * sin(az);
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
