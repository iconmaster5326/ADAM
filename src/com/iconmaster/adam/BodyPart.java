package com.iconmaster.adam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
	public BodyPart parent;
	
	public double size = 0;
	public double density = 0;
	
	public double damage = 0;
	public double maxDamage = 10;
	public double destructionDamage = 20;
	public double hitChance = -1;
	public String injuryString = "injured";

//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder("[");
//		sb.append(type).append("(").append(Double.toString(size)).append("):");
//		if (!layers.isEmpty()) {
//			for (ArrayList<BodyPart> layer : layers) {
//				sb.append("<");
//				for (BodyPart part : layer) {
//					sb.append(part);
//					sb.append(" ");
//				}
//				sb.append(">");
//			}
//		}
//		sb.append("]");
//		return sb.toString();
//	}
	
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
	
	public void damage(double amount, double punch) {
		Random random = new Random();
		if (!layers.isEmpty()) {
			for (int i=0;i<layers.size();i++) {
				double skipChance = 0;
				ArrayList<BodyPart> layer = layers.get(i);
				for (BodyPart part : layer) {
					skipChance += part.getRelativeDamage();
				}
				skipChance/=layer.size();
				skipChance=.5*punch+(.5-.5*punch)*skipChance;
				if (BodyPart.isLayerDestroyed(layer)) {
					skipChance = 1;
				} else if (i==layers.size()-1) {
					skipChance = 0;
				}
				if (random.nextDouble()>skipChance) {
					HashMap<BodyPart,Double> map = new HashMap<>();
					int n = random.nextInt(layer.size())+1;
					for (BodyPart part : layer) {
						map.put(part, part.hitChance==-1?part.size:part.hitChance);
					}
					RandomUtils.rescaleMap(map);
					for (int j=0;j<n;j++) {
						BodyPart part = RandomUtils.getWeightedRandom(map,random);
						if (part!=null) {
							part.damage(amount/n);
						}
					}
					break;
				}
			}
		} else {
			System.out.println(name+"("+parent.name+") got damaged by "+amount);
			damage += amount;
		}
	}

	public void damage(double amount) {
		damage(amount,.2);
	}
	
	public int getNumAttachedParts() {
		int sum = 0;
		for (ArrayList<BodyPart> layer : layers) {
			sum+=layer.size();
		}
		return sum;
	}
	
	public double getRelativeDamage() {
		if (!layers.isEmpty()) {
			int sum = getNumAttachedParts();
			double dmg = 0;
			for (ArrayList<BodyPart> layer : layers) {
				for (BodyPart part : layer) {
					dmg += part.getRelativeDamage();
				}
			}
			return dmg/sum;
		} else {
			return damage/maxDamage;
		}
	}
	
	public static boolean isLayerDestroyed(ArrayList<BodyPart> a) {
		for (BodyPart part : a) {
			if (!part.isDestroyed()) {
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<BodyPart> getDamagedParts() {
		ArrayList<BodyPart> a = new ArrayList<>();
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				if (part.layers.isEmpty()) {
					if (part.damage>0) {
						a.add(part);
					}
				} else {
					a.addAll(part.getDamagedParts());
				}
			}
		}
		return a;
	}

	private boolean isDestroyed() {
		if (!layers.isEmpty()) {
			for (ArrayList<BodyPart> layer : layers) {
				if (!isLayerDestroyed(layer)) {
					return false;
				}
			}
			return true;
		} else {
			return damage>=destructionDamage;
		}
	}
	
	public int getLayerOn() {
		if (parent==null) {
			return -1;
		} else {
			int i = 0;
			for (ArrayList<BodyPart> layer : parent.layers) {
				if (layer.contains(this)) {
					return i;
				}
				i++;
			}
			return -1;
		}
	}
}
