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

	@Override
	public String toString() {
		return name;
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
	
	public DamageResult damage(DamageResult res, double amount, double punch) {
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
							part.damage(res,amount/n,punch);
						}
					}
					break;
				}
			}
		} else {
			//System.out.println(name+"("+parent.name+") got damaged by "+amount);
			damage += amount;
			res.put(this, res.getOrDefault(this, 0d)+amount);
		}
		return res;
	}

	public DamageResult damage(double amount) {
		return damage(new DamageResult(),amount,.2);
	}
	
	public DamageResult damage(double amount, double punch) {
		return damage(new DamageResult(),amount,punch);
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
	
	public BodyPart getRootPart() {
		BodyPart part = this;
		while (part.parent!=null) {
			part = part.parent;
		}
		return part;
	}
	
	public static BodyPart getLowestCommonPart(BodyPart part1, BodyPart part2) {
		if (part1==null || part2==null) {
			return null;
		}
		if (part1==part2) {
			return part1;
		}
		if (part1.parent==part2) {
			return part2;
		}
		if (part2.parent==part1) {
			return part1;
		}
		return getLowestCommonPart(part1.parent==null?part1:part1.parent,part2.parent==null?part2:part2.parent);
	}
	
	public static BodyPart getLowestCommonPart(ArrayList<BodyPart> parts) {
		if (parts.isEmpty()) {
			return null;
		}
		if (parts.size()==1) {
			return parts.get(0);
		}
		ArrayList<BodyPart> a = new ArrayList<>();
		BodyPart testPart = parts.get(0);
		for (int i=1;i<parts.size();i++) {
			BodyPart part = getLowestCommonPart(testPart, parts.get(i));
			a.add(part);
		}
		ArrayList<BodyPart> a2 = new ArrayList<>();
		for (BodyPart part : a) {
			if (!a2.contains(part)) {
				a2.add(part);
			}
		}
		return getLowestCommonPart(a2);
	}
	
	public BodyPart getContainerPart() {
		return layers.isEmpty()?parent:this;
	}
	
	public boolean contains(BodyPart part) {
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part2 : layer) {
				if (part2==part || part2.contains(part)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<BodyPart> getAttachedParts() {
		ArrayList<BodyPart> a = new ArrayList<>();
		for (ArrayList<BodyPart> layer : layers) {
			a.addAll(layer);
		}
		return a;
	}
	
	public ArrayList<BodyPart> getSubpartSublist(ArrayList<BodyPart> parts) {
		ArrayList<BodyPart> a = new ArrayList<>();
		for (BodyPart part : parts) {
			if (this.contains(part)) {
				a.add(part);
			}
		}
		return a;
	}
	
	public boolean containsAny(ArrayList<BodyPart> parts) {
		return !getSubpartSublist(parts).isEmpty();
	}
	
	public boolean containsAll(ArrayList<BodyPart> parts) {
		return getSubpartSublist(parts).size()==parts.size();
	}
	
	public BodyPart getPart(String name) {
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				if (part.name.equals(name)) {
					return part;
				}
			}
		}
		return null;
	}
}
