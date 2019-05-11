#version 150
in vec3 vertColor;
in vec3 normal;
out vec4 outColor;

void main() {
	outColor = vec4(normalize(normal), 1.0);
}
