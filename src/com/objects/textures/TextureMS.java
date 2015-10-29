package com.objects.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import com.*;

public class TextureMS extends TextureFBO {
	
	public TextureMS(int index, int attachment, int format) {
		super(index, attachment, format);
	}
	
	protected void initializeTex() {
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, id);
		
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, Settings.getSamples(), format, width, height, false);
		glTexParameteri(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D_MULTISAMPLE, id, 0);
	}
	
	public void bind(int n) {
		glActiveTexture(GL_TEXTURE0 + n);
		glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, id);
	}
}