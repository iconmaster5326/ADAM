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
	
	public double size = 0;
	public double density = 0;
	public double usedRoom = 0;
	public double maxRoom = 0;
	public ArrayList<Modification> mods = new ArrayList<>();
	
	public double damage = 0;
	public double maxDamage = 10;
	public double hitChance = -1;
	public double hitResistance = 0;

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
			int sum = 0;
			for (ArrayList<BodyPart> layer : layers) {
				sum+=layer.size();
			}
//			int amt = random.nextInt(sum)+1;
//			for (int i=0;i<amt;i++) {
//				int li = random.nextInt(layers.size());
//				ArrayList<BodyPart> layer = layers.get(li);
//				int pi = random.nextInt(layer.size());
//				BodyPart part = layer.get(pi);
//				double dmg = amount/amt;
//				part.damage(dmg,punch);
//			}
			
			HashMap<BodyPart,Double> map = new HashMap<>();
			double layerProb = 1;
			for (int i=0;i<layers.size();i++) {
				ArrayList<BodyPart> layer = layers.get(i);
				for (BodyPart part : layer) {
					double prob = (1d/sum)/(layerProb*punch);
					map.put(part, prob);
				}
				layerProb /= 2;
			}
			int n = random.nextInt(sum/2)+1;
			for (int i=0;i<n;i++) {
				BodyPart part = RandomUtils.getWeightedRandom(map);
				part.damage(amount/n, punch);
			}
		} else {
			System.out.println(name+" got damaged by "+amount);
			damage += amount;
		}
	}

	public void damage(int amount) {
		damage(amount,.2);
	}
}
