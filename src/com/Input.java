package com;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.*;

import com.utils.*;
import com.utils.math.*;

@SuppressWarnings("unused")
public class Input {
	private GLFWKeyCallback keyCallback = null;
	private GLFWCursorPosCallback mousePosCallback = null;
	private GLFWMouseButtonCallback mouseButtonCallback = null;
	private GLFWCursorEnterCallback mouseEnterCallback = null;
	private GLFWWindowFocusCallback focusCallback = null;
	
	private boolean[] keyboardKeyBuffer = new boolean[512];
	private boolean[] keyboardKeyDownBuffer = new boolean[512];
	private boolean[] keyboardKeyUpBuffer = new boolean[512];
	
	public Input(long window) {
		
		keyCallback = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                    glfwSetWindowShouldClose(window, GL_TRUE);
				if(action == GLFW_PRESS){
					keyboardKeyBuffer[key] = true;
					keyboardKeyDownBuffer[key] = true;
				}
				if(action == GLFW_RELEASE)
					keyboardKeyBuffer[key] = false;
					keyboardKeyUpBuffer[key] = true;
            }
        };
        
        mousePosCallback = new GLFWCursorPosCallback() {
        	public void invoke(long window, double xpos, double ypos) {
                
            }
        };
        
        mouseButtonCallback = new GLFWMouseButtonCallback() {
        	public void invoke(long window, int button, int action, int mods) {
        		
            }
        };
        
        mouseEnterCallback = new GLFWCursorEnterCallback() {
        	public void invoke(long window, int entered) {
        		
            }
        };
        
        focusCallback  = new GLFWWindowFocusCallback() {
			public void invoke(long window, int focused) {
				
			}
		};
        
        glfwSetCallback(window, keyCallback);
        glfwSetCallback(window, mousePosCallback);
        glfwSetCallback(window, mouseButtonCallback);
        glfwSetCallback(window, mouseEnterCallback);
        glfwSetCallback(window, focusCallback);
	}
	
	public void logicLoop() {
		
		float sec = (float) Time.getDelta() / 1000;
		
		if (isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			sec *= 2;
		}
		if (isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
			sec /= 2;
		}
		
		float zRotDelta = (float) Math.PI * sec ;
		float xyRotDelta = (float) Math.PI / 2 * sec;
		float scaleDelta = 16 * sec;
		
		if (atKeyDown(GLFW_KEY_X)) {
			Camera.getActive().destroy();
			new Camera();
		}
		
		Vector3f cameraStrafe = Camera.getActive().getStrafe();
		Vector3f cameraRotation = Camera.getActive().getRotation();
		Vector3f cameraPivot = Camera.getActive().getPivot();

		if (isKeyDown(GLFW_KEY_UP)) {
			cameraRotation.x += xyRotDelta;
		}
		if (isKeyDown(GLFW_KEY_DOWN)) {
			cameraRotation.x -= xyRotDelta;
		}
		if (isKeyDown(GLFW_KEY_LEFT)) {
			cameraRotation.z += zRotDelta;
		}
		if (isKeyDown(GLFW_KEY_RIGHT)) {
			cameraRotation.z -= zRotDelta;
		}
		if (isKeyDown(GLFW_KEY_COMMA)) {
			cameraRotation.y += xyRotDelta;
		}
		if (isKeyDown(GLFW_KEY_PERIOD)) {
			cameraRotation.y -= xyRotDelta;
		}
		if (isKeyDown(GLFW_KEY_KP_ADD)) {
			cameraStrafe.y += scaleDelta;
		}
		if (isKeyDown(GLFW_KEY_KP_SUBTRACT)) {
			cameraStrafe.y -= scaleDelta;
		}
		
		Camera.getActive().setStrafe(cameraStrafe);
		Camera.getActive().setRotation(cameraRotation);
		Camera.getActive().setPivot(cameraPivot);
		
		if (atKeyDown(GLFW_KEY_F12)) {
			int time[] = Time.get();
			System.out.println(time[0] + "/" + time[1] + "/" + time[2] + "\n"
					+ time[3] + ":" + time[4] + ":" + time[5] + "." + time[6] + "\n");
		}
		
		keyboardKeyUpBuffer = new boolean[512];
		keyboardKeyDownBuffer = new boolean[512];
	}
	
	private boolean isKeyDown(int n) {
		return keyboardKeyBuffer[n];
	}
	private boolean atKeyDown(int n) {
		return keyboardKeyDownBuffer[n];
	}
	
	private boolean atKeyUp(int n) {
		return keyboardKeyUpBuffer[n];
	}
	
	public void destroy() {
		keyCallback.release();
		mousePosCallback.release();
		mouseButtonCallback.release();
		mouseEnterCallback.release();
	}
}
