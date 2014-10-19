package com.iconmaster.adam;

import java.util.Random;

/**
 *
 * @author iconmaster
 */
public abstract class Attack {
	public String name;
	public BodyPart part;
	public Random random = new Random();
	
	public Attack(BodyPart part) {
		this.part = part;
	}
	
	public abstract DamageResult onAttack(BodyPart other);
}
