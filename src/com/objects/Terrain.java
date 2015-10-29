package com.objects;

import static com.utils.Utils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.lwjgl.*;

import com.objects.textures.*;
import com.shaders.*;
import com.utils.math.*;


public class Terrain extends Object{
	
	private int width = 0, height = 0;
	private float lenXY = 1;
	private float lenZ = 4 * lenXY;
	private float def = 4;
	
	private int sId[];
	private int tiles = 8;
	private Mesh mesh = new Mesh();
	private Texture2D heightMap = new Texture2D();
	private TileMap tileMap = new TileMap();
	private Texture2D[] splatMap = new Texture2D[0];
	
	public Terrain() {
		
	}
	
	public Terrain(String name) {
		this.name = name;
		splatMap = new Texture2D[(tiles / 4) + (tiles % 4 == 0 ? 0 : 1)];
		for (int i = 0; i < splatMap.length; i++)
			splatMap[i] = new Texture2D();
		startThread();
	}

	protected void thread() {
		subcomp.add(heightMap = new Texture2D(0, "terrain/" + name + "/heightmap.tga", false));
		subcomp.add(tileMap = new TileMap("terrain/" + name + "/tilemap", tiles));
		for (int i = 0; i < splatMap.length; i++)
			subcomp.add(splatMap[i] = new Texture2D(2 + i, "terrain/" + name + "/splat" + i +".tga", false));
		
		synchronized (heightMap) {
			while (heightMap.selfReady() != 1) {
				try {
					heightMap.wait(100);
				} catch (InterruptedException e) {
					
				}
			}
		}
		
		width = (heightMap.getWidth() - 1);
		height = (heightMap.getHeight() - 1);
		
		setPosition(new Vector3f((float) -(width * lenXY) / 2, (float) -(height * lenXY) / 2, 0));
		setScale(new Vector3f(lenXY, lenXY, lenZ));
		
		loadTerrain();
		calculateNormals();
		mesh.loadBuffers();
	}
	
	public void load() {
		if (ready && !loaded) {
			loadSegmentBuffers();
			mesh.loadVBO();
			loaded = true;
		}
		heightMap.load();
		tileMap.load();
		for (int i = 0; i < splatMap.length; i++) {
			splatMap[i].load();
		}
	}
	
	private void loadTerrain() {
		int nx = width + 1, ny = height + 1;
		mesh.vertexCount = nx * ny;
		mesh.vertices = new Vertex[mesh.vertexCount];
		sId = new int[height];
		
		ByteBuffer image = heightMap.getTexture();
		
		for (int j = 0; j < heightMap.getWidth(); j++) {
			for (int i = 0; i < heightMap.getHeight(); i++) {
				image.get();	//Discard blue byte.
				image.get();	//Discard green byte.
				int r = byteToInt(image.get());
				image.get();	//Discard alpha byte.

				int n = j * nx + i;
				
				mesh.vertices[n] = new Vertex();
				mesh.vertices[n].setXYZ(i, j, (float) r / 255 - 0.5f);
				mesh.vertices[n].setUV(i / def, j / def);
			}
		}
	}
	
	private void loadSegmentBuffers() {
		int nx = width + 1;
		IntBuffer indices = BufferUtils.createIntBuffer(height * 2 + 2);
		for (int j = 0; j < height; j++) {
			indices.clear();
			for (int i = 0; i < width + 1 ; i++) {
				int n = (j * nx + i);
				indices.put(n + nx);
				indices.put(n);
			}
			indices.flip();
			sId[j] = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, sId[j]);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}
	
	private void calculateNormals() {
		int nx = width + 1;
		for (int j = 0; j < height + 1; j++) {
			for (int i = 0; i < width + 1; i++) {
				Vector3f c, u, d, l, r, result;
				float coords[] = new float[3];
				int cn, un, dn, ln, rn;
				un = dn = ln = rn = cn = j * nx + i;
				
				if(j < height){
					un += nx;
				}
				if(j > 0){
					dn -= nx;
				}
				if (i > 0) {
					ln--;
				}
				if (i < width) {
					rn++;
				}
				
				coords = mesh.vertices[cn].getXYZ();
				c = new Vector3f(coords[0], coords[1], coords[2]);
				coords = mesh.vertices[un].getXYZ();
				u = new Vector3f(coords[0], coords[1], coords[2]);
				coords = mesh.vertices[dn].getXYZ();
				d = new Vector3f(coords[0], coords[1], coords[2]);
				coords = mesh.vertices[ln].getXYZ();
				l = new Vector3f(coords[0], coords[1], coords[2]);
				coords = mesh.vertices[rn].getXYZ();
				r = new Vector3f(coords[0], coords[1], coords[2]);
				
				c = c.negate();
				Vector3f.add(u, c, u);
				Vector3f.add(d, c, d);
				Vector3f.add(l, c, l);
				Vector3f.add(r, c, r);

				c = new Vector3f(0, 0, 0);
				result = new Vector3f(0, 0, 0);
				Vector3f.cross(u, l, c);
				Vector3f.add(c, result, result);
				Vector3f.cross(l, d, c);
				Vector3f.add(c, result, result);
				Vector3f.cross(d, r, c);
				Vector3f.add(c, result, result);
				Vector3f.cross(r, u, c);
				Vector3f.add(c, result, result);
				
				if (result.length() > 0) {
					result.normalize();
				}
				
				mesh.vertices[cn].setNXYZ(result.x, result.y, result.z);
			}
		}
	}
	
	public void render() {
		calculateModelMatrix();
		calculateNormalMatrix();
		Shader.setMNMatrices(modelMatrix, normalMatrix);
		Shader.setTerrain(width / def, height / def, tiles);
		
		Shader.setProgram("Terrain");
		tileMap.bind();
		for (int i = 0; i < splatMap.length; i++)
			splatMap[i].bind();

		// Bind to the VAO that has all the information about the vertices
		glBindVertexArray(mesh.vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		
		// Bind the strip index VBO and draw the buffers
		for (int j = 0; j < sId.length; j++) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, sId[j]);
			for (int i = 0; i < width; i++) {
				glDrawElements(GL_TRIANGLE_STRIP, 2 + 2, GL_UNSIGNED_INT, i * 2 * Integer.SIZE / 8);
			}
		}
		
		// Put everything back to default (deselect)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glBindVertexArray(0);

		Shader.setProgram("");
	}
	
	public void destroy() {
		for (int i = 0; i < sId.length; i++) {
			glDeleteBuffers(sId[i]);
		}
		
		tileMap.destroy();
		mesh.destroy();
		heightMap.destroy();
		for (int i = 0; i < splatMap.length; i++)
			splatMap[i].destroy();
	}
}
