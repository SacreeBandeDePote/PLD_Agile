package lsbdp.agile.controller;

import java.sql.Time;

import lsbdp.agile.model.Delivery;

public class CmdModify extends Command {
	private Delivery delivery;
	private Time td;
	private Time tf;
	
	public CmdModify(Delivery d, Time td, Time tf) {
		this.delivery = d;
		this.td = td;
		this.tf = tf;
	}
	public void doCmd() {
	}
	public void undoCmd() {
	}

}
