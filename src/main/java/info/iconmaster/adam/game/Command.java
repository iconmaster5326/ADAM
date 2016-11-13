package info.iconmaster.adam.game;

import java.util.HashMap;
import java.util.List;

import info.iconmaster.adam.entity.Entity;
import info.iconmaster.adam.game.commands.CommandLook;

public abstract class Command {
	String name;
	List<String> flags;
	
	public Command(String name, List<String> flags) {
		this.name = name;
		this.flags = flags;
	}
	
	public abstract String execute(AdamGame game, Entity subject, String arg, List<String> flags);
	
	public static HashMap<String, Command> commands;
	public static void addCommand(Command cmd) {
		commands.put(cmd.name, cmd);
	}
	
	static {
		commands = new HashMap<>();
		
		addCommand(new CommandLook());
	}
}
