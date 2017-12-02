package lsbdp.agile.controller;

import java.sql.Time;

import javafx.util.Pair;
import lsbdp.agile.algorithm.CommandHandler;

import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.StreetMap;
import lsbdp.agile.model.Route;

public class CommandAdd implements Command {
	private String adresse;
	private Time td;
	private Time tf;
	private int i;
	private Delivery d;
	
	public CommandAdd(String adresse, Time td, Time tf, int i) {
		super();
		this.adresse = adresse;
		this.td = td;
		this.tf = tf;
		this.i = i;
	}
	
	public CommandAdd(Delivery d) {
		this.d = d;
	}
	
	@Override
	public boolean doCommand() {
		CommandHandler.addDelivery(Controller.getMap(), Controller.getSchedule(), d);
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		Pair <Route, Delivery> element = CommandHandler.findByDelivery(Controller.getSchedule(), d);
		CommandHandler.deleteDelivery(Controller.getMap(), Controller.getSchedule(), element);
		return true;
	}
}
