#version 130 

in vec4 color;
in vec2 texCoord;
in vec3 normal;
in vec3 tangent;
in vec3 bitangent;
in vec4 eyePosition;

out vec4 outputColor;
out vec4 outputNormal;

uniform sampler2D diffuseTex;
uniform sampler2D normalTex;

uniform vec4 lightPosition;
uniform vec4 lAmbient;
uniform float shininess = 100, attenuation = 0.7;

void main(void) {
	vec2 texSize;
	vec3 N;
	texSize = textureSize(diffuseTex, 0);
	vec4 textureColor;
	if(length(texSize) < 2) {
		textureColor = vec4(1,1,1,1) * color;
	} else {
		textureColor = texture2D(diffuseTex, texCoord) * color;
	}
	
	N = normal;
	texSize = textureSize(normalTex, 0);
	if(length(texSize) >= 2) {
    	vec3 T = tangent;
    	vec3 B = bitangent;
    
    	mat3 TBN = mat3(T, B, N);
    	vec3 map = normalize(texture(normalTex, texCoord).xyz * 2 - vec3(1,1,1));
    
    	N = normalize(TBN * map);
    }
    
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
    
    outputColor = clamp(textureColor * (max(lDiffuse, lAmbient)) + lSpecular, 0, 1);
    outputNormal = vec4((N / 2) + vec3(0.5f, 0.5f, 0.5f), 1.0f);
}