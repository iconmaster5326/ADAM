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
		BodyPartFactory.registerPart("human", "head,body");
		
		BodyPartFactory.registerPart("head", "mouth,nose,eye(n=left_eye),eye(n=right_eye),ear(n=left_ear),ear(n=right_ear),neck skull brain");
		BodyPartFactory.registerPart("mouth", "lips(p=true) teeth(p=true) tongue");
		BodyPartFactory.registerPart("neck", "skin fat muscle bone");
		
		BodyPartFactory.registerPart("body", "chest,arms,legs");
		BodyPartFactory.registerPart("chest", "skin fat muscle ribs(p=true) spine,guts");
		BodyPartFactory.registerPart("guts", "p=true heart,stomach,intestines(p=true),lung(n=left_lung),lung(n=right_lung)");
		
		BodyPartFactory.registerPart("legs", "d=pair_of_%s leg(n=left_leg),leg(n=right_leg)");
		BodyPartFactory.registerPart("leg", "foot,skin fat muscle bone");
		
		BodyPartFactory.registerPart("arms", "d=pair_of_%s arm(n=left_arm),arm(n=right_arm)");
		BodyPartFactory.registerPart("arm", "hand,skin fat muscle bone");
		
		BodyPartFactory.registerPart("skin", "d=layer_of_%s");
		BodyPartFactory.registerPart("fat", "d=layer_of_%s");
		BodyPartFactory.registerPart("muscle", "d=layer_of_%s");
		BodyPartFactory.registerPart("bone", "p=true");
		
		BodyPart being = BodyPartFactory.generate("human");
		System.out.println(being);
		
		String desc = DescriptionGenerator.getFullBeingDesc(being,false);
		System.out.println(desc);
	}
	
}
