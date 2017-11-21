package lsbdp.agile.controller;

import java.sql.Time;

public class CmdAdd extends Command {
	private String adresse;
	private Time td;
	private Time tf;
	private int i;
	
	public CmdAdd(String adresse, Time td, Time tf, int i) {
		super();
		this.adresse = adresse;
		this.td = td;
		this.tf = tf;
		this.i = i;
	}
	public void doCommand() {
	}
	public void undoCommand() {
	}
}
