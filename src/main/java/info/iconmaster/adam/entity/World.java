package info.iconmaster.adam.entity;

import java.util.HashMap;
import java.util.Map;

import info.iconmaster.adam.util.Vector3;

public interface World extends Entity {
	public static class EntityMap extends HashMap<Entity, Vector3> {
		private static final long serialVersionUID = -1626718035634777215L;
		
		private final World world;
		
		public EntityMap(World world) {
			super();
			this.world = world;
		}
		
		@Override
		public Vector3 put(Entity key, Vector3 value) {
			key.setParent(world);
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
			key.setParent(world);
			return super.putIfAbsent(key, value.mod(world.getRadius()));
		}
		
		@Override
		public boolean containsValue(Object value) {
			if (value instanceof Vector3) {
				value = ((Vector3)value).mod(world.getRadius());
			}
			return super.containsValue(value);
		}
	}
	
	public double getRadius();
	@Override
	public default Vector3 getSize() {
		double r = getRadius();
		return new Vector3(r, r, r);
	}
	
	public EntityMap entities();
}
