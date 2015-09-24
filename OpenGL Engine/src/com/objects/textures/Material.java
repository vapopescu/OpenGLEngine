package com.objects.textures;

import com.*;

public class Material extends Loadable{
	protected String path;
	protected Texture diffuseMap = new Texture();
	protected Texture normalMap = new Texture();
	
	public Material() {

	}
	
	public Material(String path) {
		this.path = path;
		startThread();
	}

	protected void thread() {
		diffuseMap = new Texture2D(0, path + "_d.tga", true);
		normalMap = new Texture2D(1, path + "_n.tga", false);
	}
	
	public void load() {
		diffuseMap.load();
		normalMap.load();
	}
	
	public void bind() {
		diffuseMap.bind();
		normalMap.bind();
	}
	
	public float loadPercent() {
		return (diffuseMap.loadPercent() + normalMap.loadPercent()) / 2.0f;
	}
	
	public boolean isLoaded() {
		return diffuseMap.isLoaded() && normalMap.isLoaded();
	}
	
	public void destroy() {
		diffuseMap.destroy();
		normalMap.destroy();
	}

}
