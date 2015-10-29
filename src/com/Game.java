package com;

import com.objects.Model;
import com.objects.PostFilter;
import com.objects.Screen;
import com.objects.Terrain;
import com.shaders.Shader;
import com.utils.math.Matrix4f;
import com.utils.math.Vector4f;

public class Game extends com.Object {
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
		child.add(screen = new Screen(0.3f, 0.3f, 1.0f));
		child.add(filter);
		child.add(camera);
		child.add(m1 = new Model("Sphere"));
		child.add(m2 = new Model("Ground"));
		child.add(terrain = new Terrain("Pit"));
		child.add(light = new Light(Light.DIRECT, 10, 10, 10));
		ready = true;
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
		for (int i = 0; i < child.size(); i++)
			child.get(i).destroy();
		Shader.destroyShaders();
	}
}
