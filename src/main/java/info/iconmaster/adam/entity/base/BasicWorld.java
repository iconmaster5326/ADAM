package info.iconmaster.adam.entity.base;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import info.iconmaster.adam.entity.Chunk;
import info.iconmaster.adam.entity.World;
import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.WorldCoord;

public class BasicWorld implements World {
	public BasicWorld(String name, double mass, double radius) {
		super();
		this.mass = mass;
		this.name = name;
		this.radius = radius;
	}

	double mass;
	@Override
	public double getMass() {
		return mass;
	}

	String name;
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}

	Object parent;
	@Override
	public Object getParent() {
		return parent;
	}
	@Override
	public void setParent(Object entity) {
		parent = entity;
	}

	double radius;
	@Override
	public double getRadius() {
		return radius;
	}
	
	HashMap<ChunkCoord, Chunk> chunks = new HashMap<>();
	@Override
	public Map<ChunkCoord, Chunk> getChunks() {
		return chunks;
	}
	@Override
	public Chunk generateChunk(ChunkCoord coord) {
		Chunk ch = new Chunk(this, coord);
		ch.ground = new BasicEntity("dirt", new WorldCoord(100, 10, 100), 50, Color.GREEN);
		return ch;
	}

	
}
