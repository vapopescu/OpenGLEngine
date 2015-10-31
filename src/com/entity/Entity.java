package com.entity;

import com.entity.texture.Material;
import com.utils.math.*;

/**
 * @author Vlad
 *
 */
public class Entity extends com.Object {
	
	protected Vector3f position = new Vector3f(0, 0, 0);
	protected Vector3f rotation = new Vector3f(0, 0, 0);
	protected Vector3f scale = new Vector3f(1, 1, 1);
	
	protected Matrix4f modelMatrix = null;
	protected Matrix4f normalMatrix = null;
	
	protected Mesh mesh = new Mesh();
	protected Material material = new Material();
	
	public void calculateMNMatrix() {
		modelMatrix = new Matrix4f();
		normalMatrix = new Matrix4f();
		Vector3f invScale = new Vector3f();
		
		if(scale.x != 0 && scale.y != 0 && scale.z != 0){
			invScale.x = 1f / scale.x;
			invScale.y = 1f / scale.y;
			invScale.z = 1f / scale.z;
		}
		
		Matrix4f.translate(position, modelMatrix, modelMatrix);
		Quaternion q = Quaternion.euler(rotation.x, rotation.y, rotation.z, "ZXY");
		Matrix4f.multiply(modelMatrix, q.toMatrix(), modelMatrix);
		Matrix4f.scale(invScale, modelMatrix, normalMatrix);
		Matrix4f.scale(scale, modelMatrix, modelMatrix);
	}
	
	public void setPosition(Vector3f vector) {
		position = vector;
	}
	
	public void setRotation(Vector3f vector) {
		rotation = vector;
	}
	
	public void setScale(Vector3f vector) {
		scale = vector;
	}
}