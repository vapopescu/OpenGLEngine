package com.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;

import com.utils.math.*;

/**
 * @author Vlad
 *
 */
public class Shader {
	private static ArrayList<Shader> shList = new ArrayList<Shader>();
	protected static int setId = 0;

	private String name;
	protected int pId = 0;

	private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
	/*private FloatBuffer vector3fBuffer = BufferUtils.createFloatBuffer(3);
	private FloatBuffer vector4fBuffer = BufferUtils.createFloatBuffer(4);*/

	private Shader(String name, String vsLoc, String fsLoc) {
		this.name = name;
		pId = buildProgram(vsLoc, fsLoc);
		setUniforms();
	}

	public static void initialize() {
		shList.add(new Shader("Pass", "pass_v.glsl", "pass_f.glsl") {
			protected void setUniforms() {
				setUniformTex("diffuseTex", 0);
				setUniformTex("normalTex", 1);
			}
		});
		shList.add(new Shader("Screen", "screen_v.glsl", "screen_f.glsl") {
			protected void setUniforms() {
				setUniformTex("depthTex", 0);
				setUniformTex("diffuseTex", 1);
				setUniformTex("normalTex", 2);
			}
		});
		shList.add(new Shader("Terrain", "terr_v.glsl", "terr_f.glsl") {
			protected void setUniforms() {
				setUniformTex("diffuseTex", 0);
				setUniformTex("normalTex", 1);
				setUniformTerrainTex("splatTex", 8, 2);
			}
		});
		shList.add(new Shader("BlurX", "screen_v.glsl", "blur9X.glsl") {
			protected void setUniforms() {
				setUniformTex("depthTex", 0);
			}
		});
		shList.add(new Shader("BlurY", "screen_v.glsl", "blur9Y.glsl") {
			protected void setUniforms() {
				setUniformTex("depthTex", 0);
			}
		});
	}

