package com.objects.textures;

import static com.utils.Utils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

import java.io.*;
import java.nio.*;

import org.lwjgl.*;

import com.*;

public class Texture extends Loadable {
	protected String path = "";
	protected int id = 0;
	protected int index = 0;
	protected int format = 0;
	protected boolean gamma = false;
	protected int width = 0, height = 0;
	
	protected ByteBuffer imageBuffer = null;
	
	protected void loadTextureTga(String textureFilename) {
		int size, pixelSize;
		byte[] header, pixel;
		boolean compression;
		try {
			File file = new File("data/" + textureFilename);
			if(!file.exists()){
				System.err.println("Could not load \"" + file.getPath() + "\"");
				width = 0;
				height = 0;
				format = 0;
				return;
			}
			
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			header = new byte[18];
			
			in.read(header, 0, 18);
			if(header[2] == 0x02) {
				compression = false;
			}
			else if(header[2] == 0x0A){
				compression = true;
			}
			else {
				in.close();
				throw new IOException();
			}
			
			width = byteToInt(header[12]) + 0x0100 * byteToInt(header[13]);
			height = byteToInt(header[14]) + 0x0100 * byteToInt(header[15]);
			
			// Check that it is 32 or 24 bit.
			if(header[16] == 0x20) {
				format = GL_BGRA;
				pixelSize = 4;
			} else if(header[16] == 0x18) {
				format = GL_BGR;
				pixelSize = 3;
			} else {
				in.close();
				throw new IOException();
			}

			// Calculate the size of the 32 or 24 bit image data.
			size = width * height * pixelSize;
			
			// Read in the targa image data.
			imageBuffer = BufferUtils.createByteBuffer(size);
			if (!compression) {
				byte image[] = new byte[size];
				in.read(image, 0, size);
				imageBuffer.put(image);
			}
			else {
				pixel = new byte[4];
				int i = 0;
				while (i < width * height) {
					in.read(header, 0, 1);
					if (header[0] >= 0) {
						for (int j = 0; j <= header[0]; j++) {
							in.read(pixel, 0, pixelSize);
							imageBuffer.put(pixel, 0, pixelSize);
							i++;
						}
					} else {
						in.read(pixel, 0, pixelSize);
						for (int j = 0; j <= header[0] + 128; j++) {
							imageBuffer.put(pixel, 0, pixelSize);
							i++;
						}
					}
				}
			}
			
			imageBuffer.flip();
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error loading \"" + textureFilename + "\"");
		}
	}
	
	public ByteBuffer getTexture() {
		return imageBuffer;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void bind() {
		bind(index);
	}
	
	public void bind(int n) {
		glActiveTexture(GL_TEXTURE0 + n);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public static void unbind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public ByteBuffer getTextureImage() {
		ByteBuffer image = BufferUtils.createByteBuffer(width * height * 3);
		glBindTexture(GL_TEXTURE_2D, id);
		int outFormat = 0;
		if (format == GL_RGB) {
			outFormat = GL_BGR;
		} else if (format == GL_DEPTH_COMPONENT) {
			outFormat = GL_DEPTH_COMPONENT;
		}
		glGetTexImage(GL_TEXTURE_2D, 0, outFormat, GL_UNSIGNED_BYTE, image);
		
		if (format == GL_DEPTH_COMPONENT) {
			ByteBuffer image2 = BufferUtils.createByteBuffer(width * height * 3);
			byte pixel;
			for(int i = 0; i < width * height; i++) {
				pixel = image.get();
				image2.put(pixel);
				image2.put(pixel);
				image2.put(pixel);
			}
			image2.flip();
			image = image2;
		}
		return image;
	}
	
	public void destroy() {
		glDeleteTextures(id);
	}
}
