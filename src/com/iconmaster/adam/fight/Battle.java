package com.iconmaster.adam.fight;

import com.iconmaster.adam.BodyPart;
import com.iconmaster.adam.DescriptionGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author iconmaster
 */
public class Battle {
	public static final BodyPart DRAW = new BodyPart();
	
	public ArrayList<BodyPart> combatants = new ArrayList<>();
	public int turn = 0;
	public int turns = 0;
	public Random random = new Random();

	public Battle(BodyPart... combatants) {
		this.combatants.addAll(Arrays.asList(combatants));
	}

	public Battle(ArrayList<BodyPart> combatants) {
		this.combatants.addAll(combatants);
	}
	
	public void doBattle() {
		System.out.println("FIGHT!");
		System.out.println();
		turn = random.nextInt(combatants.size());
		while (true) {
			turns++;
			
			doTurn();
			
			BodyPart winner = getWinner();
			if (winner != null) {
				System.out.println();
				if (winner==DRAW) {
					System.out.println("A draw occured in "+turns+" turns.");
					break;
				}
				System.out.println(DescriptionGenerator.formatNameFull(winner)+" won the battle in "+turns+" turns.");
				break;
			}
			
			do {
				turn--;
				if (turn<0) {
					turn = combatants.size()-1;
				}
			} while (combatants.get(turn).isAlive()!=null);
		}
	}
	
	public void doTurn() {
		BodyPart self = combatants.get(turn);
		ArrayList<BodyPart> a = getAlive();
		a.remove(self);
		BodyPart other = a.get(random.nextInt(a.size()));
		ArrayList<Attack> attks = self.getAttacks();
		if (attks.isEmpty()) {
			System.out.println(DescriptionGenerator.formatNameFull(self)+" flails around uselessly!");
			System.out.println();
		} else {
			Attack attk = attks.get(random.nextInt(attks.size()));
			DamageResult dr = attk.onAttack(other);
			System.out.println(DescriptionGenerator.formatNameFull(self)+" "+DescriptionGenerator.pluralize(attk.name)+" "+DescriptionGenerator.formatNameFull(other)+" for "+dr.damage+" damage!");
			System.out.println();
			System.out.println(DescriptionGenerator.getInjuryDesc(other, dr, false));
		}
		
		TickResult tr = other.tick();
		System.out.println(DescriptionGenerator.getTickDesc(other, tr, false));
		System.out.println();

		if (other.isAlive()!=null) {
			System.out.println(DescriptionGenerator.formatNameFull(other)+" died of "+DescriptionGenerator.getCauseOfDeath(other, null));
			System.out.println();
		}
	}
	
	public BodyPart getWinner() {
		ArrayList<BodyPart> alive = getAlive();
		if (alive.isEmpty()) {
			return DRAW;
		} else if (alive.size()==1) {
			return alive.get(0);
		}
		return null;
	}
	
	public ArrayList<BodyPart> getAlive() {
		ArrayList<BodyPart> alive = new ArrayList<>();
		for (BodyPart part : combatants) {
			if (part.isAlive()==null) {
				alive.add(part);
			}
		}
		return alive;
	}
}
