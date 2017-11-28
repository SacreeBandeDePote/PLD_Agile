package lsbdp.agile.controller;

import java.sql.Time;

import lsbdp.agile.model.Delivery;

public class CommandModify implements Command {
	private Delivery delivery;
	private Time td;
	private Time tf;
	
	public CommandModify(Delivery d, Time td, Time tf) {
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
