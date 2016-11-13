package info.iconmaster.adam.game.commands;

import java.util.Arrays;
import java.util.List;

import info.iconmaster.adam.entity.Entity;
import info.iconmaster.adam.game.AdamGame;
import info.iconmaster.adam.game.Command;

public class CommandLook extends Command {

	public CommandLook() {
		super("look", Arrays.asList("at"));
	}

	@Override
	public String execute(AdamGame game, Entity subject, String arg, List<String> flags) {
		if (flags.get(0) == null) {
			return "You look around you.";
		} else {
			return "You look at " + flags.get(0) + ".";
		}
	}
}
