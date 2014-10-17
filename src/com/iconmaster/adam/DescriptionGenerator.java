package com.iconmaster.adam;

import java.util.ArrayList;

/**
 *
 * @author iconmaster
 */
public class DescriptionGenerator {
	public static String getFullBeingDesc(BodyPart part) {
		StringBuilder sb = new StringBuilder("This is ");
		sb.append(getAorAn(formatName(part)));
		sb.append(" ");
		sb.append(formatName(part));
		sb.append(".");
		if (!part.layers.isEmpty()) {
			sb.append("\n\nOn the surface is ");
			sb.append(getFullDesc(part));
		}
		sb.append("\nThis ");
		sb.append(formatName(part));
		sb.append(" is ");
		sb.append(Double.toString(part.size));
		sb.append(" ft tall and weighs ");
		sb.append(Double.toString(part.getMass()));
		sb.append(" lbs.");
		return sb.toString();
	}
		
	public static String getFullDesc(BodyPart part) {
		StringBuilder sb = new StringBuilder();
		if (!part.layers.isEmpty()) {
			for (int i=0;i<part.layers.size();i++) {
				ArrayList<BodyPart> layer = part.layers.get(i);
				String[] list = new String[layer.size()];
				for (int j=0;j<layer.size();j++) {
					BodyPart part2 = layer.get(j);
					list[j] = getAorAn(formatName(part2))+" "+formatName(part2);
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
						sb.append("\nThe ");
						sb.append(formatName(part2));
						sb.append(", on the surface, is composed of ");
						sb.append(getFullDesc(part2));
					}
				}
			}
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
}
