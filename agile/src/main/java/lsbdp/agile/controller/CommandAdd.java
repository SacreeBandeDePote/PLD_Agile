package lsbdp.agile.controller;

import java.sql.Time;

public class CommandAdd extends Command {
	private String adresse;
	private Time td;
	private Time tf;
	private int i;
	
	public CommandAdd(String adresse, Time td, Time tf, int i) {
		super();
		this.adresse = adresse;
		this.td = td;
		this.tf = tf;
		this.i = i;
	}
	
	@Override
	public void doCommand() {
	}
	
	@Override
	public void undoCommand() {
	}
}
