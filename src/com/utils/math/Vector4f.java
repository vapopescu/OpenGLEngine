package com.utils.math;

import java.nio.*;

public class Vector4f extends Vector3f {
	public float w;
	
	public Vector4f() {
		super();
		w = 0;
	}
	
	public Vector4f(float nx, float ny, float nz, float nw) {
		super(nx, ny, nz);
		w = nw;
	}
	
	public Vector4f(Vector4f v) {
		super((Vector3f) v);
		w = v.w;
	}
	
	public Vector4f negate() {
		return new Vector4f(-x, -y, -z, -w);
	}
	
	public void store(FloatBuffer buf) {
		buf.clear();
		buf.put(x);
		buf.put(y);
		buf.put(z);
		buf.put(w);
		buf.flip();
	}
	
	public static void multiply(Matrix4f left, Vector4f right, Vector4f dest) {
		Matrix4f a = new Matrix4f(left);
		Vector4f b = new Vector4f(right);
		
		if(dest == null)
			dest = new Vector4f();
		
		dest.x = a.m00 * b.x + a.m10 * b.y + a.m20 * b.z + a.m30 * b.w;
		dest.y = a.m01 * b.x + a.m11 * b.y + a.m21 * b.z + a.m31 * b.w;
		dest.z = a.m02 * b.x + a.m12 * b.y + a.m22 * b.z + a.m32 * b.w;
		dest.w = a.m03 * b.x + a.m13 * b.y + a.m23 * b.z + a.m33 * b.w;
	}
}
