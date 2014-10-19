package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;
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
