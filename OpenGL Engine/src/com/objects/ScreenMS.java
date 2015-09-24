package com.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.*;

import com.objects.textures.*;
import com.shaders.*;

/**
 * @author Vlad
 *
 */
public class ScreenMS extends Screen {
	
	public ScreenMS() {
		super();
	}
	
	public ScreenMS(float r, float g, float b) {
		super(r, g, b);
		}
	
	protected void initializeFbo() {
		// The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		
		tId = new TextureMS[3];
		tId[0] = new TextureMS(0, GL_DEPTH_ATTACHMENT, GL_DEPTH_COMPONENT);
		tId[1] = new TextureMS(1, GL_COLOR_ATTACHMENT0, GL_RGB);
		tId[2] = new TextureMS(2, GL_COLOR_ATTACHMENT1, GL_RGB);
		drawBuffers = BufferUtils.createIntBuffer(tId.length);
		for(int i = 0; i < tId.length - 1; i++) {
			drawBuffers.put(GL_COLOR_ATTACHMENT0 + i);
		}
		drawBuffers.flip();
		glDrawBuffers(drawBuffers);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void render() {
		Shader.setProgram("ScreenMS");
		
		// Bind the textures
		for(int i = 0; i < tId.length; i++) {
			tId[i].bind();
		}

		mesh.render();
		
		Texture.unbind();
		Shader.setProgram("");
	}
}