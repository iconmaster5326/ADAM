package info.iconmaster.adam.entity;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import info.iconmaster.adam.util.WorldCoord;

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
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Joint other = (Joint) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			if (type != other.type)
				return false;
			return true;
		}
	}
	
	public static class PartsList extends AbstractCollection<Entity> implements List<Entity> {
		private ArrayList<Entity> a = new ArrayList<>();
		private Assembly assembly;
		
		public PartsList(Assembly assembly) {
			super();
			this.assembly = assembly;
		}
		
		@Override
		public void add(int index, Entity element) {
			a.add(index, element);
			element.setParent(assembly);
		}
		
		@Override
		public boolean add(Entity element) {
			element.setParent(assembly);
			return a.add(element);
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Entity> c) {
			for (Entity e : c) {
				e.setParent(assembly);
			}
			return a.addAll(index, c);
		}
		
		@Override
		public Entity get(int index) {
			return a.get(index);
		}

		@Override
		public int indexOf(Object o) {
			return a.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o) {
			return a.lastIndexOf(o);
		}

		@Override
		public ListIterator<Entity> listIterator() {
			return a.listIterator();
		}

		@Override
		public ListIterator<Entity> listIterator(int index) {
			return a.listIterator(index);
		}

		@Override
		public Entity remove(int index) {
			a.get(index).setParent(null);
			return a.remove(index);
		}

		@Override
		public Entity set(int index, Entity element) {
			a.get(index).setParent(null);
			element.setParent(assembly);
			return a.set(index, element);
		}

		@Override
		public List<Entity> subList(int fromIndex, int toIndex) {
			return a.subList(fromIndex, toIndex);
		}

		@Override
		public Iterator<Entity> iterator() {
			return a.iterator();
		}

		@Override
		public int size() {
			return a.size();
		}
	}
	
	public static class JointsMap {
		private HashMap<Entity, List<Joint>> m = new HashMap<>();
		
		public boolean has(Entity e) {
			return m.containsKey(e);
		}
		
		public List<Joint> get(Entity e) {
			return m.get(e);
		}
		
		public void add(Joint j) {
			m.putIfAbsent(j.from, new ArrayList<>());
			m.get(j.from).add(j);
			
			j = new Joint(j.to, j.type, j.from);
			m.putIfAbsent(j.from, new ArrayList<>());
			m.get(j.from).add(j);
		}
		
		public void remove(Joint j) {
			List<Joint> js = m.getOrDefault(j.from, new ArrayList<>());
			js.remove(j);
			
			j = new Joint(j.to, j.type, j.from);
			js = m.getOrDefault(j.from, new ArrayList<>());
			js.remove(j);
		}
	}
	
	public PartsList getParts();
	public JointsMap getJoints();
	
	default WorldCoord getSubassemblySize(Entity excluding, Entity part) {
		WorldCoord s = part.getSize();
		double x = s.x, y = s.y, z = s.z;
		
		if (getJoints().has(part)) {
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
		
		return new WorldCoord(x, y, z);
	}
	
	@Override
	public default WorldCoord getSize() {
		double maxX = 0, maxY = 0, maxZ = 0;
		
		for (Entity part : getParts()) {
			WorldCoord size = getSubassemblySize(null, part);
			
			maxX = Math.max(maxX, size.x);
			maxY = Math.max(maxY, size.y);
			maxZ = Math.max(maxZ, size.z);
		}
		
		return new WorldCoord(maxX, maxY, maxZ);
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
