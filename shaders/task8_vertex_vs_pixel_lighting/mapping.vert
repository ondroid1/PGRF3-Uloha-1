#version 150
in vec2 inParamPos; // input position

out vec3 vertColor;
out vec2 texCoord;
out vec3 vertNormal;
out vec3 g_lightVec;
out vec3 g_viewVec;

uniform mat4 mat;
uniform mat4 modelViewMat;
uniform float time;
uniform int objectMode;
uniform int textureMulMode;
uniform int moveMode;
uniform int mappingMode;

const vec3 lightPos = vec3(-10, 10, -10);
const float PI = 3.1415927;

vec3 grid(vec2 paramPos) {
	float p = 0;
	if(moveMode == 1) p = cos(paramPos.x * PI + time * 3);
	return vec3(
		paramPos.x * 2,
		paramPos.y * 2,
		p
	);
}

vec3 ball(vec2 paramPos) {
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

vec3 surface(vec2 paramPos) {
	if(objectMode == 0) return grid(paramPos);
	else if (objectMode == 1) return ball(paramPos);
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
	
	vec4 objectPosition = modelViewMat * vec4(position, 1.0);
	
	vec3 lightPosition = vec3(-10.0, -5.0, -10.0);
	vec3 lightDirection = normalize(lightPosition - objectPosition.xyz);
	
	vec3 viewDirection = normalize(- objectPosition.xyz);
	
	vertColor = vec3(inParamPos, 0.0);	

	texCoord = inParamPos * textureMulMode;
	
	vec3 aux;
	
	vec3 p1 = vec3(position.x + 0.5, position.yz);
	aux = surface(p1.xy);
	p1.z = aux.z;
	
	vec3 p2 = vec3(position.x - 0.5, position.yz);
	aux = surface(p2.xy);
	p2.z = aux.z;
	
	vec3 t = (p1 - p2);
	
	p1 = vec3(position.x, position.y + 0.5, position.z);
	aux = surface(p1.xy);
	p1.z = aux.z;
	
	p2 = vec3(position.x, position.y - 0.5, position.z);
	aux = surface(p2.xy);
	p2.z = aux.z;

	vec3 b = (p1 - p2);
	
	mat3 normalMat = inverse(transpose(mat3(modelViewMat)));
	
	vec3 tangent = normalMat * normalize(t);
	vec3 binormal = normalMat * normalize(b);
	vec3 normal = cross(binormal, tangent);
	
	mat3 TBN = mat3(tangent, binormal, normal);
	
	g_viewVec = viewDirection * TBN;
	g_lightVec = lightDirection * TBN;
} 