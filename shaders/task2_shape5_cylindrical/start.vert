#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// vrací souřadnice pro objekt Juicer podle tvořený cylindrických souřadnic
// http://www.math.uri.edu/~bkaskosz/flashmo/tools/cylin/
vec3 getJuicer(vec2 paramPos) {
	float s = 2 * PI * paramPos.x;
	float t = 2 * PI * paramPos.y;

	float r = t; //(1 + max(sin(t), 0)) * 0.1 * t;
	float theta = s;

	float x = r * cos(theta);
	float y = r * sin(theta);
	float z = cos(t); //(3 - t) * 0.2;

	return vec3(x,	y,	z);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getJuicer(pos);
	vertColor = finalPos;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
