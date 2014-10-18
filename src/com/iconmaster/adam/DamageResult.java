package com.iconmaster.adam;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author iconmaster
 */
public class DamageResult extends HashMap<BodyPart,Double> {
	public HashMap<BodyPart,Double> bleed = new HashMap<BodyPart,Double>();
	
	public ArrayList<BodyPart> getParts() {
			ArrayList<BodyPart> a = new ArrayList<>();
			a.addAll(this.keySet());
			return a;
	}
}
