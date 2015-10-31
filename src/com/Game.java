package com;

import com.entity.Model;
import com.entity.PostFilter;
import com.entity.Screen;
import com.entity.Terrain;
import com.shaders.Shader;
import com.utils.math.Matrix4f;
import com.utils.math.Vector3f;
import com.utils.math.Vector4f;

public class Game extends com.Object {
	public static Game handler; 
	
	private Screen screen = new Screen();
	private Camera camera = new Camera();
	private Model m1 = new Model();
	@SuppressWarnings("unused")
	private Model m2 = new Model();
	private Terrain terrain = new Terrain();
	private Light light = new Light();
	private PostFilter filter = new PostFilter();
	
	public Game() {
		handler = this;
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
	
	public void rotate_test(Vector3f vector) {
		m1.setRotation(vector);
	}
	
	public static Game get() {
		return handler;
	}
	
	public void destroy() {
		for (int i = 0; i < child.size(); i++)
			child.get(i).destroy();
		Shader.destroyShaders();
	}
}
