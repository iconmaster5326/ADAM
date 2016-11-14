package info.iconmaster.adam.entity;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

import info.iconmaster.adam.entity.Assembly.JointType;
import info.iconmaster.adam.game.AdamGame;
import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.WorldCoord;

public interface World extends Entity {
	public static final int VEC3_TO_CHUNK_SCALE = 10;
	
	public double getRadius();
	@Override
	public default WorldCoord getSize() {
		double r = getRadius();
		return new WorldCoord(r, r, r);
	}
	
	public Map<ChunkCoord, Chunk> getChunks();
	public Chunk generateChunk(ChunkCoord coord);
	
	public default Chunk getChunk(ChunkCoord coord) {
		if (getChunks().containsKey(coord)) {
			return getChunks().get(coord);
		} else {
			Chunk ch = generateChunk(coord);
			getChunks().put(coord, ch);
			return ch;
		}
	}
	public default Chunk getChunk(WorldCoord pos) {
		return getChunk(new ChunkCoord((long) (pos.x/VEC3_TO_CHUNK_SCALE), (long) (pos.z/VEC3_TO_CHUNK_SCALE)));
	}
	
	public default void move(Entity e, WorldCoord moveTo) {
		Chunk oldChunk = e.getParentWithClass(Chunk.class);
		Chunk newChunk = getChunk(moveTo);
		
		if (oldChunk == newChunk) {
			oldChunk.entities.put(e, moveTo);
		} else {
			oldChunk.entities.remove(e);
			newChunk.entities.put(e, moveTo);
		}
	}
	
	@Override
	public default void draw(AdamGame game, Graphics g, int x, int y, double pixPerUnit, JointType side) {
		g.fillRect((int) (x-getRadius()/2), (int) (y-getRadius()/2), (int) getRadius(), (int) getRadius()); 
	}
}
