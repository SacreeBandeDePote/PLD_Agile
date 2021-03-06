package lsbdp.agile.controller;


import javafx.util.Pair;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.Delivery;

public class CommandAdd implements Command {
	private Delivery delivery;
	
	public CommandAdd(Delivery d) {
		this.delivery = d;
	}
	
	@Override
	public boolean doCommand() {
		boolean ret = CommandHandler.addDelivery(Controller.getMap(), Controller.getSchedule(), delivery);
		if (!ret) Controller.openErrorPopUp("Cannot emplace this delivery");
		return ret;
	}
	
	@Override
	public boolean undoCommand() {
		Pair <Route, Delivery> element = CommandHandler.findByDelivery(Controller.getSchedule(), delivery);
		CommandHandler.deleteDelivery(Controller.getMap(), Controller.getSchedule(), element);
		return true;
	}
}
