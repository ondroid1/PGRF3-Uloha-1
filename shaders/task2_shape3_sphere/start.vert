#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// vrací souřadnice pro objekt SpaceStation ze sférických souřadnic
// http://www.math.uri.edu/~bkaskosz/flashmo/tools/sphplot/
vec3 getSpaceStation(vec2 paramPos) {
	float s = 2 * PI * paramPos.x;
	float t = PI * paramPos.y ;
	float rho = 1 + 0.5 * sin(4 * t);

	float x = cos(s) * sin(t) * rho;
	float y = sin(s) * sin(t) * rho;
	float z = cos(t) * rho;

	return vec3(x,	y,	z) * 4;
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getSpaceStation(pos);
	vertColor = finalPos;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
