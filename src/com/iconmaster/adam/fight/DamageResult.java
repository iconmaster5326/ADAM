package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;
import com.iconmaster.adam.equip.Equipment;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author iconmaster
 */
public class DamageResult extends HashMap<BodyPart,Double> {
	public double damage = 0;
	public double bleed = 0;
	public double mitigated = 0;
	public double equipDamage = 0;
	public HashMap<Equipment,Double> damagedEquips = new HashMap<>();
	public HashMap<Equipment,Double> mitigations = new HashMap<>();
	public HashMap<BodyPart,Double> bledParts = new HashMap<>();
	
	public ArrayList<BodyPart> getParts() {
			ArrayList<BodyPart> a = new ArrayList<>();
			a.addAll(this.keySet());
			return a;
	}
}
