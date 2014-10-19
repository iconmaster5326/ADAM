package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;
import com.iconmaster.adam.equip.Equipment;

/**
 *
 * @author iconmaster
 */
public class AttackFactory {
	public String name;
	public double minimum;
	public double variance;
	public double punch;

	public Attack newAttack(BodyPart part) {
		return new BasicAttack(this, part);
	}
	
	public Attack newAttack(Equipment part) {
		return new EquipAttack(this, part);
	}
}
