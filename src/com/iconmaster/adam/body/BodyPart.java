package com.iconmaster.adam.body;

import com.iconmaster.adam.equip.Equipment;
import com.iconmaster.adam.fight.Attack;
import com.iconmaster.adam.fight.DamageResult;
import com.iconmaster.adam.fight.HealResult;
import com.iconmaster.adam.fight.IsAliveResult;
import com.iconmaster.adam.fight.TickResult;
import com.iconmaster.adam.util.RandomUtils;
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
	public PronounSet pronouns = new PronounSet("it", "it", "its");
	
	public double size = 0;
	public double density = 0;
	
	public double damage = 0;
	public double maxDamage = 10;
	public double destructionDamage = 20;
	public double hitChance = -1;
	public String injuryString = "injured";
	public String removalString = "destroyed";
	public boolean essential = false;
	
	public double blood = 0;
	public double bleed = 0;
	public double bleedChance = .2;
	public double bleedRate = .8;
	
	public ArrayList<Attack> attacks = new ArrayList<>();
	public ArrayList<Equipment> equips = new ArrayList<>();

	@Override
	public String toString() {
		if (parent==null) {
			return "<"+name+">";
		}
		return getLocation();
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

	public double getMass() {
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
					int n = random.nextInt(((int)Math.abs(amount/getThreshold()))*2+1)+1;
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
			damage += amount;
			res.damage += amount;
			if (isDestroyed()) {
				bleed = 0;
			} else {
				if (random.nextDouble()<bleedChance) {
					res.bledParts.put(this, res.getOrDefault(this, 0d)+bleedRate*amount);
					res.bleed += bleedRate*amount;
					bleed += bleedRate*amount;
				}
			}
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
	
	public double getRelativeDestroyDamage() {
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
			return damage/destructionDamage;
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
					if (part.damage>0 || part.bleed > 0) {
						a.add(part);
					}
				} else {
					a.addAll(part.getDamagedParts());
				}
			}
		}
		return a;
	}

	public boolean isDestroyed() {
		if (!layers.isEmpty()) {
			for (ArrayList<BodyPart> layer : layers) {
				if (!isLayerDestroyed(layer)) {
					return false;
				}
			}
			return true;
		} else {
			return damage>=destructionDamage && blood>0;
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
	
	public BodyPart getPartRef(String ref) {
		String[] terms = ref.replace("_", " ").split("\\/");
		BodyPart part = this;
		for (String term : terms) {
			BodyPart part2 = part.getPart(term);
			if (term.isEmpty()) {
				
			} else if ("..".equals(term)) {
				part = part.parent;
			} else if ("~".equals(term)) {
				part = part.getRootPart();
			} else if (part2==null) {
				return null;
			} else {
				part = part2;
			}
		}
		return part;
	}
	
	public BodyPart findPart(String name) {
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				if (part.name.matches(name)) {
					return part;
				}
				BodyPart part2 = part.findPart(name);
				if (part2!=null) {
					return part2;
				}
			}
		}
		return null;
	}
	
	public ArrayList<BodyPart> findAllParts(String name) {
		ArrayList<BodyPart> a = new ArrayList<>();
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				if (part.name.matches(name) || part.type.matches(name)) {
					a.add(part);
				}
				a.addAll(part.findAllParts(name));
			}
		}
		return a;
	}
	
	public String getLocation() {
		StringBuilder sb = new StringBuilder("~");
		BodyPart part = this;
		while (part.parent!=null) {
			sb.insert(1, part.name.replace(" ", "_"));
			sb.insert(1,"/");
			part = part.parent;
		}
		return sb.toString();
	}
	
	public void clean() {
		int i = 0;
		for (ArrayList<BodyPart> layer : (ArrayList<ArrayList<BodyPart>>) layers.clone()) {
			if (layer.isEmpty()) {
				layers.remove(i);
				clean();
				return;
			}
			i++;
		}
	}
	
	public boolean isWorking() {
		BodyPart root = this.getRootPart();
		return getRelativeDamage()<1 && root.blood>0;
	}
	
	public boolean isAttached() {
		return getRelativeDestroyDamage()<1;
	}
	
	public IsAliveResult isAlive() {
		BodyPart root = this.getRootPart();
		if (root.getRelativeDestroyDamage()>1.5) {
			return new IsAliveResult(null, false, true);
		}
		if (root.blood<=0) {
			return new IsAliveResult(null, true, false);
		}
		HashMap<String,BodyPart> parts = new HashMap<>();
		HashMap<String,Boolean> working = new HashMap<>();
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				if (part.essential) {
					working.putIfAbsent(part.type, false);
					parts.putIfAbsent(part.type, part);
					if (part.isWorking()) {
						working.put(part.type, true);
						parts.put(part.type, part);
					}
				}
				if (!part.layers.isEmpty()) {
					IsAliveResult cause = part.isAlive();
					if (cause!=null) {
						return cause;
					}
				}
			}
		}
		for (String part : working.keySet()) {
			Boolean works = working.get(part);
			if (!works) {
				return new IsAliveResult(parts.get(part), false, false);
			}
		}
		return null;
	}
	
	public HealResult healAll() {
		HealResult res = new HealResult(0, 0);
		
		BodyPart root = this.getRootPart();
		if (root==this) {
			double bled = blood;
			blood = root.getMaxBlood();
			res.bloodHealed += blood-bled;
		}
		
		if (this.layers.isEmpty()) {
			double dmg = this.damage;
			this.damage = 0;
			bleed = 0;
			if (root!=this) {
				blood = 0;
			}
			res.damageHealed += dmg;
			return res;
		}
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				HealResult hr = part.healAll();
				res.damageHealed += hr.damageHealed;
				res.bloodHealed += hr.bloodHealed;
			}
		}
		return res;
	}
	
	public double getThreshold() {
		if (layers.isEmpty()) {
			return destructionDamage/5;
		} else {
			double avg = 0;
			for (ArrayList<BodyPart> layer : layers) {
				for (BodyPart part : layer) {
					avg += part.getThreshold();
				}
			}
			return avg/getNumAttachedParts();
		}
	}
	
	public double getMaxBlood() {
		if (layers.isEmpty()) {
			return size==0?10:size*100;
		} else {
			double sum = 0;
			for (ArrayList<BodyPart> layer : layers) {
				for (BodyPart part : layer) {
					sum += part.getMaxBlood();
				}
			}
			return sum;
		}
	}
	
	public boolean isBleeding() {
		if (layers.isEmpty()) {
			return bleed>0;
		} else {
			for (ArrayList<BodyPart> layer : layers) {
				for (BodyPart part : layer) {
					if (part.isBleeding()) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public TickResult tick() {
		TickResult tr = new TickResult();
		BodyPart root = this.getRootPart();
		boolean bledOut = root.blood<=0;
		tr = tick(tr);
		if (root.blood<=0 && !bledOut) {
			tr.bledOut = true;
		}
		return tr;
	}
	
	public TickResult tick(TickResult tr) {
		BodyPart root = this.getRootPart();
		if (layers.isEmpty()) {
			root.blood -= bleed;
			tr.bled += bleed;
			if (bleed>0) {
				tr.bloodLost.put(this, bleed);
			}
			if (root.blood<=0) {
				bleed *= .4;
				tr.clotted.add(this);
				if (bleed/getMaxBlood()<.05) {
					bleed = 0;
					tr.healed.add(this);
				}
			} else if (Math.random()<.2) {
				bleed *= .8;
				if (bleed/getMaxBlood()<.05) {
					bleed = 0;
					tr.healed.add(this);
				}
				tr.clotted.add(this);
			}
			return tr;
		}
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				part.tick(tr);
			}
		}
		return tr;
	}
	
	public ArrayList<Attack> getAttacks() {
		ArrayList<Attack> a = new ArrayList<>();
		if (this==this.getRootPart()) {
			for (Equipment eq : equips) {
				a.addAll(eq.attacks);
			}
		}
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				a.addAll(part.getAttacks());
			}
		}
		a.addAll(attacks);
		
		ArrayList<Attack> a2 = new ArrayList<>();
		for (Attack attk : a) {
			if (attk.canUseAttack()) {
				a2.add(attk);
			}
		}
		return a2;
	}
	
	public void applyPronouns(PronounSet ps) {
		pronouns = ps;
		for (ArrayList<BodyPart> layer : layers) {
			for (BodyPart part : layer) {
				part.applyPronouns(ps);
			}
		}
	}
	
	public double getSlotRoom() {
		return 1-getSlotRoomUsed();
	}
	
	public double getSlotRoomUsed() {
		double room = 0;
		for (Equipment eq : getRootPart().equips) {
			if (eq.attachedTo==this) {
				room += eq.slotRoom;
			}
		}
		return room;
	}
}
