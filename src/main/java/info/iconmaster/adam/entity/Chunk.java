package info.iconmaster.adam.entity;

import java.util.HashMap;
import java.util.Map;

import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.Vector3;

public class Chunk {
	public final ChunkCoord pos;
	public final World world;
	public Entity ground = null;
	public double height = 0.0;
	
	public final Map<Entity, Vector3> entities = new HashMap<Entity, Vector3>() {
		private static final long serialVersionUID = -1626718035634777215L;
		
		@Override
		public Vector3 put(Entity key, Vector3 value) {
			key.setParent(Chunk.this);
			return super.put(key, value.mod(world.getRadius()));
		}
		
		@Override
		public void putAll(Map<? extends Entity, ? extends Vector3> m) {
			for (Map.Entry<? extends Entity, ? extends Vector3> entry : m.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}
		
		@Override
		public Vector3 putIfAbsent(Entity key, Vector3 value) {
			if (!containsKey(key)) key.setParent(Chunk.this);
			return super.putIfAbsent(key, value.mod(world.getRadius()));
		}
		
		@Override
		public boolean containsValue(Object value) {
			if (value instanceof Vector3) {
				value = ((Vector3)value).mod(world.getRadius());
			}
			return super.containsValue(value);
		}
		
		@Override
		public Vector3 remove(Object key) {
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
