package com;

import static org.lwjgl.opengl.GL11.*;

import com.objects.*;
import com.shaders.*;
import com.utils.math.*;

public class Game extends Loadable {
	Screen screen = new Screen();
	Camera camera = new Camera();
	Model m1 = new Model();
	Model m2 = new Model();
	Terrain terrain = new Terrain();
	Light light = new Light();
	PostFilter filter = new PostFilter();
	
	public Game() {
		startThread();
	}

	protected void thread() {
		subcomp.add(screen = new Screen(0.3f, 0.3f, 1.0f));
		subcomp.add(filter);
		subcomp.add(camera);
		subcomp.add(m1 = new Model("Sphere"));
		subcomp.add(m2 = new Model("Ground"));
		subcomp.add(terrain = new Terrain("Pit"));
		subcomp.add(light = new Light(Light.DIRECT, 10, 10, 10));
		ready = true;
	}
	
	public void load() {
		float f = readyPercent();
		glClearColor(f, f, f, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if(ready && !loaded) {
			loaded = true;
		}
		loadSubcomp();
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
		
		screen.makeActive();
		m1.render();
		//m2.render();
		terrain.render();
		
		screen.drawTo(filter);
		//filter.apply("BlurX");
		//filter.apply("BlurY");
		filter.drawTo();
	}
	
	public void destroy() {
		for (int i = 0; i < subcomp.size(); i++)
			subcomp.get(i).destroy();
		Shader.destroyShaders();
	}
}
