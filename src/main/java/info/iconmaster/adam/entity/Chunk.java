package info.iconmaster.adam.entity;

import java.util.HashMap;
import java.util.Map;

import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.WorldCoord;

public class Chunk {
	public final ChunkCoord pos;
	public final World world;
	public Entity ground = null;
	public double height = 0.0;
	
	public final Map<Entity, WorldCoord> entities = new HashMap<Entity, WorldCoord>() {
		private static final long serialVersionUID = -1626718035634777215L;
		
		@Override
		public WorldCoord put(Entity key, WorldCoord value) {
			key.setParent(Chunk.this);
			return super.put(key, value.mod(world.getRadius()));
		}
		
		@Override
		public void putAll(Map<? extends Entity, ? extends WorldCoord> m) {
			for (Map.Entry<? extends Entity, ? extends WorldCoord> entry : m.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}
		
		@Override
		public WorldCoord putIfAbsent(Entity key, WorldCoord value) {
			if (!containsKey(key)) key.setParent(Chunk.this);
			return super.putIfAbsent(key, value.mod(world.getRadius()));
		}
		
		@Override
		public boolean containsValue(Object value) {
			if (value instanceof WorldCoord) {
				value = ((WorldCoord)value).mod(world.getRadius());
			}
			return super.containsValue(value);
		}
		
		@Override
		public WorldCoord remove(Object key) {
			if (key instanceof Entity && containsKey(key)) {
				((Entity)key).setParent(null);
			}
			return super.remove(key);
		};
		
		@Override
		public boolean remove(Object key, Object value) {
			if (key instanceof Entity && containsKey(key) && get(key) == value) {
				((Entity)key).setParent(null);
			}
			return super.remove(key, value);
		};
		
		@Override
		public void clear() {
			for (Entity e : keySet()) {
				e.setParent(null);
			}
			super.clear();
		};
	};
	
	public Chunk(World world, ChunkCoord pos) {
		this.pos = pos;
		this.world = world;
	}
}
