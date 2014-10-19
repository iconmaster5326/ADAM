package com.iconmaster.adam;

import com.iconmaster.adam.fight.IsAliveResult;
import com.iconmaster.adam.fight.DamageResult;
import com.iconmaster.adam.fight.TickResult;
import java.util.ArrayList;

/**
 *
 * @author iconmaster
 */
public class DescriptionGenerator {
	public static String getFullBeingDesc(BodyPart part, boolean you) {
		StringBuilder sb = new StringBuilder(you?"You are ":"This is ");
		if (!part.proper) {
			sb.append(getAorAn(formatName(part)));
			sb.append(" ");
		}
		sb.append(formatName(part));
		sb.append(".");
		if (part.proper) {
			sb.append(you?" You are ":(" "+capitalize(part.pronouns.he)+" is "));
			sb.append(getAorAn(formatName(part)));
			sb.append(" ");
			sb.append(part.desc.replace("%s", part.type));
			sb.append(".");
		}
		if (!part.layers.isEmpty()) {
			sb.append("\n\nOn ");
			sb.append(you?"your":part.pronouns.his);
			sb.append(" surface is ");
			sb.append(getFullDesc(part,you));
		}
		if (you) {
			sb.append("\nYou are ");
		} else {
			sb.append("\n");
			if (!part.proper) {
				sb.append("This ");
			}
			sb.append(formatName(part));
			sb.append(" is ");
		}
		sb.append(Double.toString(part.size));
		sb.append(" ft tall and weigh");
		sb.append(you?" ":"s ");
		sb.append(Double.toString(part.getMass()));
		sb.append(" lbs.");
		
		ArrayList<BodyPart> injuries = part.getDamagedParts();
		if (!injuries.isEmpty()) {
			sb.append("\n");
			if (you) {
				sb.append("\nYou are ");
			} else {
				sb.append("\nThis ");
				sb.append(formatName(part));
				sb.append(" is ");
			}
			sb.append(getInjuryString(part));
			sb.append(".");
			for (BodyPart injury : injuries) {
				sb.append("\n");
				if (injury.parent!=null && injury.layers.isEmpty()) {
					sb.append("The ");
					sb.append(formatName(injury));
					sb.append(injury.getLayerOn()==0?" on ":" in ");
					sb.append(you?"your ":(part.pronouns.his+" "));
					sb.append(formatName(injury.parent));
				} else {
					sb.append(you?"Your ":"The ");
					sb.append(formatName(injury));
				}
				sb.append(injury.plural?" are ":" is ");
				sb.append(getInjuryString(injury));
				sb.append(".");
				if (injury.isBleeding()) {
					sb.append(" The injury is bleeding ");
					sb.append(getBleedString(injury));
					sb.append(".");
				}
			}
		}
		if (part.isAlive()!=null) {
			sb.append("\n\n");
			IsAliveResult cause = part.isAlive();
			sb.append(you?"You are":(capitalize(part.pronouns.he)+" is"));
			sb.append(" dead. ");
			sb.append(you?"You":(capitalize(part.pronouns.he)));
			sb.append(" died due to ");
			sb.append(getCauseOfDeath(part,cause));
		}
		return sb.toString();
	}
		
