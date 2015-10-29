#version 150

in vec4 color;
in vec2 texCoord;

out vec4 outputColor;

uniform sampler2DMS depthTex;
uniform sampler2DMS diffuseTex;
uniform sampler2DMS normalTex;

void main(void) {
	ivec2 pixel = ivec2(texCoord * textureSize(diffuseTex));
	vec4 textureColor = (texelFetch(diffuseTex, pixel, 0) + 
		texelFetch(diffuseTex, pixel, 1) +
		texelFetch(diffuseTex, pixel, 2) +
		texelFetch(diffuseTex, pixel, 3)) / 4.0f;
		
	vec4 normalColor = (texelFetch(normalTex, pixel, 0) + 
		texelFetch(normalTex, pixel, 1) +
		texelFetch(normalTex, pixel, 2) +
		texelFetch(normalTex, pixel, 3)) / 4.0f;
		
	float depth = pow((texelFetch(depthTex, pixel, 0).x + 
		texelFetch(depthTex, pixel, 1).x +
		texelFetch(depthTex, pixel, 2).x +
		texelFetch(depthTex, pixel, 3).x) / 4.0f, 10.0f);
	
	outputColor = pow(textureColor, vec4(1.0f / 2.2f));
	//outputColor = normalColor;
	//outputColor = vec4(vec3(depth), 1);
	//outputColor = texture2D(posTex, texCoord);
 }