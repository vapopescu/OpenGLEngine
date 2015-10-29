#version 130

in vec3 inputPosition;
in vec3 inputNormal;
in vec4 inputColor;
in vec2 inputTexCoord;

out vec4 color;
out vec2 texCoord;

void main(void) {
	gl_Position = vec4(inputPosition, 1.0f);
	color = inputColor;
	texCoord = inputTexCoord;
}