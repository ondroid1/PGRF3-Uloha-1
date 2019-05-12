#version 150
in vec2 inParamPos;

out vec3 vertColor;
out vec2 texCoord;
out vec3 normal;
out vec3 lightDirection;
out vec3 viewDirection;
out float intensity;
out float distance;

uniform mat4 mat;
uniform mat4 modelViewMat;
uniform float time;
uniform int objectMode;
uniform int visualisationMode;
uniform int moveMode;
uniform int specularMode;
uniform int shaderMode;

const float PI = 3.14;

// ohnutí gridu do podoby elipsoidu
vec3 getSphere(vec2 xy) {
	float az = xy.x * PI;
	float ze = xy.y * PI / 2; // máme od -1 do 1 a chceme od -PI/2 do PI/2
	float r = 1;

	float x = cos(az) * cos(ze) *r;
	float y = 2*sin(az) * cos(ze) *r;
	float z = 0.5 * sin(ze) *r;

	return vec3(x, y, z);
}


vec3 normalDer(vec2 paramPos) {
	float delta = 0.001;
	vec2 dx = vec2(delta, 0), dy = vec2(0, delta);
	vec3 tx = getSphere(paramPos + dx) - getSphere(paramPos - dx);
	vec3 ty = getSphere(paramPos + dy) - getSphere(paramPos - dy);
	return cross(tx, ty);
}

vec3 normalDiff (vec2 uv) {
	float delta = 0.001;
	vec3 dzdu = (getSphere(uv + vec2(delta,0)) - getSphere(uv - vec2(delta,0)))/2.0/delta;
	vec3 dzdv = (getSphere(uv + vec2(0,delta)) - getSphere(uv - vec2(0,delta)))/2.0/delta;
	return cross(dzdu,dzdv);
}

void main() {
	vec3 position = getSphere(inParamPos);

//	if(shaderMode == 0) normal = normalDer(inParamPos);
//	else normal = normalize(normalDer(inParamPos));
//	normal = normalDer(inParamPos);

	vec4 objectPosition = modelViewMat * vec4(position, 1.0);

	vec3 lightPosition = vec3(-10.0, -5.0, -10.0);

	lightDirection = lightPosition - objectPosition.xyz;

	distance = length(lightDirection);

	viewDirection = - objectPosition.xyz;

	intensity = max(dot(normalize(lightDirection), normalize(normal)), 0.0);

	vertColor = vec3(inParamPos, 0.0);

	texCoord = inParamPos * 4;

	gl_Position = mat * vec4(position, 1.0);
} 