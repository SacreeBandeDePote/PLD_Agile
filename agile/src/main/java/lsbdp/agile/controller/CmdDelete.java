package lsbdp.agile.controller;

import lsbdp.agile.model.Delivery;

public class CmdDelete extends Command {
	private Delivery delivery;
	
	public CmdDelete(Delivery d) {
		this.delivery = d;
	}
	public void doCmd() {
	}
	public void undoCmd() {
	}

}