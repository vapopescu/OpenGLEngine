package com.utils.math;

import java.nio.*;

public class Matrix4f {
	public float m00, m01, m02, m03, 
		m10, m11, m12, m13,
		m20, m21, m22, m23,
		m30, m31, m32, m33;
	
	public Matrix4f() {
		m00 = 1;
		m01 = 0;
		m02 = 0;
		m03 = 0;
		
		m10 = 0;
		m11 = 1;
		m12 = 0;
		m13 = 0;
		
		m20 = 0;
		m21 = 0;
		m22 = 1;
		m23 = 0;
		
		m30 = 0;
		m31 = 0;
		m32 = 0;
		m33 = 1;
	}
	
	public Matrix4f(Matrix4f m) {
		m00 = m.m00;
		m01 = m.m01;
		m02 = m.m02;
		m03 = m.m03;
		
		m10 = m.m10;
		m11 = m.m11;
		m12 = m.m12;
		m13 = m.m13;
		
		m20 = m.m20;
		m21 = m.m21;
		m22 = m.m22;
		m23 = m.m23;
		
		m30 = m.m30;
		m31 = m.m31;
		m32 = m.m32;
		m33 = m.m33;
	}
	
	public Matrix4f(float[] v) {
		try {
			if(v.length < 16)
				throw new Exception();
		} catch (Exception e) {
			System.err.println("Float buffer too small.");
			e.printStackTrace();
		}
		
		m00 = v[0];
		m01 = v[1];
		m02 = v[2];
		m03 = v[3];
		
		m10 = v[4];
		m11 = v[5];
		m12 = v[6];
		m13 = v[7];
		
		m20 = v[8];
		m21 = v[9];
		m22 = v[10];
		m23 = v[11];
		
		m30 = v[12];
		m31 = v[13];
		m32 = v[14];
		m33 = v[15];
	}
	
	public void store(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m02);
		buf.put(m03);
		buf.put(m10);
		buf.put(m11);
		buf.put(m12);
		buf.put(m13);
		buf.put(m20);
		buf.put(m21);
		buf.put(m22);
		buf.put(m23);
		buf.put(m30);
		buf.put(m31);
		buf.put(m32);
		buf.put(m33);
	}
	
	public static void multiply(Matrix4f left, Matrix4f right, Matrix4f dest) {
		Matrix4f a, b;
		a = new Matrix4f(left);
		b = new Matrix4f(right);
		
		if(dest == null)
			dest = new Matrix4f();
		
		dest.m00 = a.m00 * b.m00 + a.m10 * b.m01 + a.m20 * b.m02 + a.m30 * b.m03;
		dest.m01 = a.m01 * b.m00 + a.m11 * b.m01 + a.m21 * b.m02 + a.m31 * b.m03;
		dest.m02 = a.m02 * b.m00 + a.m12 * b.m01 + a.m22 * b.m02 + a.m32 * b.m03;
		dest.m03 = a.m03 * b.m00 + a.m13 * b.m01 + a.m23 * b.m02 + a.m33 * b.m03;
		
		dest.m10 = a.m00 * b.m10 + a.m10 * b.m11 + a.m20 * b.m12 + a.m30 * b.m13;
		dest.m11 = a.m01 * b.m10 + a.m11 * b.m11 + a.m21 * b.m12 + a.m31 * b.m13;
		dest.m12 = a.m02 * b.m10 + a.m12 * b.m11 + a.m22 * b.m12 + a.m32 * b.m13;
		dest.m13 = a.m03 * b.m10 + a.m13 * b.m11 + a.m23 * b.m12 + a.m33 * b.m13;
		
		dest.m20 = a.m00 * b.m20 + a.m10 * b.m21 + a.m20 * b.m22 + a.m30 * b.m23;
		dest.m21 = a.m01 * b.m20 + a.m11 * b.m21 + a.m21 * b.m22 + a.m31 * b.m23;
		dest.m22 = a.m02 * b.m20 + a.m12 * b.m21 + a.m22 * b.m22 + a.m32 * b.m23;
		dest.m23 = a.m03 * b.m20 + a.m13 * b.m21 + a.m23 * b.m22 + a.m33 * b.m23;
		
		dest.m30 = a.m00 * b.m30 + a.m10 * b.m31 + a.m20 * b.m32 + a.m30 * b.m33;
		dest.m31 = a.m01 * b.m30 + a.m11 * b.m31 + a.m21 * b.m32 + a.m31 * b.m33;
		dest.m32 = a.m02 * b.m30 + a.m12 * b.m31 + a.m22 * b.m32 + a.m32 * b.m33;
		dest.m33 = a.m03 * b.m30 + a.m13 * b.m31 + a.m23 * b.m32 + a.m33 * b.m33;
	}
	
	public static void translate(Vector3f vec, Matrix4f src, Matrix4f dest) {
		Matrix4f aux = new Matrix4f();
		if (dest == null)
			dest = new Matrix4f();
		
		aux.m30 = vec.x;
		aux.m31 = vec.y;
		aux.m32 = vec.z;
		
		multiply(src, aux, dest);
	}
	
	public static void scale(Vector3f vec, Matrix4f src, Matrix4f dest) {
		Matrix4f aux = new Matrix4f();
		if(dest == null)
			dest = new Matrix4f();
		
		aux.m00 = vec.x;
		aux.m11 = vec.y;
		aux.m22 = vec.z;
		
		multiply(src, aux, dest);
	}
	
	public static void rotate(float angle, Vector3f vec, Matrix4f src, Matrix4f dest) {
		Matrix4f aux = new Matrix4f();
		if(dest == null)
			dest = new Matrix4f();
		
		float c = (float) Math.cos((double) angle);
		float s = (float) Math.sin((double) angle);
		
		aux.m00 = vec.x * vec.x * (1 - c) + c;
		aux.m01 = vec.y * vec.x * (1 - c) + vec.z * s;
		aux.m02 = vec.z * vec.x * (1 - c) - vec.y * s;
		
		aux.m10 = vec.x * vec.y * (1 - c) - vec.z * s;
		aux.m11 = vec.y * vec.y * (1 - c) + c;
		aux.m12 = vec.z * vec.y * (1 - c) + vec.x * s;
		
		aux.m20 = vec.x * vec.z * (1 - c) + vec.y * s;
		aux.m21 = vec.y * vec.z * (1 - c) - vec.x * s;
		aux.m22 = vec.z * vec.z * (1 - c) + c;
		
		multiply(src, aux, src);
	}
	
	public Matrix4f multiply(Matrix4f left, Matrix4f right) {
		Matrix4f result = new Matrix4f();
		multiply(left, right, result);
		return result;
	}
	
	public void translate(Vector3f v) {
		translate(v, this, this);
	}
	
	public void scale(Vector3f v) {
		scale(v, this, this);
	}

	public void rotate(float f, Vector3f v) {
		rotate(f, v, this, this);
	}
	
	public String toString() {
		String str = "";
		
		str = Float.toString(m00) + " " + Float.toString(m10) + " " + Float.toString(m20) + " " + Float.toString(m30) + "\n" + 
				Float.toString(m01) + " " + Float.toString(m11) + " " + Float.toString(m21) + " " + Float.toString(m31) + "\n" + 
				Float.toString(m02) + " " + Float.toString(m12) + " " + Float.toString(m22) + " " + Float.toString(m32) + "\n" + 
				Float.toString(m03) + " " + Float.toString(m13) + " " + Float.toString(m23) + " " + Float.toString(m33);
		
		return str;
	}
}
