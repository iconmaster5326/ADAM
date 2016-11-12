package info.iconmaster.adam.entity.base;

import info.iconmaster.adam.entity.Entity;
import info.iconmaster.adam.util.Vector3;

public class BasicEntity implements Entity {
	
	public BasicEntity(String name, Vector3 size, double mass) {
		super();
		this.size = size;
		this.mass = mass;
		this.name = name;
	}

	Vector3 size;
	@Override
	public Vector3 getSize() {
		return size;
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

}
