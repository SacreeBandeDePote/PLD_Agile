package lsbdp.agile.controller;

public interface Command {
	public boolean doCommand();
	public boolean undoCommand();
}
