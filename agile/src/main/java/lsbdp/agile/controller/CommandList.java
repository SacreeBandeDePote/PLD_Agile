package lsbdp.agile.controller;

import java.util.ArrayList;

public class CommandList {
	private ArrayList<Command> commandList = new ArrayList<Command>();
	private int index;
	private int indexMax;

	public CommandList() {
		index = -1;
		indexMax = -1;
	}
	public void addCommand(Command c) {
		index ++;
		indexMax = index;
		if(commandList.size() - 1 > index)
			commandList.set(index, c);
		else
			commandList.add(index, c);
	}
	public void undo() {
		if(index > -1) {
			if (commandList.get(index).undoCommand());
				index --;
		}
	}
	public void redo() {
		if(index < indexMax) {
			index ++;
			commandList.get(index).doCommand();
		}
	}
}
