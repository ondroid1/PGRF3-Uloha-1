#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// objekt ve s
vec3 getSpheric(vec2 paramPos) {
	float azimuth;

	azimuth = paramPos.x * 2 * PI;

	float zenith = paramPos.y * PI;
	float r = 1;

	return vec3(
		r * sin(zenith) * cos(azimuth),
		r * sin(zenith) * sin(azimuth),
		r * cos(zenith)
	);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getSpheric(pos);
	vertColor = finalPos;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
