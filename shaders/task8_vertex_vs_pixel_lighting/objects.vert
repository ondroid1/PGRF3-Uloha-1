#version 150
in vec2 inParamPos; // input position

out vec3 vertColor;
out vec2 texCoord;
out vec3 vertNormal;

uniform mat4 mat;
uniform float time;
uniform int objectMode;
uniform int visualisationMode;
uniform int moveMode;

const float PI = 3.1415927;

vec3 cartesian_1(vec2 paramPos) {
	float x;
	
	if(moveMode == 0) x = (paramPos.x * 2) - 1;
	else x = cos(time * 3) + (paramPos.x * 2) - 1;
	
	float y = (paramPos.y * 2) - 1;
	float p = sqrt( 20 * pow(x, 2) + 20 * pow(y, 2) );
	return vec3(
		x,
		y,
		0.5 * cos(p) 
	);
}


vec3 cartesian_2(vec2 paramPos) {
	float p;
	
	if(moveMode == 0) p = paramPos.x * 2 * PI * 3;
	else p = time * 3 + paramPos.x * 2 * PI * 3;
	
	return vec3(
		paramPos.x * 2 - 1,
		0.25 * cos(p),
		paramPos.y * 2 - 1
	);
}


vec3 cylindrical_1(vec2 paramPos) {
	float s;
	
	if(moveMode == 0) s = paramPos.x * 2 * PI;
	else s = time * 3 + paramPos.x * 2 * PI;
	
	float t = paramPos.y * 2 * PI;
	
	float r = (1 + max(sin(t), 0)) * 0.1 * t;
	float theta = s;
	
	return vec3(
		r * cos(theta),
		r * sin(theta),
		(3 - t) * 0.2
	);
}


vec3 cylindrical_2(vec2 paramPos) {
	float s;
	float t =	(paramPos.y * 2) - 1;
	
	if(moveMode == 0) s = paramPos.x * 4 * PI;
	else s = time * 3 + paramPos.x * 4 * PI;
	
	float r = 0.75 * paramPos.x;
	float theta = s;
	
	return vec3(
		r * cos(theta),
		r * sin(theta),
		t
	);
}


vec3 spheric_1(vec2 paramPos) {
	float azimuth;
	
	if(moveMode == 0) azimuth = paramPos.x * 2 * PI;
	else azimuth = time * 3 + paramPos.x * 2 * PI;
	
	float zenith = paramPos.y * PI;
	float r = 1;
	return vec3(
		r * sin(zenith) * cos(azimuth),
		r * sin(zenith) * sin(azimuth),
		r * cos(zenith)
	);
}


vec3 spheric_2(vec2 paramPos) {
	float azimuth;
	
	if(moveMode == 0) azimuth = paramPos.x * 2 * PI;
	else azimuth = time * 3 + paramPos.x * 2 * PI ;

	float zenith = paramPos.y * PI;
	float r = cos(sin(zenith * zenith));
	return vec3(
		r * sin(zenith) * cos(azimuth),
		r * sin(zenith) * sin(azimuth),
		r * cos(zenith)
	);
}


vec3 surface(vec2 paramPos) {
	if(objectMode == 0) return cartesian_1(paramPos);
	else if (objectMode == 1) return cartesian_2(paramPos);
	else if (objectMode == 2) return cylindrical_1(paramPos);
	else if (objectMode == 3) return cylindrical_2(paramPos);
	else if (objectMode == 4) return spheric_1(paramPos);
	else return spheric_2(paramPos);	
}


vec3 normal(vec2 uv) {
	float delta = 0.001;
	vec3 dzdu = (surface(uv+vec2(delta,0))-surface(uv-vec2(delta,0)))/2.0/delta;
	vec3 dzdv = (surface(uv+vec2(0,delta))-surface(uv-vec2(0,delta)))/2.0/delta;
	return cross(dzdu,dzdv);
}


void main() {
	vec3 position = surface(inParamPos);
	
	gl_Position = mat * vec4(position, 1.0);
	
	vertNormal = normal(inParamPos);
	
	vertColor = vec3(inParamPos, 0.0);	

	texCoord = inParamPos * 2;
} 