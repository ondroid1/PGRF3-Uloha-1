#version 150
in vec3 vertColor;
in vec2 texCoord;
in vec3 normal;
in vec3 lightDirection;
in vec3 viewDirection;
in float intensity; // per vertex diffuse
in float distance;

out vec4 outColor; // output

uniform sampler2D texture;
uniform int objectMode;
uniform int visualisationMode;
uniform int moveMode;
uniform int specularMode;
uniform int reflectorMode;
uniform int shaderMode;

const vec4 Diffuse = vec4(0.7, 0.7, 0.7, 1.0);
const vec4 Specular = vec4(0.6, 0.6, 0.6, 1.0);
const vec4 Ambient = vec4(0.1, 0.1, 0.1, 1.0);

const float constantAttenuation = 0.155;
const float linearAttenuation = 0.1;
const float quadraticAttenuation = 0.0007;
const float spotCutOff = 0.99;
const vec3 spotVec = vec3(0.0, 0.0, -1.0);

void main() {
	vec3 nd = normalize(normal);
	vec3 ld = normalize(lightDirection);
	vec3 vd = normalize(viewDirection);
	
	vec4 BaseColor = vec4(vertColor, 1.0);
	if(visualisationMode == 1) BaseColor = vec4(normalize(normal) * 0.5 + 0.5, 1.0);
	else if(visualisationMode == 2) BaseColor = texture2D(texture, texCoord);
	
	float SpecularPower = 35;
	if(specularMode == 1) SpecularPower = 20;
	else if(specularMode == 2) SpecularPower = 13;
	else if(specularMode == 3) SpecularPower = 6;
	
	float NDotL = max(dot( nd, ld), 0.0 );
	
	vec3 reflection = normalize( ( ( 2.0 * nd) * NDotL) - ld);
	float RDotV = max( 0.0, dot( reflection, vd) );
	
	vec3 halfVector = normalize(ld + vd);
	float NDotH = max( 0.0, dot( nd, halfVector) );
	
	vec4 totalAmbient = Ambient * BaseColor;
	vec4 totalDiffuse;
	
	if(shaderMode == 0) totalDiffuse = Diffuse * NDotL * BaseColor;
	else totalDiffuse = Diffuse * intensity * BaseColor;
	 
	vec4 totalSpecular = Specular * ( pow( NDotH, SpecularPower ) );
    
	float att = 1.0 / (constantAttenuation + linearAttenuation * distance + quadraticAttenuation * distance * distance); 
	
	if(reflectorMode == 1) {
		float spotEffect = dot(normalize(spotVec), normalize(-vd));
		if (spotEffect > spotCutOff) {
			outColor = vec4(totalAmbient.xyz + att * (totalDiffuse.xyz + totalSpecular.xyz), 1.0);
		} else {
			outColor = totalAmbient;
		}
	}
	else outColor = vec4(totalAmbient.xyz + att * (totalDiffuse.xyz + totalSpecular.xyz), 1.0);
}