package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;

/**
 *
 * @author iconmaster
 */
public class BasicAttack extends Attack {
	public BodyPart part;
	public double minimum;
	public double variance;
	public double punch;

	public BasicAttack(AttackFactory factory, BodyPart part) {
		this.part = part;
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
		return part.getRelativeDamage()<1;
	}
}
