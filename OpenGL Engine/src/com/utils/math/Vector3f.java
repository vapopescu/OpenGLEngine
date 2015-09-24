package com.utils.math;

import java.nio.*;

public class Vector3f {
	public float x, y, z;
	
	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3f(float nx, float ny, float nz) {
		x = nx;
		y = ny;
		z = nz;
	}
	
	public Vector3f(Vector3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public Vector3f(float[] v) {
		try {
			if(v.length < 3)
				throw new Exception();
		} catch (Exception e) {
			System.err.println("Float buffer too small.");
			e.printStackTrace();
		}
		
		x = v[0];
		y = v[1];
		z = v[2];
	}
	
	public static void add(Vector3f left, Vector3f right, Vector3f dest) {
		dest.x = left.x + right.x;
		dest.y = left.y + right.y;
		dest.z = left.z + right.z;
	}
	
	public static float dot(Vector3f left, Vector3f right) {
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}
	
	public static void cross(Vector3f left, Vector3f right, Vector3f dest) {
		Vector3f a = new Vector3f(left);
		Vector3f b = new Vector3f(right);
		
		dest.x = a.y * b.z - a.z * b.y;
		dest.y = a.z * b.x - a.x * b.z;
		dest.z = a.x * b.y - a.y * b.x;
	}
	
	public float length() {
		return x * x + y * y + z * z;
	}
	
	public void normalize() {
		float length = length();
        if (length != 1f && length != 0f){
            length = (float) (1.0f / Math.sqrt(length));
            x *= length;
            y *= length;
            z *= length;
        }
		
	}
	
	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}
	
	public void store(FloatBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
	}
}
