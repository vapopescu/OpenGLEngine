package com.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.nio.*;

import org.lwjgl.*;

import com.*;
import com.objects.textures.*;
import com.shaders.*;
import com.utils.*;

/**
 * @author Vlad
 *
 */
public class Screen extends Object {
	
	protected int fbo = 0;
	protected int width = 0;
	protected int height = 0;
	protected IntBuffer drawBuffers = null;
	protected Texture[] tId = null;
	protected float[] clearColor = {0, 0, 0};
	
	public Screen() {
		name = "Screen";
		width = Settings.getWidth();
		height = Settings.getHeight();
		startThread();
		}
	
	public Screen(float r, float g, float b) {
		name = "Screen";
		width = Settings.getWidth();
		height = Settings.getHeight();
		setClearColor(r, g, b);
		startThread();
		}

	protected void thread() {
		mesh.setFormat(GL_TRIANGLE_STRIP);
		mesh.loadScreenMesh();
		mesh.loadBuffers();
	}
	
	public void load() {
		if(ready && !loaded){
			mesh.loadVBO();
			initializeFbo();
			loaded = true;
		}
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
	
	protected void render(String shader) {
		Shader.setProgram(shader);
		
		// Bind the textures
		for(int i = 0; i < tId.length; i++) {
			tId[i].bind();
		}

		mesh.render();
		
		Texture.unbind();
		Shader.setProgram("");
	}
	
	public void makeActive() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glViewport(0, 0, width, height);
		glClearColor(clearColor[0], clearColor[1], clearColor[2], 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void drawTo(Screen screen) {
		screen.makeActive();
		
		render("ScreenMS");
	}
	
	public void drawTo() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, width, height);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		render("ScreenMS");
	}
	
	public void printScreen(int index) {
		ByteBuffer image = null;
		if (index >= 0 && index <= 5) {
			image = tId[index].getTextureImage();
		} else if (index == -1){
			image = BufferUtils.createByteBuffer(width * height * 3);
			glReadPixels(0, 0, width, height, GL_BGR, GL_UNSIGNED_BYTE, image);
		}
		int time[] = Time.get();
		String filename = "screenshots/" + time[0] + "_" + time[1] + "_" + time[2] + "_"
				+ time[3] + "_" + time[4] + "_" + time[5] + "_" + time[6] + ".tga";
		try {
			FileOutputStream out = new FileOutputStream(filename);
			byte header[] = new byte[18];
			for (int i = 0; i < 18; i++) {
				header[i] = 0;
			}
			
			header[2] = 0x0A;
			header[12] = (byte) (width % 0x100);
			header[13] = (byte) (width / 0x100);
			header[14] = (byte) (height % 0x100);
			header[15] = (byte) (height / 0x100);
			header[16] = 0x18;
			
			out.write(header);
			
			byte pixel[] = new byte[3];
			byte lastPixel[] = new byte[3];
			int n;

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 3);
			IntBuffer pixelNumber = BufferUtils.createIntBuffer(width * height * 3);
			
			image.get(pixel, 0, 3);
			while (image.remaining() >= 3) {
				lastPixel[0] = pixel[0];
				lastPixel[1] = pixel[1];
				lastPixel[2] = pixel[2];
				n = 0;
				
				while (lastPixel[0] == pixel[0] && lastPixel[1] == pixel[1] && lastPixel[2] == pixel[2]
						&& n < 128 && image.remaining() >= 3) {
					image.get(pixel, 0, 3);
					n++;
				}
				
				pixels.put(lastPixel[0]);
				pixels.put(lastPixel[1]);
				pixels.put(lastPixel[2]);
				pixelNumber.put(n);
			}
			
			pixelNumber.put(0);
			pixels.flip();
			pixelNumber.flip();
			
			n = pixelNumber.get();
			while(n > 0) {
				if(n > 1){
					out.write(127 + n);
					pixels.get(pixel, 0, 3);
					out.write(pixel);
					n = pixelNumber.get();
				} else {
					int m = 0;
					while (n == 1 && m < 128) {
						n = pixelNumber.get();
						m++;
					}
					out.write(m - 1);
					for (int i = 0; i < m; i++) {
						pixels.get(pixel, 0, 3);
						out.write(pixel);
					}
				}
			}
			
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void errorCheck() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		int error = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		
		switch (error) {
		case GL_FRAMEBUFFER_COMPLETE:
			System.out.println("GL_FRAMEBUFFER_COMPLETE");
			return;
		case GL_FRAMEBUFFER_UNDEFINED:
			System.out.println("GL_FRAMEBUFFER_UNDEFINED");
			return;
		case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
			System.out.println("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			return;
		case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
			System.out.println("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			return;
		case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
			System.out.println("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
			return;
		case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
			System.out.println("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
			return;
		case GL_FRAMEBUFFER_UNSUPPORTED:
			System.out.println("GL_FRAMEBUFFER_UNSUPPORTED");
			return;
		case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE:
			System.out.println("GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE");
			return;
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	protected void setClearColor(float r, float g, float b) {
		clearColor[0] = r;
		clearColor[1] = g;
		clearColor[2] = b;
	}
	
	public void destroy(){
		glDeleteFramebuffers(fbo);
		for(int i = 0; i < tId.length; i++) {
			tId[i].destroy();
		}
		mesh.destroy();
	}
}