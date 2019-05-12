#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 normal;
out vec3 vertColor;

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// ohnutí gridu do kónického tvaru
vec3 getConus(vec2 xy) {
	float az = xy.x * PI;
	float ze = xy.y * 2 * PI;

	float x = ze * cos(az);
	float y = ze * sin(az);
	float z = ze;

	return vec3(x, y, z) / 2;
}

vec3 getNormal(vec2 xy) {
	vec3 u = getConus(xy + vec2(0.001, 0)) - getConus(xy - vec2(0.001, 0));
	vec3 v = getConus(xy + vec2(0, 0.001)) - getConus(xy - vec2(0, 0.001));
	return cross(u, v);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getConus(pos);
	normal = getNormal(pos);

	vertColor = normal;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
