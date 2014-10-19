package com.iconmaster.adam.fight;

import com.iconmaster.adam.BodyPart;

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
}
