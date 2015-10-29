package com.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.*;

import com.objects.textures.*;

public class PostFilter extends Screen {
	int fboAux;
	protected Texture[] tIdAux = null;

	protected void initializeFbo() {
		// The framebuffer, which regroups 0, 1, or more textures, and 0 or 1
		// depth buffer.
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);

		tId = new TextureFBO[3];
		tId[0] = new TextureFBO(0, GL_DEPTH_ATTACHMENT, GL_DEPTH_COMPONENT);
		tId[1] = new TextureFBO(1, GL_COLOR_ATTACHMENT0, GL_RGB);
		tId[2] = new TextureFBO(2, GL_COLOR_ATTACHMENT1, GL_RGB);
		drawBuffers = BufferUtils.createIntBuffer(tId.length);
		for (int i = 0; i < tId.length - 1; i++) {
			drawBuffers.put(GL_COLOR_ATTACHMENT0 + i);
		}
		drawBuffers.flip();
		glDrawBuffers(drawBuffers);

		fboAux = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fboAux);

		tIdAux = new TextureFBO[3];
		tIdAux[0] = new TextureFBO(0, GL_DEPTH_ATTACHMENT, GL_DEPTH_COMPONENT);
		tIdAux[1] = new TextureFBO(1, GL_COLOR_ATTACHMENT0, GL_RGB);
		tIdAux[2] = new TextureFBO(2, GL_COLOR_ATTACHMENT1, GL_RGB);
		glDrawBuffers(drawBuffers);

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void apply(String filter) {
		glBindFramebuffer(GL_FRAMEBUFFER, fboAux);
		glViewport(0, 0, width, height);
		glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		render(filter);
		
		int aux1;
		aux1 = fbo;
		fbo = fboAux;
		fboAux = aux1;

		Texture[] aux2;
		aux2 = tId;
		tId = tIdAux;
		tIdAux = aux2;

		//glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}

	public void drawTo() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, width, height);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		render("Screen");
	}
}
