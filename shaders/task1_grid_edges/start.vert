#version 150
in vec2 inPosition; // input from the vertex buffer

uniform mat4 proj;
uniform mat4 view;

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

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getSphere(pos);

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
