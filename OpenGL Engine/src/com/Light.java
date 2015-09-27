package com;

import com.utils.math.*;

/**
 * @author Vlad
 *
 */
@SuppressWarnings("unused")
public class Light extends Loadable {
	private int type = 0;
	private Vector4f diffuseColor = new Vector4f(1, 1, 1, 1);
	private Vector4f ambientColor = new Vector4f(0.1f, 0.1f, 0.1f, 1);
	private Vector4f specularColor = new Vector4f(1, 1, 1, 1);

	private Vector4f translation = new Vector4f(0, 0, 0, 1);
	private Vector3f rotation = new Vector3f(0, 0, 0);
	private Vector3f scale = new Vector3f(1, 1, 1);
	
	public static final int DIRECT = 0;
	public static final int POINT = 1;
	public static final int SPOT = 2;
	
	public Light() {
		
	}
	
	public Light(int nType, float x, float y, float z) {
		type = nType;
		translation.x = x;
		translation.y = y;
		translation.z = z;
		switch (type) {
		case DIRECT:
			translation.w = 0;
			break;
		case POINT:
		case SPOT:
			translation.w = 1;
			break;
		}
		startThread();
	}
	
	public void setDiffuseColor(float red, float green, float blue, float alpha) {
		diffuseColor = new Vector4f(red, green, blue, alpha);
	}

	public void setAmbientColor(float red, float green, float blue, float alpha) {
		ambientColor = new Vector4f(red, green, blue, alpha);
	}

	public void setSpecularColor(float red, float green, float blue, float alpha) {
		specularColor = new Vector4f(red, green, blue, alpha);
	}

	public void setPosition(float x, float y, float z) {
		translation = new Vector4f(x, y, z, 1);
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public Vector4f getPosition(Matrix4f viewMatrix) {
		Vector4f position = new Vector4f();
		Vector4f.multiply(viewMatrix, translation, position);
		return position;
	}
}

