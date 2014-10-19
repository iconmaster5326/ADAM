package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;
import java.util.Random;

/**
 *
 * @author iconmaster
 */
public abstract class Attack {
	public String name;
	public Random random = new Random();
	
	public abstract DamageResult onAttack(BodyPart other);
	
	public boolean canUseAttack() {
		return true;
	}
}
