package com.entity.texture;

public class TileMap extends Material{
	private int tiles;
	
	public TileMap() {
		
	}
	
	public TileMap(String path, int tiles) {
		this.path = path;
		this.tiles = tiles;
		startThread();
	}
	
	protected void thread() {
		child.add(diffuseMap = new TextureArray(0, path + "_d.tga", tiles, true));
		child.add(normalMap = new TextureArray(1, path +  "_n.tga", tiles, false));
		ready = true;
	}

}
