package com.objects.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import com.*;

public class TextureFBO extends Texture {
	protected int attachment = 0;
	
	public TextureFBO() {
		
	}
	
	public TextureFBO(int index, int attachment, int format) {
		this.attachment = attachment;
		this.index = index;
		this.format = format;
		width = Settings.getWidth();
		height = Settings.getHeight();
		imageBuffer = null;
		
		initializeTex();
	}
	
	protected void initializeTex() {
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, id, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}



