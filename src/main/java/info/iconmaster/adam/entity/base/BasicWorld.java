package info.iconmaster.adam.entity.base;

import info.iconmaster.adam.entity.World;

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

	EntityMap entities = new EntityMap(this);
	@Override
	public EntityMap entities() {
		return entities;
	}
}
