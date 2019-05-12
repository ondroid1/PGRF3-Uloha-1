#version 150
in vec3 vertColor;
in vec2 texCoord;
in vec3 vertNormal;
in vec3 g_lightVec;
in vec3 g_viewVec;

out vec4 outColor; // output

uniform sampler2D texture;
uniform sampler2D normalTexture;
uniform sampler2D heightTexture;
uniform sampler2D redTexture;
uniform sampler2D redNormalTexture;
uniform sampler2D redHeightTexture;
uniform int objectMode;
uniform int mappingMode;
uniform int textureMode;

const float scaleL = 0.04;
const float scaleK = -0.02;
const float SpecularPower = 10;
const vec4 Diffuse = vec4(0.7, 0.7, 0.7, 1.0);
const vec4 Specular = vec4(0.9, 0.9, 0.9, 1.0);
const vec4 Ambient = vec4(0.1, 0.1, 0.1, 1.0);
const float constantAttenuation = 0.155;
const float linearAttenuation = 0.1;
const float quadraticAttenuation = 0.0007;

void main() {
	vec3 nd;
	vec3 vd = normalize(g_viewVec);
	vec3 ld = normalize(g_lightVec);
	vec4 BaseColor;
	
	if(textureMode == 0) {
	
		// School's bricks texture
		BaseColor = texture2D(texture, texCoord);
		if(mappingMode == 0) {
		
			// Normal mapping
			nd = texture2D(normalTexture, texCoord.xy).rgb * 2.0 - 1.0;
		} else if(mappingMode == 1) {
		
			// Parallax mapping
			float height = texture2D(heightTexture, texCoord).r;
			vec2 offset = vd.xy * (height * scaleL + scaleK);
			BaseColor = texture2D(texture, texCoord + offset);
			nd = texture2D(normalTexture, texCoord.xy + offset).rgb * 2.0 - 1.0;
		}
	} else {
	
		// Red bricks texture
		BaseColor = texture2D(redTexture, texCoord);
		if(mappingMode == 0) {
		
			// Normal mapping
			nd = texture2D(redNormalTexture, texCoord.xy).rgb * 2.0 - 1.0;
		} else if(mappingMode == 1) {
		
			// Parallax mapping
			float height = texture2D(redHeightTexture, texCoord).r;
			vec2 offset = vd.xy * (height * scaleL + scaleK);
			BaseColor = texture2D(redTexture, texCoord + offset);
			nd = texture2D(redNormalTexture, texCoord.xy + offset).rgb * 2.0 - 1.0;
		}
	}
	
	float NDotL = max(dot(nd, ld), 0.0);
	vec3 reflection = normalize((( 2.0 * nd ) * NDotL) - ld); 
    float RDotV = max(0.0, dot(reflection, vd));
    vec3 halfVector = normalize(ld + vd);
    float NDotH = max(0.0, dot(nd, halfVector));
    
	vec4 totalDiffuse = NDotL * Diffuse * BaseColor;
    vec4 totalAmbient = BaseColor * Ambient;
    vec4 totalSpecular = Specular * ( pow( NDotH, SpecularPower * 4.0 ) );
    
	outColor = totalAmbient + totalDiffuse + totalSpecular;
}