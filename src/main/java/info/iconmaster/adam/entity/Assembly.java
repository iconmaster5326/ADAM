package info.iconmaster.adam.entity;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.adam.util.Vector3;

public interface Assembly extends Entity {
	public static enum JointType {
		TOP,
		BOTTOM,
		FRONT,
		BACK,
		LEFT,
		RIGHT,
		INSIDE;
	}
	
	public static class Joint {
		public Entity from;
		public JointType type;
		public Entity to;
		
		public Joint(Entity from, JointType type, Entity to) {
			super();
			this.from = from;
			this.type = type;
			this.to = to;
		}
	}
	
	public List<Entity> getParts();
	public Map<Entity, List<Joint>> getJoints();
	
	default Vector3 getSubassemblySize(Entity excluding, Entity part) {
		Vector3 s = part.getSize();
		double x = s.x, y = s.y, z = s.z;
		
		if (getJoints().containsKey(part)) {
			EnumMap<JointType, Double> extents = new EnumMap<>(JointType.class);
			
			for (Joint j : getJoints().get(part)) {
				if (j.to != excluding) {
					switch (j.type) {
						case LEFT:
						case RIGHT: {
							extents.put(j.type, Math.max(extents.getOrDefault(j.type, 0.0), getSubassemblySize(part, j.to).x));
							break;
						}
						case TOP:
						case BOTTOM: {
							extents.put(j.type, Math.max(extents.getOrDefault(j.type, 0.0), getSubassemblySize(part, j.to).y));
							break;
						}
						case FRONT:
						case BACK: {
							extents.put(j.type, Math.max(extents.getOrDefault(j.type, 0.0), getSubassemblySize(part, j.to).z));
							break;
						}
						default: {}
					}
				}
			}
			
			x = extents.getOrDefault(JointType.LEFT, 0.0) + x + extents.getOrDefault(JointType.RIGHT, 0.0);
			y = extents.getOrDefault(JointType.TOP, 0.0) + y + extents.getOrDefault(JointType.BOTTOM, 0.0);
			z = extents.getOrDefault(JointType.FRONT, 0.0) + z + extents.getOrDefault(JointType.BACK, 0.0);
		}
		
		return new Vector3(x, y, z);
	}
	
	@Override
	public default Vector3 getSize() {
		double maxX = 0, maxY = 0, maxZ = 0;
		
		for (Entity part : getParts()) {
			Vector3 size = getSubassemblySize(null, part);
			
			maxX = Math.max(maxX, size.x);
			maxY = Math.max(maxY, size.y);
			maxZ = Math.max(maxZ, size.z);
		}
		
		return new Vector3(maxX, maxY, maxZ);
	}
	
	@Override
	public default double getMass() {
		int sum = 0;
		for (Entity part : getParts()) {
			sum += part.getMass();
		}
		return sum;
	}
}
