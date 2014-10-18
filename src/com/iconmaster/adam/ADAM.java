package com.iconmaster.adam;

import com.iconmaster.adam.CLAHelper.CLA;

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
		
		CLA cla = CLAHelper.getArgs(args);
		if (!cla.containsKey("d")) {
			
			class InteractiveSystem {
				public BodyPart being;
				public boolean you = true;
				public DamageResult dmgres = null;
			}
			
			final InteractiveSystem sys = new InteractiveSystem();
			sys.being = being;
			
			CommandLine cl = new CommandLine();
			cl.addCommand("desc",0,(s)->{
				System.out.println(DescriptionGenerator.getFullBeingDesc(sys.being,sys.you));
			});
			cl.addCommand("you",0,(s)->{
				System.out.println("Second-person mode is "+(sys.you?"on.":"off."));
			});
			cl.addCommand("you",1,(s)->{
				sys.you = Boolean.parseBoolean(s[0]);
				System.out.println("Second-person mode is now "+(sys.you?"on.":"off."));
			});
			cl.addCommand("name",0,(s)->{
				System.out.println((sys.you?"You are ":"This is ")+(sys.being.proper?"":DescriptionGenerator.getAorAn(sys.being.name)+" ")+DescriptionGenerator.formatName(sys.being)+".");
			});
			cl.addCommand("name",1,(s)->{
				sys.being.name = s[0];
				System.out.println((sys.you?"You are now ":"This is now ")+(sys.being.proper?"":DescriptionGenerator.getAorAn(sys.being.name)+" ")+DescriptionGenerator.formatName(sys.being)+".");
			});
			cl.addCommand("desc",0,(s)->{
				System.out.println((sys.you?"You are ":"This is ")+(sys.being.proper?"":DescriptionGenerator.getAorAn(sys.being.name)+" ")+DescriptionGenerator.formatName(sys.being)+".");
			});
			cl.addCommand("desc",1,(s)->{
				sys.being.desc = s[0];
				System.out.println((sys.you?"You are now ":"This is now ")+(sys.being.proper?"":DescriptionGenerator.getAorAn(sys.being.name)+" ")+DescriptionGenerator.formatName(sys.being)+".");
			});
			cl.addCommand("size",0,(s)->{
				System.out.println((sys.you?"You are ":"This is ")+sys.being.size+" feet tall.");
			});
			cl.addCommand("size",1,(s)->{
				sys.being.size = Double.parseDouble(s[0]);
				System.out.println((sys.you?"You are now ":"This is now ")+sys.being.size+" feet tall.");
			});
			cl.addCommand("mass",0,(s)->{
				System.out.println((sys.you?"You weigh ":"This weighs ")+sys.being.getMass()+" pounds.");
			});
			cl.addCommand("proper",0,(s)->{
				System.out.println((sys.you?"you are ":"This being is ")+(sys.being.proper?"":"not ")+"referred to by a proper noun.");
			});
			cl.addCommand("proper",1,(s)->{
				sys.being.proper = Boolean.parseBoolean(s[0]);
				System.out.println((sys.you?"you are now ":"This being is now ")+(sys.being.proper?"":"not ")+"referred to by a proper noun.");
			});
			cl.addCommand("plural",0,(s)->{
				System.out.println((sys.you?"you are ":"This being is ")+(sys.being.plural?"":"not ")+"pluralized.");
			});
			cl.addCommand("plural",1,(s)->{
				sys.being.plural = Boolean.parseBoolean(s[0]);
				System.out.println((sys.you?"you are now ":"This being is now ")+(sys.being.plural?"":"not ")+"pluralized.");
			});
			cl.addCommand("damage",0,(s)->{
				System.out.println((sys.you?"you have been ":"This being has been ")+sys.being.getRelativeDamage()*100+"% damaged.");
			});
			cl.addCommand("new",0,(s)->{
				sys.being = BodyPartFactory.generate("human");
				System.out.println("Generated a new "+sys.being.type+".");
			});
			cl.addCommand("new",1,(s)->{
				sys.being = BodyPartFactory.generate(s[0]);
				System.out.println("Generated a new "+sys.being.type+".");
			});
			cl.addCommand("hit",1,(s)->{
				double damage = Double.parseDouble(s[0]);
				System.out.println("Hitting for "+damage+"...");
				sys.dmgres = sys.being.damage(damage);
			});
			cl.addCommand("wound",0,(s)->{
				if (sys.dmgres==null) {
					System.out.println("There's no wounds to write home about!");
				} else {
					System.out.println(DescriptionGenerator.getInjuryDesc(sys.being,sys.dmgres,sys.you));
				}
			});
			
			cl.handle();
			return;
		}

		for (int i=0;i<10;i++) {
			System.out.println("Hitting for 5...");
			DamageResult res = being.damage(5);

			System.out.println("Bumpus has an avg. damage of "+being.getRelativeDamage());

			System.out.println(DescriptionGenerator.getInjuryDesc(being,res,true));
			System.out.println();
		}
		
		String desc = DescriptionGenerator.getFullBeingDesc(being,true);
		System.out.println(desc);
	}
	
}
