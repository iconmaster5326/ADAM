package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;
import com.iconmaster.adam.equip.Equipment;

/**
 *
 * @author iconmaster
 */
public class EquipAttack extends Attack {
	public Equipment eq;
	public double minimum;
	public double variance;
	public double punch;

	public EquipAttack(AttackFactory factory, Equipment eq) {
		this.eq = eq;
		this.name = factory.name;
		this.minimum = factory.minimum;
		this.variance = factory.variance;
		this.punch = factory.punch;
	}

	@Override
	public DamageResult onAttack(BodyPart other) {
		return other.damage(minimum+random.nextDouble()*variance,punch);
	}
	
	@Override
	public boolean canUseAttack() {
		return eq.attachedTo!=null && eq.attachedTo.getRelativeDamage()<1;
	}
}
