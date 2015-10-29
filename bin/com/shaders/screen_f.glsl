#version 130

in vec4 color;
in vec2 texCoord;

out vec4 outputColor;

uniform sampler2D diffuseTex;

void main(void) {
	outputColor = texture2D(diffuseTex, texCoord);
 }