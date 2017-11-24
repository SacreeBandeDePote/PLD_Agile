package lsbdp.agile.controller;

import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;

public class CommandDelete extends Command {
	private DeliverySchedule schedule;
	private Delivery delivery;
	
	public CommandDelete(Delivery d, DeliverySchedule s) {
		this.schedule = s;
		this.delivery = d;
	}
	
	@Override
	public boolean doCommand() {
		//algo.deleteDelivery(d);
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		return true;
	}

}