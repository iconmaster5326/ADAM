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
		
		BodyPartFactory.registerPart("head", "s=.2 skin(hc=.6),mouth,nose,eye(n=left_eye),eye(n=right_eye),ear(n=left_ear),ear(n=right_ear),neck skull brain");
		BodyPartFactory.registerPart("mouth", "lips(p=true) teeth(p=true) tongue");
		BodyPartFactory.registerPart("neck", "skin fat muscle bone");
		
		BodyPartFactory.registerPart("body", "s=.8 chest,arms,legs");
		BodyPartFactory.registerPart("chest", "s=.6 skin fat muscle ribs spine,guts");
		BodyPartFactory.registerPart("ribs", "s=.08 m=13 p=true");
		BodyPartFactory.registerPart("spine", "s=.08 m=13");
		BodyPartFactory.registerPart("guts", "s=.05 p=true heart,stomach,intestines,lung(n=left_lung),lung(n=right_lung)");
		
		BodyPartFactory.registerPart("heart", "s=.1 m=14");
		BodyPartFactory.registerPart("stomach", "s=.25 m=9");
		BodyPartFactory.registerPart("intestines", "s=.5 m=5 p=true");
		BodyPartFactory.registerPart("lung", "s=.125 m=2");
		
		BodyPartFactory.registerPart("legs", "s=.15 d=pair_of_%s leg(n=left_leg),leg(n=right_leg)");
		BodyPartFactory.registerPart("leg", "s=.5 foot(hc=.1),skin(hc=.9) fat muscle bone");
		
		BodyPartFactory.registerPart("arms", "s=.15 d=pair_of_%s arm(n=left_arm),arm(n=right_arm)");
		BodyPartFactory.registerPart("arm", "s=.5 hand(hc=.1),skin(hc=.9) fat muscle bone");
		
		BodyPartFactory.registerPart("skin", "s=.1 m=7 d=layer_of_%s i=bruised");
		BodyPartFactory.registerPart("fat", "s=.25 m=6 d=layer_of_%s i=torn");
		BodyPartFactory.registerPart("muscle", "s=.25 m=8 d=layer_of_%s i=torn");
		BodyPartFactory.registerPart("bone", "s=.4 m=7 p=true i=fractured");
		
		BodyPart being = BodyPartFactory.generate("human");
		being.size = 5+7/12d;
		being.name = "Bumpus";
		being.proper = true;

		for (int i=0;i<10;i++) {
			System.out.println("Hitting for 5...");
			DamageResult res = being.damage(5);
			//System.out.println(res);
//			for (BodyPart part : res.keySet()) {
//				System.out.println(part.name+"("+part.parent.name+") got damaged by "+res.get(part));
//			}
//			System.out.println();
			System.out.println("Bumpus has an avg. damage of "+being.getRelativeDamage());
//			System.out.println("He was injured in the "+BodyPart.getLowestCommonPart(res.getParts()));
//			System.out.println();
			System.out.println(DescriptionGenerator.getInjuryDesc(being,res,true));
			System.out.println();
		}
		
		String desc = DescriptionGenerator.getFullBeingDesc(being,true);
		System.out.println(desc);
	}
	
}
