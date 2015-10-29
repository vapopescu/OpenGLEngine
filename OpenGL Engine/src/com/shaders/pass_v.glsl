#version 130

in vec3 inputPosition;
in vec3 inputNormal;
in vec4 inputColor;
in vec2 inputTexCoord;
in vec3 inputTangent;

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
	tangent = normalize(mat3(vMatrix * nMatrix) * inputTangent);
	tangent = normalize(tangent - normal * dot(tangent, normal));
	bitangent = normalize(cross(normal, tangent));
	eyePosition = vMatrix * mMatrix * vec4(inputPosition, 1.0f);
}