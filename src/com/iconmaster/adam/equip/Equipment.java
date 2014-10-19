package com.iconmaster.adam.equip;

import com.iconmaster.adam.body.BodyPart;
import com.iconmaster.adam.fight.Attack;
import java.util.ArrayList;

/**
 *
 * @author iconmaster
 */
public class Equipment {
	public String name;
	public EquipMatch primaryMatch;
	public EquipMatch[] matches;
	public double slotRoom = 0;
	public ArrayList<Attack> attacks = new ArrayList<>();
	
	public BodyPart attachedTo;
	public boolean wielded = false;
	
	public double damage=0;
	public double maxDamage=20;
	public double damageChance=.2;
	public double damageRate = .2;
	public double mitigationBase=0;
	public double mitigationRate=1;
	public String damagedString = "damaged";
	public String brokenString = "broken";
	
	public ArrayList<BodyPart> canEquip(BodyPart being) {
		ArrayList<BodyPart> a = new ArrayList<>();
		if (primaryMatch.term.equals(being.type)) {
			for (EquipMatch match : matches) {
				ArrayList<BodyPart> parts = being.findAllParts(match.term);
				if (match.oneOrMore) {
					if (parts.isEmpty()) {
						return a;
					}
				}
				else if (match.zeroOrMore) {
					
				} else if (match.zeroOrOne) {
					if (parts.size()>1) {
						return a;
					}
				} else {
					if (parts.size()!=1) {
						return a;
					}
				}
			}
			
			if (being.getSlotRoom()>=slotRoom) {
				a.add(being);
			}
		} else {
			for (ArrayList<BodyPart> layer : being.layers) {
				for (BodyPart part : layer) {
					a.addAll(canEquip(part));
				}
			}
		}
		return a;
	}
	
	public void equip(BodyPart part) {
		unequip();
		attachedTo = part;
		part.getRootPart().equips.add(this);
	}
	
	public void unequip() {
		if (attachedTo!=null) {
			attachedTo.getRootPart().equips.remove(this);
			attachedTo = null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
	
	public double getPercentDamaged() {
		return damage/maxDamage;
	}
	
	public double mitigate(double amount) {
		return amount-amount*(mitigationBase*((1-getPercentDamaged())*mitigationRate));
	}
	
	public void damage(double amount) {
		damage+=amount;
	}
}
