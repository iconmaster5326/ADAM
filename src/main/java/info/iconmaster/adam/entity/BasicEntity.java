package info.iconmaster.adam.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BasicEntity implements Entity {
	
	public BasicEntity(String name, Color color, double size, double mass) {
		this.size = size;
		this.mass = mass;
		this.name = name;
		this.color = color;
	}
	
	double size;
	@Override
	public double getSize() {
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
	
	Color color;
	@Override
	public Color getColor() {
		return color;
	}
	
	ArrayList<EntityAttachment> atts = new ArrayList<>();
	@Override
	public List<EntityAttachment> getAttachments() {
		return atts;
	}
}