	/**
	 * Builds a shader program using the shader files specified.
	 * 
	 * @param vsLoc
	 *            - Vertex shader address in file system.
	 * @param fsLoc
	 *            - Fragment shader address in file system.
	 * @return The ID of the shader program created.
	 */
	protected int buildProgram(String vsLoc, String fsLoc) {
		int vsId = this.loadShader("src/com/shaders/" + vsLoc, GL_VERTEX_SHADER);
		int fsId = this.loadShader("src/com/shaders/" + fsLoc, GL_FRAGMENT_SHADER);

		int id = glCreateProgram();

		glAttachShader(id, vsId);
		glAttachShader(id, fsId);

		glBindAttribLocation(id, 0, "inputPosition");
		glBindAttribLocation(id, 1, "inputNormal");
		glBindAttribLocation(id, 2, "inputTangent");
		glBindAttribLocation(id, 3, "inputColor");
		glBindAttribLocation(id, 4, "inputTexCoord");

		glLinkProgram(id);
		if (glGetProgrami(id, GL_COMPILE_STATUS) == GL_FALSE) {
			outputLinkerErrorMessage(id);
			System.exit(-1);
		}

		return id;
	}

	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			outputShaderErrorMessage(shaderID, filename);
			System.exit(-1);
		}

		return shaderID;
	}

	void outputShaderErrorMessage(int shaderId, String shaderFilename) {
		int logSize;
		String infoLog;

		// Get the size of the string containing the information log for the
		// failed shader compilation message.
		logSize = glGetShaderi(shaderId, GL_INFO_LOG_LENGTH);

		// Now retrieve the info log.
		infoLog = glGetShaderInfoLog(shaderId, logSize);

		System.err.println("Error compiling " + shaderFilename + ":\n" + infoLog);
	}

	void outputLinkerErrorMessage(int programId) {
		int logSize;
		String infoLog;

		// Get the size of the string containing the information log for the
		// failed shader compilation message.
		logSize = glGetProgrami(programId, GL_INFO_LOG_LENGTH);

		// Now retrieve the info log.
		infoLog = glGetProgramInfoLog(programId, logSize);

		System.err.println("Error linking program:\n" + infoLog);
	}

	protected void setUniforms() {

	}

	public static void setVPMatrices(Matrix4f nViewMatrix, Matrix4f nProjectionMatrix) {
		getShader("Pass").setUniformMatrix4f("vMatrix", nViewMatrix);
		getShader("Pass").setUniformMatrix4f("pMatrix", nProjectionMatrix);
		getShader("Terrain").setUniformMatrix4f("vMatrix", nViewMatrix);
		getShader("Terrain").setUniformMatrix4f("pMatrix", nProjectionMatrix);
	}

	public static void setMNMatrices(Matrix4f nModelMatrix, Matrix4f nNormalMatrix) {
		getShader("Pass").setUniformMatrix4f("mMatrix", nModelMatrix);
		getShader("Pass").setUniformMatrix4f("nMatrix", nNormalMatrix);
		getShader("Terrain").setUniformMatrix4f("mMatrix", nModelMatrix);
		getShader("Terrain").setUniformMatrix4f("nMatrix", nNormalMatrix);
	}

	public static void setTangents(Vector3f nTangent, Vector3f nBitangent) {
		getShader("Pass").setUniformVector3f("iTangent", nTangent);
		getShader("Pass").setUniformVector3f("iBitangent", nBitangent);
	}

	public static void setLight(Vector4f nDiffuseLight, Vector4f nAmbientLight, Vector4f nSpecularLight,
			Vector4f nLightPosition) {
		getShader("Pass").setUniformVector4f("lAmbient", nAmbientLight);
		getShader("Pass").setUniformVector4f("lightPosition", nLightPosition);
		getShader("Terrain").setUniformVector4f("lAmbient", nAmbientLight);
		getShader("Terrain").setUniformVector4f("lightPosition", nLightPosition);
	}

	public static void setTerrain(float x, float y, float z) {
		getShader("Terrain").setVector2f("terrainSize", x, y, z);
	}
	
	private void setUniformMatrix4f(String name, Matrix4f matrix) {
		glUseProgram(pId);
		matrix.store(floatBuffer);
		glUniformMatrix4(glGetUniformLocation(pId, name), false, floatBuffer);
		glUseProgram(setId);
	}
	
	protected void setVector2f(String name, float x, float y, float z) {
		glUseProgram(pId);
		glUniform3f(glGetUniformLocation(pId, name), x, y, z);
		glUseProgram(setId);
	}
	
	private void setUniformVector3f(String name, Vector3f vector) {
		glUseProgram(pId);
		vector.store(floatBuffer);
		glUniform3(glGetUniformLocation(pId, name), floatBuffer);
		glUseProgram(setId);
	}

	private void setUniformVector4f(String name, Vector3f vector) {
		glUseProgram(pId);
		vector.store(floatBuffer);
		glUniform4(glGetUniformLocation(pId, name), floatBuffer);
		glUseProgram(setId);
	}
	
	protected void setUniformTex(String name, int n) {
		glUseProgram(pId);
		glUniform1i(glGetUniformLocation(pId, name), n);
		glUseProgram(setId);
	}
	
	protected void setUniformTerrainTex(String name,int tiles, int n) {
		glUseProgram(pId);
		int loc = glGetUniformLocation(pId, name);
		for (int i = 0; i < tiles; i++)
			glUniform1i(loc + i, n + i);
		glUseProgram(setId);
	}

	public static void setProgram(String name) {
		if (name == "") {
			setId = 0;
			glUseProgram(setId);
			return;
		}

		Shader sh = getShader(name);
		if (sh != null)
			setId = sh.pId;
		else
			setId = 0;

		glUseProgram(setId);
	}

	private static Shader getShader(String name) {
		for (int i = 0; i < shList.size(); i++)
			if (shList.get(i).name == name) {
				return shList.get(i);
			}
		return null;
	}

	private void destroy() {
		glDeleteProgram(pId);
	}

	public static void destroyShaders() {
		// Delete the shaders.
		glUseProgram(0);
		for (int i = 0; i < shList.size(); i++)
			shList.get(i).destroy();
	}
}
