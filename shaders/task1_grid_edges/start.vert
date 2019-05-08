#version 150
in vec2 inPosition; // input from the vertex buffer

uniform mat4 proj;
uniform mat4 view;

void main() {
	gl_Position = proj * view * vec4(inPosition, 0.0, 1.0);
}
