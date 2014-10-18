package com.iconmaster.adam;

import java.util.ArrayList;

/**
 *
 * @author iconmaster
 */
public class BodyPart {
	public String type = "???";
	public String name = "???";
	public String desc = "%s";
	public boolean plural = false;
	public boolean proper = false;
	public ArrayList<ArrayList<BodyPart>> layers = new ArrayList<>();
	
	public double size = 0;
	public double density = 0;
	public double damage = 0;
	public double usedRoom = 0;
	public double maxRoom = 0;
	public ArrayList<Modification> mods = new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(type).append("(").append(Double.toString(size)).append("):");
		if (!layers.isEmpty()) {
			for (ArrayList<BodyPart> layer : layers) {
				sb.append("<");
				for (BodyPart part : layer) {
					sb.append(part);
					sb.append(" ");
				}
				sb.append(">");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public double getMass(double factor) {
		if (layers.isEmpty()) {
			return density*size*factor;
		} else {
			double sum = 0;
			for (ArrayList<BodyPart> layer : layers) {
				for (BodyPart part : layer) {
					sum += part.getMass(factor*size);
				}
			}
			return sum;
		}
	}

	double getMass() {
		return getMass(size);
	}
}
