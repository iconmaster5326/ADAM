package com.iconmaster.adam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author iconmaster
 */
public class TickResult {
	public HashMap<BodyPart,Double> bloodLost = new HashMap<>();
	public HashSet<BodyPart> bledOut = new HashSet<>();
	public HashSet<BodyPart> clotted = new HashSet<>();
	public HashSet<BodyPart> healed = new HashSet<>();
	
	public double bled;
	
	public ArrayList<BodyPart> getParts() {
			ArrayList<BodyPart> a = new ArrayList<>();
			a.addAll(bloodLost.keySet());
			return a;
	}
}
