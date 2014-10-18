package com.iconmaster.adam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author iconmaster
 */
public class CommandLine {
	public static class Command {
		String name;
		int args;
		OnExecute fn;

		public Command(String name, int args, OnExecute fn) {
			this.name = name;
			this.args = args;
			this.fn = fn;
		}
	}
	
	public static interface OnExecute {
		public void execute(String... args);
	}
	
	public CommandLine() {
		addCommand("exit",0,(args)->{
			System.exit(0);
		});
		addAlias("exit","quit");
	}
	
	public ArrayList<Command> cmds = new ArrayList<>();
	
	public void addCommand(String name, OnExecute fn) {
		cmds.add(new Command(name, -1, fn));
	}
	
	public void addCommand(String name, int args, OnExecute fn) {
		cmds.add(new Command(name, args, fn));
	}
	
	public void addAlias(String name, String newName) {
		for (Command cmd : (ArrayList<Command>)cmds.clone()) {
			if (cmd.name.equals(name)) {
				addCommand(newName,cmd.args,cmd.fn);
			}
		}
	}
	
	public void execute(String name, String... args) {
		Command partfound = null;
		for (Command cmd : cmds) {
			if (cmd.name.equals(name)) {
				partfound = cmd;
			}
			if (cmd.name.equals(name) && (cmd.args==-1 || cmd.args==args.length)) {
				cmd.fn.execute(args);
				return;
			}
		}
		if (partfound!=null) {
			System.out.println("ERROR: Command "+name+" found for "+partfound.args+" arguments, not "+args.length+".");
		} else {
			System.out.println("ERROR: Command "+name+" not found.");
		}
	}
	
	public void handle() {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			String input = in.nextLine();
			String[] terms = input.split("\\s+");
			
			if (terms.length!=0) {
				String prefix = terms[0];
				terms = Arrays.copyOfRange(terms, 1, terms.length);
				
				execute(prefix,terms);
			}
		}
	}
}
