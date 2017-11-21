package lsbdp.agile.controller;

import lsbdp.agile.model.Delivery;

public class CommandDelete extends Command {
	private Delivery delivery;
	
	public CommandDelete(Delivery d) {
		this.delivery = d;
	}
	
	@Override
	public boolean doCommand() {
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		return true;
	}

}