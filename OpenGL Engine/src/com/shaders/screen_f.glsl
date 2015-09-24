#version 330

in vec4 color;
in vec2 texCoord;

out vec4 outputColor;

uniform sampler2D depthTex;
uniform sampler2D diffuseTex;
uniform sampler2D normalTex;

void main(void) {
	vec4 textureColor = texture2D(diffuseTex, texCoord);
	float depth = pow(texture2D(depthTex, texCoord).x, 150f);
	
	outputColor = pow(textureColor, vec4(1.0f / 2.2f));
	//outputColor = mix(outputColor, vec4(fogColor,1), depth);
	//outputColor = texture2D(posTex, texCoord);
 }
