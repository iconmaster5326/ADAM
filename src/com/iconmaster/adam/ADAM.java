package com.iconmaster.adam;

import com.iconmaster.adam.CLAHelper.CLA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
		BodyPartFactory.registerPart("neck", "skin fat muscle bone(e=true)");
		
		BodyPartFactory.registerPart("eye", "ir=missing");
		BodyPartFactory.registerPart("ear", "ir=missing");
		BodyPartFactory.registerPart("nose", "ir=missing");
		
		BodyPartFactory.registerPart("skull", "ir=crushed");
		BodyPartFactory.registerPart("brain", "e=true ir=crushed");
		
		BodyPartFactory.registerPart("body", "s=.8 chest,arms,legs");
		BodyPartFactory.registerPart("chest", "s=.6 skin fat muscle ribs spine,guts");
		BodyPartFactory.registerPart("ribs", "s=.08 m=13 p=true i=fractured ir=broken");
		BodyPartFactory.registerPart("spine", "e=true s=.08 m=13 i=fractured ir=broken");
		BodyPartFactory.registerPart("guts", "n=chest_cavity s=.05 p=true heart,stomach,intestines,lung(n=left_lung),lung(n=right_lung)");
		
		BodyPartFactory.registerPart("heart", "s=.1 m=14 e=true ir=crushed");
		BodyPartFactory.registerPart("stomach", "s=.25 m=9 ir=crushed");
		BodyPartFactory.registerPart("intestines", "s=.5 m=5 p=true ir=crushed");
		BodyPartFactory.registerPart("lung", "s=.125 m=2 e=true ir=crushed");
		
		BodyPartFactory.registerPart("legs", "s=.15 d=pair_of_%s leg(n=left_leg),leg(n=right_leg)");
		BodyPartFactory.registerPart("leg", "s=.5 foot(hc=.1),skin(hc=.9) fat muscle bone");
		
		BodyPartFactory.registerPart("arms", "s=.15 d=pair_of_%s arm(n=left_arm),arm(n=right_arm)");
		BodyPartFactory.registerPart("arm", "s=.5 hand,skin(hc=.9) fat muscle bone");
		
		BodyPartFactory.registerPart("hand", "hc=.1 a+=punch");
		
		BodyPartFactory.registerPart("skin", "s=.1 m=7 d=layer_of_%s i=bruised ir=flayed");
		BodyPartFactory.registerPart("fat", "s=.25 m=6 d=layer_of_%s i=torn ir=flayed");
		BodyPartFactory.registerPart("muscle", "s=.25 m=8 d=layer_of_%s i=torn ir=flayed");
		BodyPartFactory.registerPart("bone", "s=.4 m=7 i=fractured ir=broken");
		
		BodyPartFactory.registerAttack("punch", "d=5-10 p=.2");
		
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
				public HashMap<String,BodyPart> saved = new HashMap<>();
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
				sys.being.name = s[0].replace("_", " ");
				System.out.println((sys.you?"You are now ":"This is now ")+(sys.being.proper?"":DescriptionGenerator.getAorAn(sys.being.name)+" ")+DescriptionGenerator.formatName(sys.being)+".");
			});
			cl.addCommand("desc",0,(s)->{
				System.out.println((sys.you?"You are ":"This is ")+(sys.being.proper?"":DescriptionGenerator.getAorAn(sys.being.name)+" ")+DescriptionGenerator.formatName(sys.being)+".");
			});
			cl.addCommand("desc",1,(s)->{
				sys.being.desc = s[0].replace("_", " ");;
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
				if (sys.being.layers.isEmpty()) {
					System.out.println("Damage: "+sys.being.damage+"/"+sys.being.maxDamage+"/"+sys.being.destructionDamage);
					System.out.println("Bleed: "+sys.being.bleed);
				}
				if (sys.being.getRootPart()==sys.being) {
					System.out.println("Blood: "+sys.being.blood+"/"+sys.being.getMaxBlood());
				}
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
			cl.addCommand("new",-1,(s)->{
				sys.being = BodyPartFactory.generateFromDef(s[0],CommandLine.recombine(Arrays.copyOfRange(s, 1, s.length)));
				System.out.println("Generated a new "+sys.being.type+".");
			});
			cl.addCommand("hit",2,(s)->{
				double damage = Double.parseDouble(s[0]);
				double punch = Double.parseDouble(s[1]);
				System.out.println("Hitting for "+damage+"...");
				sys.dmgres = sys.being.damage(damage,punch);
				System.out.println();
				System.out.println(DescriptionGenerator.getInjuryDesc(sys.being,sys.dmgres,sys.you));
			});
			cl.addCommand("hit",1,(s)->{
				double damage = Double.parseDouble(s[0]);
				System.out.println("Hitting for "+damage+"...");
				sys.dmgres = sys.being.damage(damage);
				System.out.println();
				System.out.println(DescriptionGenerator.getInjuryDesc(sys.being,sys.dmgres,sys.you));
			});
			cl.addCommand("wound",0,(s)->{
				if (sys.dmgres==null) {
					System.out.println("There's no wounds to write home about!");
				} else {
					System.out.println(DescriptionGenerator.getInjuryDesc(sys.being,sys.dmgres,sys.you));
				}
			});
			cl.addCommand("get",1,(s)->{
				BodyPart part = sys.being.getPartRef(s[0]);
				if (part==null) {
					System.out.println("There's no part called that!");
				} else {
					sys.being = part;
					System.out.println("You are now working with "+part.name+".");
				}
			});
			cl.addCommand("save",1,(s)->{
				sys.saved.put(s[0], sys.being);
				System.out.println(sys.being.name+" has been saved.");
			});
			cl.addCommand("load",1,(s)->{
				BodyPart part = sys.saved.get(s[0]);
				if (part==null) {
					System.out.println("There's nothing saved in "+s[0]+"!");
				} else {
					sys.being = part;
					System.out.println(sys.being.name+" has been loaded.");
				}
			});
			cl.addCommand("define",-1,(s)->{
				BodyPartFactory.registerPart(s[0], CommandLine.recombine(Arrays.copyOfRange(s, 1, s.length)));
			});
			cl.addCommand("newl",0,(s)->{
				sys.being.layers.add(new ArrayList<>());
				System.out.println("Layer "+(sys.being.layers.size()-1)+" added.");
			});
			cl.addCommand("clear",0,(s)->{
				sys.being.layers.clear();
				System.out.println("All layers cleared.");
			});
			cl.addCommand("add",2,(s)->{
				BodyPart part = BodyPartFactory.generate(s[0]);
				int layer = Integer.parseInt(s[1]);
				if (layer<0 || layer>sys.being.layers.size()-1) {
					System.out.println("The specified layer doesn't exist!");
					return;
				}
				sys.being.layers.get(layer).add(part);
				part.parent = sys.being;
				System.out.println("Added "+part.name+".");
			});
			cl.addCommand("addg",2,(s)->{
				BodyPart part = BodyPartFactory.generate(s[0]);
				int layer = Integer.parseInt(s[1]);
				if (layer<0 || layer>sys.being.layers.size()-1) {
					System.out.println("The specified layer doesn't exist!");
					return;
				}
				sys.being.layers.get(layer).add(part);
				part.parent = sys.being;
				sys.being = part;
				System.out.println("Added "+part.name+".");
			});
			cl.addCommand("comp",0,(s)->{
				int i=0;
				for (ArrayList<BodyPart> layer : sys.being.layers) {
					System.out.print("Layer "+i+": ");
					for (BodyPart part : layer) {
						System.out.print(part);
						System.out.print(", ");
					}
					System.out.println();
					i++;
				}
			});
			cl.addCommand("where",0,(s)->{
				System.out.println("You are at "+sys.being.getLocation()+".");
			});
			cl.addCommand("find",1,(s)->{
				ArrayList<BodyPart> parts = sys.being.findAllParts(s[0].replace("_", " "));
				System.out.println(parts.size()+" entires were found:");
				for (BodyPart part : parts) {
					System.out.println("\t"+part.getLocation());
				}
			});
			cl.addCommand("del",1,(s)->{
				int layerOf = -1;
				int partOf = -1;
				int i = 0;
				for (ArrayList<BodyPart> layer : sys.being.layers) {
					int j = 0;
					for (BodyPart part : layer) {
						if (part.name.equals(s[0].replace("_"," "))) {
							layerOf = i;
							partOf = j;
						}
						j++;
					}
					i++;
				}
				if (layerOf==-1) {
					System.out.println("There isn't a part called "+s[0]+"!");
					return;
				}
				sys.being.layers.get(layerOf).remove(partOf);
				System.out.println("Deleted "+s[0]+".");
			});
			cl.addCommand("clean",0,(s)->{
				int old = sys.being.layers.size();
				sys.being.clean();
				System.out.println("Cleaned up. Deleted "+(old-sys.being.layers.size())+" empty layers.");
			});
			cl.addCommand("alive",0,(s)->{
				IsAliveResult cause = sys.being.isAlive();
				System.out.println((sys.you?"you are ":"This being is ")+(cause==null?"":"not ")+"alive.");
				if (cause!=null) {
					System.out.println((sys.you?"you are ":"This being is ")+"dead due to "+DescriptionGenerator.getCauseOfDeath(being, cause));
				}
			});
			cl.addCommand("heal",0,(s)->{
				HealResult healed = sys.being.healAll();
				System.out.println((sys.you?"you have been healed for ":"This being has been healed for ")+healed.damageHealed+" damage and "+healed.bloodHealed+" blood.");
			});
			cl.addCommand("tick",0,(s)->{
				TickResult tr = sys.being.tick();
				System.out.println(DescriptionGenerator.getTickDesc(sys.being,tr,sys.you));
			});
			cl.addCommand("bleed",0,(s)->{
				System.out.println("Bleed is "+sys.being.bleed+".");
			});
			cl.addCommand("bleed",1,(s)->{
				sys.being.bleed = Double.parseDouble(s[0]);
				System.out.println("Bleed is now "+sys.being.bleed+".");
			});
			cl.addCommand("blood",0,(s)->{
				System.out.println("Blood is "+sys.being.blood+".");
			});
			cl.addCommand("blood",1,(s)->{
				sys.being.blood = Double.parseDouble(s[0]);
				System.out.println("Blood is now "+sys.being.blood+".");
			});
			cl.addCommand("goto",1,(s)->{
				ArrayList<BodyPart> parts = sys.being.findAllParts(s[0].replace("_", " "));
				if (parts.isEmpty()) {
					System.out.println("There isn't a part anywhere called "+s[0]+"!");
					return;
				}
				sys.being = parts.get(0);
				System.out.println("Navigated to "+sys.being+".");
			});
			cl.addCommand("goto",2,(s)->{
				ArrayList<BodyPart> parts = sys.being.findAllParts(s[0].replace("_", " "));
				int index = Integer.parseInt(s[1]);
				if (index<0 || index>parts.size()-1) {
					System.out.println("There isn't a part anywhere called "+s[0]+"!");
					return;
				}
				sys.being = parts.get(index);
				System.out.println("Navigated to "+sys.being+".");
			});
			cl.addCommand("fight",1,(s)->{
				BodyPart other = sys.saved.get(s[0]);
				if (other==null) {
					System.out.println("You can't fight something that doesn't exist!");
					return;
				}
				Battle battle = new Battle(sys.being, other);
				battle.doBattle();
			});
			cl.addCommand("human",1,(s)->{
				sys.being = BodyPartFactory.generate("human");
				sys.being.name = s[0].replace("_", " ");
				sys.being.size = 5+7d/12;
				sys.being.proper = true;
				System.out.println("Generated "+s[0]+".");
			});
			cl.addCommand("root",0,(s)->{
				sys.being = sys.being.getRootPart();
				System.out.println("Went to root.");
			});
			cl.addCommand("attack",0,(s)->{
				ArrayList<Attack> a = sys.being.getAttacks();
				System.out.println("Found "+a.size()+" attacks available:");
				for (Attack attk : a) {
					System.out.println("\t"+attk.name);
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
