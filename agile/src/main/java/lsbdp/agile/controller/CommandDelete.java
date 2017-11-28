package lsbdp.agile.controller;

import javafx.util.Pair;
import lsbdp.agile.algorithm.CommandHandler;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;

public class CommandDelete implements Command {
	private Pair<Route, Delivery> element;
	private int index;
	//private Pair<Route, Delivery> nextElement;
	
	public CommandDelete(Delivery d) {
		this.element = p;
	}
	
	@Override
	public boolean doCommand() {
		index = CommandHandler.deleteDelivery(Controller.getMap(), Controller.getSchedule(), element);
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		CommandHandler.undoDelete(Controller.getMap(), Controller.getSchedule(), element, index);
		return true;
	}

}