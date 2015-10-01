#version 330

in vec4 color;
in vec2 texCoord;
in vec3 normal;
in vec3 tangent;
in vec3 bitangent;
in vec4 eyePosition;

layout (location = 0) out vec4 outputColor;
layout (location = 1) out vec4 outputNormal;
layout (location = 2) out vec4 outputDepth;

uniform sampler2DArray diffuseTex;
uniform sampler2DArray normalTex;
uniform sampler2D splatTex[8];
uniform vec3 terrainSize;
uniform int tile;

uniform vec4 lightPosition;
uniform vec4 lAmbient;
uniform float shininess = 100, attenuation = 0.01;

void main(void) {
	int tiles = int(round(terrainSize.z));
	float weight[32];
	int splats = (tiles / 4) + ((tiles % 4) == 0 ? 0 : 1);
	
	for (int i = 0; i < splats; i++) {
		vec4 splat = texture2D(splatTex[i], texCoord / terrainSize.xy);
		weight[i * 4 + 0] = splat.r;
		weight[i * 4 + 1] = splat.g;
		weight[i * 4 + 2] = splat.b;
		weight[i * 4 + 3] = splat.a;
		}
	
	outputColor = vec4(0, 0, 0, 0);
	outputNormal = vec4(0, 0, 0, 0);
	for (int i = 0; i < terrainSize.z; i++)
		if (weight[i] > 0) {
			outputColor += texture(diffuseTex, vec3(texCoord, tiles - i - 1)) * weight[i];
			outputNormal += texture(normalTex, vec3(texCoord, tiles - i - 1)) * weight[i];
		}
	
	if (outputColor.a < 1.0f) {
		outputColor += vec4(1, 1, 1, 1) * (1.0f - outputColor.a);
		outputNormal += vec4(0.5f, 0.5f, 1.0f, 1.0f) * (1.0f - outputNormal.a);
	} else if (outputColor.a > 1.0f) {
		outputColor /= outputColor.a;
		outputNormal /= outputNormal.a;
	}
	
	vec3 N = normal;
    vec3 T = normalize(tangent - N * dot(tangent, N));
    vec3 B = normalize(bitangent - N * dot(tangent, N));
    	
    mat3 TBN = mat3(T, B, N);
    vec3 map = normalize(outputNormal.xyz * 2 - vec3(1,1,1));
    	
    N = normalize(TBN * map);
    
    vec3 P = vec3(eyePosition);
    vec3 L = vec3(0,0,1);
    if(lightPosition.w == 0.0f)
		L = normalize(lightPosition.xyz);
	else
		L = normalize(lightPosition.xyz - P);
	vec3 V = normalize(-P);
    vec3 H = normalize(L + V);
	
	vec4 lDiffuse = vec4(vec3(clamp(dot(N, L), 0.0f, 1.0f)), 1);
	vec4 lSpecular = vec4(vec3(clamp(pow(max(dot(N, H), 0), shininess) * attenuation, 0.0f, 1.0f)), 1);
    
    outputColor = clamp(outputColor * (max(lDiffuse, lAmbient)) + lSpecular, 0, 1);
    outputNormal = vec4((N / 2) + vec3(0.5f, 0.5f, 0.5f), 1.0f);
}