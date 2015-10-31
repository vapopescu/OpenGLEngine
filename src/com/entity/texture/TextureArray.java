package com.entity.texture;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;

import com.*;

public class TextureArray extends Texture{
	private int depth;
	
	public TextureArray() {
		
	}
	
	public TextureArray(int index, String path, int depth, boolean gamma) {
		this.index = index;
		this.path = path;
		this.depth = depth;
		this.gamma = gamma;
		startThread();
	}
	
	protected void thread() {
		loadTextureTga(path);
		height /= depth;
	}
	
	public void load() {
		if (ready && !loaded) {
			initializeTex();
			loaded = true;
		}
	}
	
	private void initializeTex() {
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
		
		int internalFormat = GL_RGBA;
		if (gamma)
			internalFormat = GL_SRGB_ALPHA;
		glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, internalFormat, width, height, depth, 0, format, GL_UNSIGNED_BYTE, imageBuffer);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		int aniso = Settings.getAnisotropy();
		if (aniso > 0)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, aniso);
		glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
	}
	
	public void bind(int n) {
		glActiveTexture(GL_TEXTURE0 + n);
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
	}
}
