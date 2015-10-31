package com.entity.texture;

public class Material extends com.Object{
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
		child.add(diffuseMap = new Texture2D(0, path + "_d.tga", true));
		child.add(normalMap = new Texture2D(1, path + "_n.tga", false));
		ready = true;
	}
	
	public void load() {
		diffuseMap.load();
		normalMap.load();
		loaded = true;
	}
	
	public void bind() {
		diffuseMap.bind();
		normalMap.bind();
	}
	
	public void destroy() {
		diffuseMap.destroy();
		normalMap.destroy();
	}

}
