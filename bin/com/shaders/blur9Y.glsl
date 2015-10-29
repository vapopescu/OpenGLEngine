#version 330
in vec4 color;
in vec2 texCoord;

out vec4 outputColor;

uniform sampler2D diffuseTex;

/*uniform float offset[3] = {0.0, 1.3846153846, 3.2307692308};
uniform float weight[3] = {0.2270270270, 0.3162162162, 0.0702702703};*/

void main(void) {
	vec2 pixel = vec2(0, 1.0f / textureSize(diffuseTex, 0).y);
	
	float offset[3];
	float weight[3];

	offset[0] = 0.0f;
	offset[1] = 1.3846153846f;
	offset[2] = 3.2307692308f;
	
	weight[0] = 0.2270270270f;
	weight[1] = 0.3162162162f;
	weight[2] = 0.0702702703f;
	
	outputColor = texture2D(diffuseTex, texCoord) * weight[0];
	for (int i = 1; i < 3; i++) {
		outputColor += texture2D(diffuseTex, texCoord + offset[i] * pixel) * weight[i];
		outputColor += texture2D(diffuseTex, texCoord - offset[i] * pixel) * weight[i];
	}
 }
