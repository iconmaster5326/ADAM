package com.iconmaster.adam.equip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author iconmaster
 */
public class EquipFactory {
	public static HashMap<String,String> equips = new HashMap<>();
	
	public static void registerEquip(String name, String define) {
		equips.put(name, define);
	}
	
	public static Equipment generate(String name) {
		String def = equips.get(name);
		Equipment equip = new Equipment();
		equip.name = name;
		equip.matches = new EquipMatch[0];
		for (String sub : def.split("\\s+")) {
			if (sub.contains("=")) {
				String desc = sub.substring(sub.indexOf("=")+1);
				if (sub.startsWith("n")) {
					equip.name = desc;
				}
			} else {
				if (equip.primaryMatch==null) {
					equip.primaryMatch = getMatch(sub);
				} else {
					List<EquipMatch> a = Arrays.asList(equip.matches);
					ArrayList<EquipMatch> a2 = new ArrayList<>();
					a2.addAll(a);
					a2.add(getMatch(sub));
					equip.matches = a2.toArray(equip.matches);
				}
			}
		}
		return equip;
	}
	
	public static EquipMatch getMatch(String input) {
		String name = input;
		EquipMatch m = new EquipMatch();
		if (input.contains("?")) {
			m.zeroOrOne = true;
			name = name.replace("?", "");
		}
		if (input.contains("*")) {
			m.zeroOrMore = true;
			name = name.replace("*", "");
		}
		if (input.contains("+")) {
			m.oneOrMore = true;
			name = name.replace("+", "");
		}
		m.term = name.replace("_", " ");
		return m;
	}
}
