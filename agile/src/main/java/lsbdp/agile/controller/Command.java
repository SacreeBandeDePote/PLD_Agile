package lsbdp.agile.controller;

public abstract class Command {
	public boolean doCommand() {
		return false;
	}
	public boolean undoCommand() {
		return false;
	}
}
