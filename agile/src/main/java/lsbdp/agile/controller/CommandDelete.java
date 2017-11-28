package lsbdp.agile.controller;

import javafx.util.Pair;
import lsbdp.agile.algorithm.CommandHandler;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;

public class CommandDelete implements Command {
	private DeliverySchedule schedule;
	private Pair<Route, Delivery> element;
	private int index;
	//private Pair<Route, Delivery> nextElement;
	
	public CommandDelete(DeliverySchedule s, Pair<Route, Delivery> p) {
		this.schedule = s;
		this.element = p;
	}
	
	@Override
	public boolean doCommand() {
		index = CommandHandler.deleteDelivery(Controller.getMap(), schedule, element);
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		CommandHandler.undoDelete(Controller.getMap(), schedule, element, index);
		return true;
	}

}