	public static String getFullDesc(BodyPart part, boolean you) {
		StringBuilder sb = new StringBuilder();
		if (!part.layers.isEmpty()) {
			for (int i=0;i<part.layers.size();i++) {
				ArrayList<BodyPart> layer = part.layers.get(i);
				String[] list = new String[layer.size()];
				for (int j=0;j<layer.size();j++) {
					BodyPart part2 = layer.get(j);
					list[j] = (part2.plural ? "": getAorAn(formatName(part2))+" ")+formatName(part2);
				}
				sb.append(getListString(list));
				sb.append(".");
				if (i!=part.layers.size()-1) {
					sb.append("\nUnder that is ");
				}
			}
			sb.append("\n");
			for (int i=0;i<part.layers.size();i++) {
				ArrayList<BodyPart> layer = part.layers.get(i);
				for (int j=0;j<layer.size();j++) {
					BodyPart part2 = layer.get(j);
					if (!part2.layers.isEmpty()) {
						sb.append(you?"\nYour ":("\n"+capitalize(part.pronouns.his)+" "));
						sb.append(formatName(part2));
						sb.append(", on the surface, ");
						if (part2.plural) {
							sb.append("are");
						} else {
							sb.append("is");
						}
						sb.append(" composed of ");
						sb.append(getFullDesc(part2,you));
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String getInjuryDesc(BodyPart being, DamageResult res, boolean you) {
		StringBuilder sb = new StringBuilder();
		if (you) {
			sb.append("You have an injury on your ");
		} else {
			if (!being.proper) {
				sb.append("The ");
			}
			sb.append(formatName(being));
			sb.append(" has an injury on ").append(being.pronouns.his).append(" ");
		}
		ArrayList<BodyPart> parts = res.getParts();
		BodyPart common = BodyPart.getLowestCommonPart(parts).getContainerPart();
		if (common==being || common==null) {
			sb.append("entire body.");
		} else {
			sb.append(formatName(common));
			sb.append(".");
		}
		if (common==null) {
			return sb.toString();
		}
		ArrayList<BodyPart> wound = new ArrayList<>();
		for (BodyPart part : common.getAttachedParts()) {
			if (part.containsAny(parts)) {
				wound.add(part);
			}
		}
		if (wound.isEmpty()) {
			wound = parts;
			sb.append(" The wound is localized in ");
		} else {
			sb.append(" The wound stretches between ");
		}

		String[] list = new String[wound.size()];
		for (int j=0;j<wound.size();j++) {
			BodyPart part2 = wound.get(j);
			list[j] = (you?"your ":(being.pronouns.his+" "))+formatName(part2);
		}
		sb.append(getListString(list));
		sb.append(".");
		
		for (BodyPart injury : parts) {
			sb.append("\n");
			if (injury.parent!=null && injury.layers.isEmpty()) {
				sb.append("The ");
				sb.append(formatName(injury));
				sb.append(injury.getLayerOn()==0?" on ":" in ");
				sb.append(you?"your ":(being.pronouns.his+" "));
				sb.append(formatName(injury.parent));
			} else {
				sb.append(you?"Your ":"The ");
				sb.append(formatName(injury));
			}
			sb.append(injury.plural?" are ":" is ");
			sb.append(getInjuryString(injury));
			sb.append(".");
			if (injury.isBleeding()) {
				sb.append(" The injury is bleeding ");
				sb.append(getBleedString(injury));
				sb.append(".");
			}
		}
			
		return sb.toString();
	}
	
	public static String getTickDesc(BodyPart part, TickResult tr, boolean you) {
		StringBuilder sb = new StringBuilder();
		ArrayList<BodyPart> parts = tr.getParts();
		for (BodyPart injury : parts) {
			sb.append("\n");
			if (injury.parent!=null && injury.layers.isEmpty()) {
				sb.append("The ");
				sb.append(formatName(injury));
				sb.append(injury.getLayerOn()==0?" on ":" in ");
				sb.append(you?"your ":(part.pronouns.his+" "));
				sb.append(formatName(injury.parent));
			} else {
				sb.append(you?"Your ":"The ");
				sb.append(formatName(injury));
			}
			if (tr.healed.contains(injury)) {
				sb.append(" stopped bleeding.");
			} else {
				sb.append(" bled ");
				sb.append(getBleedString(injury));
				sb.append(".");
				if (tr.clotted.contains(injury)) {
					sb.append(" The blood is clotting slightly.");
				}
			}
		}
		if (tr.bledOut) {
			sb.append("\n");
			sb.append(you?"You ":(capitalize(part.pronouns.he)+" "));
			sb.append("bled out.");
		}
		return sb.toString();
	}
	
	public static String getAorAn(String word) {
		return word.matches("[aeiou].*") ? "an" : "a";
	}
	
	public static String getListString(String[] list) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<list.length;i++) {
			sb.append(list[i]);
			if (i<list.length-1 && list.length!=2) {
				sb.append(", ");
			}
			if (i==0 && list.length == 2) {
				sb.append(" ");
			}
			if (i==list.length-2) {
				sb.append("and ");
			}
		}
		return sb.toString();
	}
	
	public static String formatName(BodyPart part) {
		return part.desc.replace("%s", part.name);
	}
	
	public static String capitalize(String input) {
		return input.substring(0,1).toUpperCase()+input.substring(1);
	}
	
	public static String getInjuryString(BodyPart part) {
		StringBuilder sb = new StringBuilder();
		if (part.isAttached()) {
			double avg = part.getRelativeDamage();
			if (avg<.1) {
				sb.append("slightly ");
			} else if (avg<.3) {
				sb.append("moderately ");
			} else if (avg<1) {
				sb.append("heavily ");
			} else if (avg>=1) {
				sb.append("entirely ");
			}
			sb.append(part.injuryString);
		} else {
			sb.append(part.removalString);
		}
		return sb.toString();
	}

	public static String getBleedString(BodyPart injury) {
		double bleed = (injury.bleed/injury.getMaxBlood());
		if (bleed<.4) {
			return "slightly";
		} else if (bleed<.8) {
			return "moderately";
		} else {
			return "profusely";
		}
	}
	
	public static String getCauseOfDeath(BodyPart being, IsAliveResult cause) {
		if (cause==null) {
			cause = being.isAlive();
		}
		if (cause.bloodLoss) {
			return "blood loss.";
		}
		if (cause.destroyed) {
			return "complete destruction.";
		}
		String rem = "a "+cause.part.removalString;
		return (rem+" "+formatName(cause.part)+".");
	}
	
	public static String formatNameFull(BodyPart part) {
		return ((part.proper || part.plural)?"":DescriptionGenerator.getAorAn(part.name)+" ")+DescriptionGenerator.formatName(part);
	}
	
	public static String pluralize(String input) {
		if (input.endsWith("s") || input.endsWith("x") || input.endsWith("ch") || input.endsWith("sh")) {
			return input+"es";
		} else if (input.endsWith("y")) {
			return input.substring(0,input.length()-1)+"ies";
		} else {
			return input+"s";
		}
	}
}
