package com.iconmaster.adam.fight;

import com.iconmaster.adam.body.BodyPart;
import com.iconmaster.adam.equip.Equipment;
import java.util.HashMap;

/**
 *
 * @author iconmaster
 */
public class AttackFactory {
	public static HashMap<String, AttackFactory> attacks = new HashMap<>();

	public static void registerAttack(String name, String def) {
		AttackFactory af = new AttackFactory();
		af.name = name;
		attacks.put(name, af);
		for (String sub : def.split("\\s+")) {
			if (sub.contains("=")) {
				String flag = sub.substring(0, sub.indexOf("="));
				String desc = sub.substring(sub.indexOf("=") + 1);
				if ("n".equals(flag)) {
					af.name = desc;
				} else if ("d".equals(flag)) {
					if (desc.contains("-")) {
						af.minimum = Double.parseDouble(desc.substring(0, desc.indexOf("-")));
						af.variance = Double.parseDouble(desc.substring(desc.indexOf("-") + 1));
					} else {
						af.minimum = Double.parseDouble(desc);
						af.variance = 0;
					}
				} else if ("ap".equals(flag)) {
					af.type.punch = Double.parseDouble(desc);
				} else if ("as".equals(flag)) {
					af.type.spread = Double.parseDouble(desc);
				}
			}
		}
	}
	
	
	public String name;
	public double minimum;
	public double variance;
	public AttackType type = new AttackType();

	public Attack newAttack(BodyPart part) {
		return new BasicAttack(this, part);
	}
	
	public Attack newAttack(Equipment part) {
		return new EquipAttack(this, part);
	}
}
