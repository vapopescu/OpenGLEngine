package com;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Settings {
	private static int width = 683;
	private static int height = 384;
	private static int samples = 4;
	private static int anisotropy = 8;
	private static int vSync = 0;
	
	public static void initialize() {
		if(glfwExtensionSupported("GL_EXT_texture_filter_anisotropic") == GL_FALSE)
			anisotropy = 0;
	}
	
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static int getSamples() {
		return samples;
	}
	
	public static int getAnisotropy() {
		return anisotropy;
	}
	
	public static int getVSync() {
		return vSync;
	}
}
