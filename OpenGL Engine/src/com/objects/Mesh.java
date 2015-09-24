package com.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.lwjgl.*;

import com.utils.math.*;

public class Mesh {
	protected int vaoId = 0;
	protected int vboId = 0;
	protected int vboiId = 0;
	protected int format = GL_TRIANGLES;
	
	protected Vertex[] vertices = null;
	protected int vertexCount = 0;

	protected int[] indexArray = null;
	protected int indexCount = 0;

	protected FloatBuffer verticesBuffer = null;
	protected IntBuffer indexBuffer = null;
	
	protected void loadBuffers() {
		// Put each 'Vertex' in one FloatBuffer
		verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			// Add position, color and texture floats to the buffer
			verticesBuffer.put(vertices[i].getElements());
		}
		verticesBuffer.flip();
		indexBuffer = BufferUtils.createIntBuffer(indexCount);
		for (int i = 0; i < indexCount; i++) {
			indexBuffer.put(indexArray[i]);
		}
		indexBuffer.flip();
	}

	protected void loadVBO() {
		// Create a new Vertex Array Object in memory and select it (bind)
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STREAM_DRAW);

		// Put the position coordinates in attribute list 0
		glVertexAttribPointer(0, Vertex.positionElementCount, GL_FLOAT, false, Vertex.stride,
				Vertex.positionByteOffset);
		// Put the normal coordinates in attribute list 1
		glVertexAttribPointer(1, Vertex.normalElementCount, GL_FLOAT, false, Vertex.stride, Vertex.normalByteOffset);
		// Put the tangent components in attribute list 2
		glVertexAttribPointer(2, Vertex.tangentElementCount, GL_FLOAT, false, Vertex.stride, Vertex.tangentByteOffset);
		// Put the color components in attribute list 3
		glVertexAttribPointer(3, Vertex.colorElementCount, GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		// Put the texture coordinates in attribute list 4
		glVertexAttribPointer(4, Vertex.textureElementCount, GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind)
		vboiId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void computeTangents() {
		for (int i = 0; i < indexCount; i += 3) {

			Vector3f[] tan = new Vector3f[3];
			tan[0] = new Vector3f(vertices[indexArray[i]].getTXYZ());
			tan[1] = new Vector3f(vertices[indexArray[i + 1]].getTXYZ());
			tan[2] = new Vector3f(vertices[indexArray[i + 2]].getTXYZ());

			float[] vec1 = null;
			float[] vec2 = null;
			float[] vec3 = null;
			float[] uv1 = null;
			float[] uv2 = null;
			float[] uv3 = null;
			float aux;
			vec1 = vertices[indexArray[i]].getXYZ();
			vec2 = vertices[indexArray[i + 1]].getXYZ();
			vec3 = vertices[indexArray[i + 2]].getXYZ();
			uv1 = vertices[indexArray[i]].getUV();
			uv2 = vertices[indexArray[i + 1]].getUV();
			uv3 = vertices[indexArray[i + 2]].getUV();

			aux = (uv2[0] - uv1[0]) * (uv3[1] - uv1[1]) - (uv2[1] - uv1[1]) * (uv3[0] - uv1[0]);

			float dTanX = ((vec2[0] - vec1[0]) * (uv3[1] - uv1[1]) - (vec3[0] - vec1[0]) * (uv2[1] - uv1[1])) / aux;
			float dTanY = ((vec2[1] - vec1[1]) * (uv3[1] - uv1[1]) - (vec3[1] - vec1[1]) * (uv2[1] - uv1[1])) / aux;
			float dTanZ = ((vec2[2] - vec1[2]) * (uv3[1] - uv1[1]) - (vec3[2] - vec1[2]) * (uv2[1] - uv1[1])) / aux;

			vertices[indexArray[i]].setTXYZ(tan[0].x + dTanX, tan[0].y + dTanY, tan[0].z + dTanZ);
			vertices[indexArray[i + 1]].setTXYZ(tan[1].x + dTanX, tan[1].y + dTanY, tan[1].z + dTanZ);
			vertices[indexArray[i + 2]].setTXYZ(tan[2].x + dTanX, tan[2].y + dTanY, tan[2].z + dTanZ);
		}

		for (int i = 0; i < indexCount; i++) {
			Vector3f tan = new Vector3f(vertices[indexArray[i]].getTXYZ());
			tan.normalize();
			vertices[indexArray[i]].setTXYZ(tan.x, tan.y, tan.z);
		}
	}
	
	protected void loadScreenMesh() {
		vertexCount = 4;
		indexCount = 4;
		vertices = new Vertex[4];
		indexArray = new int[indexCount];
		
		for (int i = 0; i < 4; i++) {
			vertices[i] = new Vertex();
			vertices[i].setNXYZ(0, 0, 1);
			indexArray[i] = i;
		}
		
		vertices[0].setXYZ(-1, 1, -1);
		vertices[0].setUV(0, 1);
		vertices[1].setXYZ(-1, -1, -1);
		vertices[1].setUV(0, 0);
		vertices[2].setXYZ(1, 1, -1);
		vertices[2].setUV(1, 1);
		vertices[3].setXYZ(1, -1, -1);
		vertices[3].setUV(1, 0);
	}
	
	public void render() {
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
		glDrawElements(format, indexCount, GL_UNSIGNED_INT, 0);

		// Put everything back to default (deselect)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glBindVertexArray(0);
	}
	
	public void setFormat(int format) {
		this.format = format;
	}
	
	public void destroy() {
		// Select the VAO
		glBindVertexArray(vaoId);

		// Disable the VBO index from the VAO attributes list
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);

		// Delete the vertex VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboId);

		// Delete the index VBO
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboiId);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
