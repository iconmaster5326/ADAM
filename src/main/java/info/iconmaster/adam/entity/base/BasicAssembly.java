package info.iconmaster.adam.entity.base;

import info.iconmaster.adam.entity.Assembly;

public class BasicAssembly implements Assembly {

	public BasicAssembly(String name) {
		super();
		this.name = name;
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

	Assembly.PartsList parts = new PartsList(this);
	@Override
	public PartsList getParts() {
		return parts;
	}

	Assembly.JointsMap joints = new JointsMap();
	@Override
	public JointsMap getJoints() {
		return joints;
	}
}
