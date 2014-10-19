package com.iconmaster.adam.fight;

import com.iconmaster.adam.BodyPart;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author iconmaster
 */
public class DamageResult extends HashMap<BodyPart,Double> {
	public double damage = 0;
	public double bleed = 0;
	public HashMap<BodyPart,Double> bledParts = new HashMap<BodyPart,Double>();
	
	public ArrayList<BodyPart> getParts() {
			ArrayList<BodyPart> a = new ArrayList<>();
			a.addAll(this.keySet());
			return a;
	}
}
