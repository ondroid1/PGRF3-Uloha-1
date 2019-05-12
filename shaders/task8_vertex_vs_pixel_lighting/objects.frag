#version 150
in vec3 vertColor;
in vec2 texCoord;
in vec3 vertNormal;

out vec4 outColor; // output

uniform sampler2D texture;
uniform int objectMode;
uniform int visualisationMode;
uniform int moveMode;

void main() {
	vec4 BaseColor = vec4(vertColor, 1.0);
	
	if(visualisationMode == 1) BaseColor = vec4(normalize(vertNormal) * 0.5 + 0.5, 1.0);
	else if(visualisationMode == 2) BaseColor = texture2D(texture, texCoord);
	
	outColor = BaseColor;
}