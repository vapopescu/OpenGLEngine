package com;

import com.utils.math.*;

/**
 * @author Vlad
 *
 */
public class Camera extends com.Object {
	private Vector3f pivot = null;
	private Vector3f rotation = null;
	private Vector3f strafe = null;
	
	private Matrix4f viewMatrix = null;
	private Matrix4f perspectiveMatrix = null;
	private Matrix4f orthographicMatrix = null;
	private boolean perspectiveView = true;
	
	private float near_plane = 0.5f;			//Used by both view modes.
	private float far_plane = 50.0f;
	
	private float fov = (float) Math.PI / 3;	//Used by perspective view.
	
	private float left_plane = -1f;				//Used by orthographic view.
	private float right_plane = 1f;
	private float top_plane = 1f;
	private float bottom_plane = -1f;
	
	private static Camera activeCamera = null;
	
	public Camera() {
		startThread();
	}
	
	protected void thread() {
		pivot = new Vector3f(0, 0, 1);
		rotation = new Vector3f((float) Math.PI / 12, 0, (float) Math.PI);
		strafe = new Vector3f(0, -5, 0);
		
		updatePerspectiveMatrix();
		updateOrthographicMatrix();
		
		setActive();
	}
	
	public void updatePerspectiveMatrix() {
		perspectiveMatrix = new Matrix4f();
		
		float y_scale = 1 / (float) Math.tan(fov / 2.0f);
		float x_scale = y_scale / Settings.getWidth() * Settings.getHeight();
		float frustum_length = far_plane - near_plane;
		
		perspectiveMatrix.m00 = x_scale;
		perspectiveMatrix.m11 = y_scale;
		perspectiveMatrix.m22 = -((far_plane + near_plane) / frustum_length);
		perspectiveMatrix.m23 = -1;
		perspectiveMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
        perspectiveMatrix.m33 = 0;
	}
	
	public void updateOrthographicMatrix() {
		orthographicMatrix = new Matrix4f();
		float dx = right_plane - left_plane;
		float dy = top_plane - bottom_plane;
		float dz = far_plane - near_plane;
		float sx = right_plane + left_plane;
		float sy = top_plane + bottom_plane;
		float sz = far_plane + near_plane;
		
		orthographicMatrix.m00 =  2 / dx;
		orthographicMatrix.m11 =  2 / dy;
		orthographicMatrix.m22 =  -2 / dz;
		orthographicMatrix.m30 = -sx / dx;
		orthographicMatrix.m31 = -sy / dy;
		orthographicMatrix.m32 = -sz / dz;
	}
	
	public Matrix4f getViewMatrix() {
		viewMatrix = new Matrix4f();
		
		//  1  0  0  0
		//  0  0 -1  0
		//  0  1  0  0
		//  0  0  0  1
		
		viewMatrix.m11 = 0.0f;
		viewMatrix.m12 = -1.0f;
		viewMatrix.m21 = 1.0f;
		viewMatrix.m22 = 0.0f;
		
		Matrix4f.translate((Vector3f)strafe.negate(), viewMatrix, viewMatrix);
		Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Matrix4f.translate((Vector3f)pivot.negate(), viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	
	public static Matrix4f getLightMatrix() {
		Matrix4f cameraMatrix = new Matrix4f();
		Vector3f rotation = Camera.activeCamera.rotation;
		
		cameraMatrix.m11 = 0.0f;
		cameraMatrix.m12 = -1.0f;
		cameraMatrix.m21 = 1.0f;
		cameraMatrix.m22 = 0.0f;
		
		Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), cameraMatrix, cameraMatrix);
		Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), cameraMatrix, cameraMatrix);
		Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), cameraMatrix, cameraMatrix);

		return cameraMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		if(perspectiveView)
			return perspectiveMatrix;
		else
			return orthographicMatrix;
	}
	
	public void setPivot(Vector3f vector) {
		pivot = vector;
	}
	
	public void setRotation(Vector3f vector) {
		rotation = vector;
	}
	
	public void setStrafe(Vector3f vector) {
		strafe = vector;
	}
	
	public void setActive() {
		activeCamera = this;
	}
	
	public Vector3f getPivot() {
		return new Vector3f(pivot);
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}
	
	public Vector3f getStrafe() {
		return new Vector3f(strafe);
	}
	
	public static Camera getActive() {
		return activeCamera;
	}

	public void destroy() {
		
	}
}
