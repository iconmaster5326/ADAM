package info.iconmaster.adam.game;

import java.awt.Color;
import java.util.ArrayList;

import info.iconmaster.adam.entity.Assembly.Joint;
import info.iconmaster.adam.entity.Assembly.JointType;
import info.iconmaster.adam.entity.Entity;
import info.iconmaster.adam.entity.base.BasicAssembly;
import info.iconmaster.adam.entity.base.BasicEntity;
import info.iconmaster.adam.entity.base.BasicWorld;
import info.iconmaster.adam.util.ChunkCoord;
import info.iconmaster.adam.util.WorldCoord;

public class AdamGame {
	public Entity player;
	public ArrayList<Entity> controllable = new ArrayList<>();
	public long timePassed = 0;
	
	public AdamGame() {
		Entity head = new BasicEntity("head", new WorldCoord(.6,1,.6), 50, Color.GRAY);
		Entity torso = new BasicEntity("torso", new WorldCoord(2.5,3,1), 150, Color.YELLOW);
		Entity larm = new BasicEntity("left arm", new WorldCoord(1,3,1), 50, Color.RED);
		Entity rarm = new BasicEntity("right arm", new WorldCoord(1,3,1), 50, Color.BLUE);
		Entity lleg = new BasicEntity("left leg", new WorldCoord(1,3,1), 50, Color.GREEN);
		Entity rleg = new BasicEntity("right leg", new WorldCoord(1,3,1), 50, Color.CYAN);
		
		BasicAssembly playerAssembly = new BasicAssembly("Bumpus");
		player = playerAssembly;
		
		playerAssembly.getParts().add(head);
		playerAssembly.getParts().add(torso);
		playerAssembly.getParts().add(larm);
		playerAssembly.getParts().add(rarm);
		playerAssembly.getParts().add(lleg);
		playerAssembly.getParts().add(rleg);
		
		playerAssembly.getJoints().add(new Joint(head, JointType.TOP, torso));
		playerAssembly.getJoints().add(new Joint(larm, JointType.LEFT, torso));
		playerAssembly.getJoints().add(new Joint(rarm, JointType.RIGHT, torso));
		playerAssembly.getJoints().add(new Joint(lleg, JointType.BOTTOM, torso));
		playerAssembly.getJoints().add(new Joint(rleg, JointType.BOTTOM, torso));
		
		BasicWorld earth = new BasicWorld("Earth", 1000000, 1000000);
		earth.getChunk(new ChunkCoord(0, 0)).entities.put(player, new WorldCoord(5, 0, 3, Math.PI/3));
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
