package com;

import static org.lwjgl.opengl.GL11.*;

import com.objects.*;
import com.shaders.*;
import com.utils.math.*;

public class Game extends Loadable {
	Screen window = new Screen(1.0f, 0.3f, 0.3f);
	ScreenMS screen = new ScreenMS(0.3f, 0.3f, 1.0f);
	Model m1 = new Model();
	Model m2 = new Model();
	Terrain terrain = new Terrain();
	Light light = null;

	public Game() {
		startThread();
	}

	protected void thread() {
		new Camera();
		light = new Light(Light.DIRECT, 10, 10, 10);
		m1 = new Model("Sphere");
		m2 = new Model("Ground");
		terrain = new Terrain("Pit");
	}
	
	public void load() {
		float f = loadPercent();
		glClearColor(f, f, f, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		screen.load();
		m1.load();
		m2.load();
		terrain.load();
	}
	
	public void render() {
		Matrix4f viewMatrix = Camera.getActive().getViewMatrix();
		Matrix4f projectionMatrix = Camera.getActive().getProjectionMatrix();
		Vector4f diffuseColor = light.getDiffuseColor();
		Vector4f ambientColor = light.getAmbientColor();
		Vector4f specularColor = light.getSpecularColor();
		Vector4f lightPosition = light.getPosition(viewMatrix);

		Shader.setVPMatrices(viewMatrix, projectionMatrix);
		Shader.setLight(diffuseColor, ambientColor, specularColor, lightPosition);
		
		//window.drawTo();
		screen.drawTo();
		m1.render();
		//m2.render();
		terrain.render();
		
		window.drawTo();
		screen.render();
	}
	
	public float loadPercent() {
		return (m1.loadPercent() + m2.loadPercent() + terrain.loadPercent() + screen.loadPercent()) / 4;
	}
	
	public boolean isLoaded() {
		return m1.isLoaded() && m2.isLoaded() && terrain.isLoaded() && screen.isLoaded();
	}
	
	public void destroy() {
		m1.destroy();
		m2.destroy();
		screen.destroy();
		terrain.destroy();
		Shader.destroyShaders();
	}
}
