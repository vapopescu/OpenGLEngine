package com.objects;

import com.*;
import com.objects.textures.*;
import com.utils.math.*;

/**
 * @author Vlad
 *
 */
public class Object extends Loadable {
	
	public String name;
	
	protected Vector3f position = new Vector3f(0, 0, 0);
	protected Vector3f rotation = new Vector3f(0, 0, 0);
	protected Vector3f scale = new Vector3f(1, 1, 1);
	
	protected Matrix4f modelMatrix = null;
	protected Matrix4f normalMatrix = null;
	
	protected Mesh mesh = new Mesh();
	protected Material material = new Material();
	
	public void calculateModelMatrix() {
		modelMatrix = new Matrix4f();
		
		Matrix4f.translate(position, modelMatrix, modelMatrix);
		Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.scale(scale, modelMatrix, modelMatrix);
	}
	
	public void calculateNormalMatrix() {
		normalMatrix = new Matrix4f();
		Vector3f invScale = new Vector3f();
		boolean ok = true;
		
		if(scale.x == 0){
			invScale.y = 0;
			invScale.z = 0;
			ok = false;
		}
		if(scale.y == 0){
			invScale.z = 0;
			invScale.x = 0;
			ok = false;
		}
		if(scale.z == 0){
			invScale.x = 0;
			invScale.y = 0;
			ok = false;
		}
		if(ok){
			invScale.x = 1f / scale.x;
			invScale.y = 1f / scale.y;
			invScale.z = 1f / scale.z;
		}
		
		Matrix4f.translate(position, normalMatrix, normalMatrix);
		Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), normalMatrix, normalMatrix);
		Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), normalMatrix, normalMatrix);
		Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), normalMatrix, normalMatrix);
		Matrix4f.scale(invScale, normalMatrix, normalMatrix);
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