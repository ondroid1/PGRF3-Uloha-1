#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 vertColor; // výstupní barva

uniform mat4 proj;
uniform mat4 view;

const float PI = 3.14;

// vrací souřadnice pro objekt Juicer podle tvořený cylindrických souřadnic
// http://www.math.uri.edu/~bkaskosz/flashmo/tools/cylin/
vec3 getJuicer2(vec2 paramPos) {
	float s = 2 * PI * paramPos.x;
	float t = 2 * PI * paramPos.y;

	float r = t;
	float theta = s;

	float x = s * sin(theta);
	float y = r * sin(theta);
	float z = t * 0.5;

	return vec3(x,	y,	z);
}

void main() {
	vec2 pos = inPosition * 2 - 1;
	vec3 finalPos;

	finalPos = getJuicer2(pos);
	vertColor = finalPos;

	gl_Position = proj * view * vec4(finalPos, 1.0);
}
