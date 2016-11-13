package info.iconmaster.adam.game;

import java.util.ArrayList;

import info.iconmaster.adam.entity.Entity;
import info.iconmaster.adam.entity.base.BasicEntity;
import info.iconmaster.adam.entity.base.BasicWorld;
import info.iconmaster.adam.util.Vector3;

public class AdamGame {
	public Entity player;
	public ArrayList<Entity> controllable = new ArrayList<>();
	public long timePassed = 0;
	
	public AdamGame() {
		player = new BasicEntity("Bumpus", new Vector3(3,6,1), 200);
		BasicWorld earth = new BasicWorld("Earth", 1000000, 1000000);
		earth.entities().put(player, new Vector3(0, 0, 0));
		controllable.add(player);
	}
	
	public String doAction(String input) {
		input = input.trim().toLowerCase();
		
		if (input.contains(":")) {
			String subjectName = input.substring(0, input.indexOf(':')).trim();
			input = input.substring(input.indexOf(':')+1).trim();
		}
		
		Command cmd = null;
		for (String key : Command.commands.keySet()) {
			if (input.startsWith(key)) {
				cmd = Command.commands.get(key);
				break;
			}
		}
		
		if (cmd == null) {
			return "Come again?";
		}
		
		Entity subject = player;
		ArrayList<String> flags = new ArrayList<>();
		for (int i = 0; i < cmd.flags.size(); i++) {
			flags.add(null);
		}
		
		ArrayList<String> parts = new ArrayList<>();
		parts.add(input.substring(cmd.name.length()).trim());
		
		for (String flagName : cmd.flags) {
			ArrayList<String> newParts = new ArrayList<>();
			for (String part: parts) {
				if (part.contains(flagName)) {
					newParts.add(part.substring(0, part.indexOf(flagName)).trim());
					newParts.add(flagName);
					newParts.add(part.substring(part.indexOf(flagName)+flagName.length()).trim());
				} else {
					newParts.add(part);
				}
			}
			parts = newParts;
		}
		
		for (int i = 0; i < parts.size(); i++) {
			String part = parts.get(i);
			if (cmd.flags.contains(part)) {
				String arg = (i-1 == parts.size() ? "" : (cmd.flags.contains(parts.get(i+1)) ? "" : parts.get(i+1)));
				flags.set(cmd.flags.indexOf(part), arg);
			}
		}
		
		return cmd.execute(this, subject, (parts.isEmpty() ? "" : parts.get(0)), flags);
	}
}
