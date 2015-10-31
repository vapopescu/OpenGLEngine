package com;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.*;
import java.nio.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.shaders.*;
import com.utils.*;

/**
 * 
 * @author Vlad
 * 
 *         Class used for creating the window and GL context.
 */
public class Window {
	private static long window;
	private Input input;
	private Game game;
	private GLFWErrorCallback errorCallback;

	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("./native").getAbsolutePath());
		Window win = new Window();
		win.loop();
		win.destroy();
	}

	public Window() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_DECORATED, GL_TRUE);
		glfwWindowHint(GLFW_SAMPLES, Settings.getSamples());
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		
		int width = Settings.getWidth();
		int height = Settings.getHeight();
		
		// Create the window
		window = glfwCreateWindow(width, height, "OpenGL Engine", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		input = new Input(window);

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Set v-sync
		glfwSwapInterval(Settings.getVSync());
		// Make the window visible
		glfwShowWindow(window);
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the ContextCapabilities instance and makes the OpenGL
		// bindings available for use.
		GLContext.createFromCurrent();
		Shader.initialize();
		Settings.initialize();
		
		game = new Game();
	}

	private void loop() {
		while (!game.loadedAll()) {
			float f = Object.loadPercent();
			glClearColor(f, f, f, 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			game.load();
			
			glfwSwapBuffers(window);
		}

		glClearDepth(1.0f);
		glEnable(GL_DEPTH_TEST);
		
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		while (glfwWindowShouldClose(window) == GL_FALSE) {
			Time.update();
			input.logicLoop();
			game.render();

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

	public static void bindContext() {
		glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
	}

	public static void unbindContext() {
		glfwMakeContextCurrent(0);
	}

	private void destroy() {
		game.destroy();
		input.destroy();
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
	}
}
