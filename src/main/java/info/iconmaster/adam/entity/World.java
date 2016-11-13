package info.iconmaster.adam.entity;

import java.util.Map;

import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.Vector3;

public interface World extends Entity {
	public double getRadius();
	@Override
	public default Vector3 getSize() {
		double r = getRadius();
		return new Vector3(r, r, r);
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
}
