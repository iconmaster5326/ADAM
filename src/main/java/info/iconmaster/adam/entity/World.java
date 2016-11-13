package info.iconmaster.adam.entity;

import java.util.Map;

import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.Vector3;

public interface World extends Entity {
	public static final int VEC3_TO_CHUNK_SCALE = 10;
	
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
	public default Chunk getChunk(Vector3 pos) {
		return getChunk(new ChunkCoord((long) (pos.x/VEC3_TO_CHUNK_SCALE), (long) (pos.z/VEC3_TO_CHUNK_SCALE)));
	}
	
	public default void move(Entity e, Vector3 moveTo) {
		Chunk oldChunk = e.getParentWithClass(Chunk.class);
		Chunk newChunk = getChunk(moveTo);
		
		if (oldChunk == newChunk) {
			oldChunk.entities.put(e, moveTo);
		} else {
			oldChunk.entities.remove(e);
			newChunk.entities.put(e, moveTo);
		}
	}
}
