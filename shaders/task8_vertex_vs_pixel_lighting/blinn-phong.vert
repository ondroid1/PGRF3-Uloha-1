#version 150
in vec2 inParamPos; // input position

out vec3 vertColor;
out vec2 texCoord;
out vec3 normal;
out vec3 lightDirection;
out vec3 viewDirection;
out float intensity; // per vertex diffuse
out float distance;

uniform mat4 mat;
uniform mat4 modelViewMat;
uniform float time;
uniform int objectMode;
uniform int visualisationMode;
uniform int moveMode;
uniform int specularMode;
uniform int shaderMode;

const float PI = 3.1415927;

vec3 snake(vec2 paramPos) {
	float t = paramPos.x * 2 * PI;
	float s = paramPos.y;
	float offset = 1;
	if(moveMode == 1) offset = cos(time * 3);
	return vec3(
		(1-s)*(3+cos(t))*cos(2*PI*s) * 0.5,
		(1-s)*(3+cos(t))*sin(2*PI*s) * 0.5 * offset,
		6*s+(1-s)*sin(t) * 0.5 - 1
	);
}

vec3 ball(vec2 paramPos) {
	float azimuth;
	
	if(moveMode == 0) azimuth = paramPos.x * 2 * PI;
	else azimuth = time * 3 + paramPos.x * 2 * PI;
	
	float zenith = paramPos.y * PI;
	float r = 1;
	return vec3(
		r * sin(zenith) * cos(azimuth) * 2,
		r * sin(zenith) * sin(azimuth) * 2,
		r * cos(zenith) * 2
	);
}


vec3 doodle(vec2 paramPos) {
	float azimuth;
	
	if(moveMode == 0) azimuth = paramPos.x * 2 * PI;
	else azimuth = time * 3 + paramPos.x * 2 * PI ;

	float zenith = paramPos.y * PI;
	float r = cos(sin(zenith * zenith));
	return vec3(
		r * sin(zenith) * cos(azimuth) * 2,
		r * sin(zenith) * sin(azimuth) * 2,
		r * cos(zenith) * 2
	);
}

vec3 surface(vec2 paramPos) {
	if(objectMode == 0) return ball(paramPos);
	else if (objectMode == 1) return snake(paramPos);
	else if (objectMode == 2) return doodle(paramPos);	
}

vec3 normalDer(vec2 paramPos) {
	float delta = 0.001;
	vec2 dx = vec2(delta, 0), dy = vec2(0, delta);
	vec3 tx = surface(paramPos + dx) - surface(paramPos - dx);
	vec3 ty = surface(paramPos + dy) - surface(paramPos - dy);
	return cross(tx, ty);
}

vec3 normalDiff (vec2 uv) {
   float delta = 0.001;
   vec3 dzdu = (surface(uv + vec2(delta,0)) - surface(uv - vec2(delta,0)))/2.0/delta;
   vec3 dzdv = (surface(uv + vec2(0,delta)) - surface(uv - vec2(0,delta)))/2.0/delta;
   return cross(dzdu,dzdv);
} 

void main() {
	vec3 position = surface(inParamPos);
	
	if(shaderMode == 0) normal = normalDer(inParamPos);
	else normal = normalize(normalDer(inParamPos));
	
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