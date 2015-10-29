#version 130

in vec3 inputPosition;
in vec3 inputNormal;
in vec4 inputColor;
in vec2 inputTexCoord;

out vec4 color;
out vec2 texCoord;
out vec3 normal;
out vec3 tangent;
out vec3 bitangent;
out vec4 eyePosition;

uniform mat4 mMatrix;
uniform mat4 vMatrix;
uniform mat4 pMatrix;
uniform mat4 nMatrix;

void main(void) {
	gl_Position = pMatrix * vMatrix * mMatrix * vec4(inputPosition, 1.0f);
	color = inputColor;
	texCoord = inputTexCoord;
	normal = normalize(mat3(vMatrix * nMatrix) * inputNormal);
	tangent = mat3(vMatrix * nMatrix) * vec3(1,0,0);
	bitangent = mat3(vMatrix * nMatrix) * vec3(0,1,0);
	eyePosition = vMatrix * mMatrix * vec4(inputPosition, 1.0f);
}