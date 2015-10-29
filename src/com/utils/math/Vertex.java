package com.utils.math;

/**
 * @author Vlad
 *
 */
public class Vertex {
	// Vertex data
	private float[] xyzw = {0, 0, 0, 1};
	private float[] nxyz = {0, 0, 0};
	private float[] txyz = {0, 0, 0};
	private float[] rgba = {1, 1, 1, 1};
	private float[] uv = {0, 0};
	
	// The amount of bytes an element has
	private static final int elementBytes = 4;
	
	// Elements per parameter
	public static final int positionElementCount = 4;
	public static final int normalElementCount = 3;
	public static final int tangentElementCount = 3;
	public static final int colorElementCount = 4;
	public static final int textureElementCount = 2;
	
	// Bytes per parameter
	public static final int positionBytesCount = positionElementCount * elementBytes;
	public static final int normalByteCount = normalElementCount * elementBytes;
	public static final int tangentByteCount = tangentElementCount * elementBytes;
	public static final int colorByteCount = colorElementCount * elementBytes;
	public static final int textureByteCount = textureElementCount * elementBytes;
	
	// Byte Offsets per parameter
	public static final int positionByteOffset = 0;
	public static final int normalByteOffset = positionByteOffset + positionBytesCount;
	public static final int tangentByteOffset = normalByteOffset + normalByteCount;
	public static final int colorByteOffset = tangentByteOffset + tangentByteCount;
	public static final int textureByteOffset = colorByteOffset + colorByteCount;
	
	// The amount of elements that a vertex has
	public static final int elementCount = positionElementCount + normalElementCount + 
			tangentElementCount + colorElementCount + textureElementCount;	
	// The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
	public static final int stride = positionBytesCount + normalByteCount + 
			tangentByteCount + colorByteCount + textureByteCount;
	
	// Setters
	public void setXYZ(float x, float y, float z) {
		setXYZW(x, y, z, 1f);
	}
	
	public void setNXYZ(float x, float y, float z) {
		nxyz = new float[] {x, y, z};
	}
	
	public void setTXYZ(float x, float y, float z) {
		txyz = new float[] {x, y, z};
	}
	
	public void setRGB(float r, float g, float b) {
		setRGBA(r, g, b, 1f);
	}
	
	public void setUV(float u, float v) {
		uv = new float[] {u, v};
	}
	
	public void setXYZW(float x, float y, float z, float w) {
		xyzw = new float[] {x, y, z, w};
	}
	
	public void setRGBA(float r, float g, float b, float a) {
		rgba = new float[] {r, g, b, a};
	}
	
	// Getters	
	public float[] getElements() {
		float[] out = new float[Vertex.elementCount];
		int i = 0;
		
		// Insert XYZW elements
		out[i++] = xyzw[0];
		out[i++] = xyzw[1];
		out[i++] = xyzw[2];
		out[i++] = xyzw[3];
		// Insert NXYZ elements
		out[i++] = nxyz[0];
		out[i++] = nxyz[1];
		out[i++] = nxyz[2];
		// Insert TXYZ elements
		out[i++] = txyz[0];
		out[i++] = txyz[1];
		out[i++] = txyz[2];
		// Insert RGBA elements
		out[i++] = rgba[0];
		out[i++] = rgba[1];
		out[i++] = rgba[2];
		out[i++] = rgba[3];
		// Insert UV elements
		out[i++] = uv[0];
		out[i++] = uv[1];
		
		return out;
	}
	
	public float[] getXYZW() {
		return new float[] {xyzw[0], xyzw[1], xyzw[2], xyzw[3]};
	}
	
	public float[] getXYZ() {
		return new float[] {xyzw[0], xyzw[1], xyzw[2]};
	}
	
	public float[] getNXYZ() {
		return new float[] {nxyz[0], nxyz[1], nxyz[2]};
	}
	
	public float[] getTXYZ() {
		return new float[] {txyz[0], txyz[1], txyz[2]};
	}
	
	public float[] getRGBA() {
		return new float[] {rgba[0], rgba[1], rgba[2], rgba[3]};
	}
	
	public float[] getRGB() {
		return new float[] {rgba[0], rgba[1], rgba[2]};
	}
	
	public float[] getUV() {
		return new float[] {uv[0], uv[1]};
	}
}