package com.iconmaster.adam;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author iconmaster
 */
public class BodyPartFactory {
	public static HashMap<String,String> parts = new HashMap<>();
	
	public static void registerPart(String name, String define) {
		parts.put(name, define);
	}
	
	public static String getPartDef(String name) {
		return parts.getOrDefault(name, "");
	}
	
	public static BodyPart generate(String name) {
		return generateFromDef(name,getPartDef(name));
	}
	
	public static BodyPart generateFromDef(String name, String def) {
		BodyPart part = new BodyPart();
		part.type = name;
		part.name = name;
		if (def.isEmpty()) {
			return part;
		}
		for (String sub : def.split("\\s+")) {
			if (!applyFlag(part,sub))  {
				ArrayList<BodyPart> pl = new ArrayList<>();
				for (String sub2 : sub.split(",")) {
					if (sub2.contains("(")) {
						BodyPart part2 = generate(sub2.substring(0,sub2.lastIndexOf('(')));
						part2.parent = part;
						for (String sub3 : sub2.substring(sub2.lastIndexOf('(')+1,sub2.length()-1).split(";")) {
							applyFlag(part2,sub3);
						}
						pl.add(part2);
					} else {
						BodyPart part2 = generate(sub2);
						part2.parent = part;
						pl.add(part2);
					}
				}
				part.layers.add(pl);
			}
		}
		part.healAll();
		return part;
	}
	
	public static boolean applyFlag(BodyPart part, String input) {
		if (input.matches("[^\\(]*=.*")) {
			String flag = input.substring(0,input.indexOf("="));
			String desc = input.substring(input.indexOf("=")+1);
			if (flag.startsWith("n")) {
				part.name = desc.replace("_", " ");
			} else if (flag.startsWith("s")) {
				part.size = Double.parseDouble(desc);
			} else if (flag.startsWith("d")) {
				part.desc = desc.replace("_", " ");
			} else if (flag.startsWith("m")) {
				part.density = Double.parseDouble(desc);
			} else if (flag.startsWith("p")) {
				part.plural = Boolean.parseBoolean(desc);
			} else if (flag.startsWith("e")) {
				part.essential = Boolean.parseBoolean(desc);
			} else if (flag.startsWith("hm")) {
				part.maxDamage = Double.parseDouble(desc);
				part.destructionDamage = 2*part.maxDamage;
			} else if (flag.startsWith("hd")) {
				part.destructionDamage = Double.parseDouble(desc);
			} else if (flag.startsWith("hc")) {
				part.hitChance = Double.parseDouble(desc);
			} else if (flag.startsWith("ir")) {
				part.removalString = desc.replace("_", " ");
			} else if (flag.startsWith("i")) {
				part.injuryString = desc.replace("_", " ");
			} else if (flag.startsWith("bc")) {
				part.bleedChance = Double.parseDouble(desc);
			} else if (flag.startsWith("br")) {
				part.bleedRate = Double.parseDouble(desc);
			}
			return true;
		}
		return false;
	}
}
