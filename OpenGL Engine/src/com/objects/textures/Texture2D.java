package com.objects.textures;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;

import com.*;

/**
 * @author Vlad
 *
 */
public class Texture2D extends Texture {
	
	public Texture2D() {

	}

	public Texture2D(int index, String path, boolean gamma) {
		this.index = index;
		this.path = path;
		this.gamma = gamma;
		startThread();
	}

	protected void thread() {
		loadTextureTga(path);
	}

	public void load() {
		if (finished && !loaded) {
			initializeTex();
			loaded = true;
		}
	}

	private void initializeTex() {
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		int internalFormat = GL_RGBA;
		if (gamma)
			internalFormat = GL_SRGB_ALPHA;
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, imageBuffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		int aniso = Settings.getAnisotropy();
		if (aniso > 0)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, aniso);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
