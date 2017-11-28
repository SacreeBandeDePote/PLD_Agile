package lsbdp.agile.controller;

import java.util.Date;

import lsbdp.agile.model.Delivery;

public class CommandModify implements Command {
	private Delivery delivery;
	private Date td;
	private Date tf;
	
	public CommandModify(Delivery d, Date td, Date tf) {
		this.delivery = d;
		this.td = td;
		this.tf = tf;
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
