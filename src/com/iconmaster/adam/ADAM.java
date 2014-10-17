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
		BodyPartFactory.registerPart("human", "body");
		
		BodyPartFactory.registerPart("body", "chest,arms,legs");
		BodyPartFactory.registerPart("chest", "skin muscle ribs(p=true) spine,guts");
		BodyPartFactory.registerPart("guts", "p=true heart,stomach,intestines(p=true),lung(n=left_lung),lung(n=right_lung)");
		
		BodyPartFactory.registerPart("legs", "d=pair_of_%s leg(n=left_leg),leg(n=right_leg)");
		BodyPartFactory.registerPart("leg", "foot,skin fat muscle bone");
		
		BodyPartFactory.registerPart("arms", "d=pair_of_%s arm(n=left_arm),arm(n=right_arm)");
		BodyPartFactory.registerPart("arm", "hand,skin fat muscle bone");
		
		BodyPartFactory.registerPart("skin", "d=layer_of_%s");
		BodyPartFactory.registerPart("fat", "d=layer_of_%s");
		BodyPartFactory.registerPart("muscle", "d=layer_of_%s");
		BodyPartFactory.registerPart("bone", "d=layer_of_%s");
		
		BodyPart being = BodyPartFactory.generate("human");
		System.out.println(being);
		
		String desc = DescriptionGenerator.getFullBeingDesc(being);
		System.out.println(desc);
	}
	
}
