#version 150
in vec3 vertColor; // vstup z proběhlé části pipeline (vertex shaderu)
out vec4 outColor; // výstup z  fragment shaderu

void main() {
	outColor = vec4(vertColor, 1.0);
} 
