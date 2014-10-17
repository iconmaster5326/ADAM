package com.iconmaster.adam;

/**
 *
 * @author iconmaster
 */
public class ADAM {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		BodyPartFactory.registerPart("arms", "s=2 d=pair_of_%s arm(n=left_arm),arm(n=right_arm)");
		BodyPartFactory.registerPart("arm", "hand,skin fat muscle bone");
		
		BodyPartFactory.registerPart("skin", "d=layer_of_%s");
		BodyPartFactory.registerPart("fat", "d=layer_of_%s");
		BodyPartFactory.registerPart("muscle", "d=layer_of_%s");
		BodyPartFactory.registerPart("bone", "d=layer_of_%s");
		
		BodyPart being = BodyPartFactory.generate("arms");
		System.out.println(being);
		
		String desc = DescriptionGenerator.getFullBeingDesc(being);
		System.out.println(desc);
	}
	
}
