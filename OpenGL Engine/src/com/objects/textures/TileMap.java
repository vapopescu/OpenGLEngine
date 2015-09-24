package com.objects.textures;

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
		diffuseMap = new TextureArray(0, path + "_d.tga", tiles, true);
		normalMap = new TextureArray(1, path +  "_n.tga", tiles, false);
	}

}
