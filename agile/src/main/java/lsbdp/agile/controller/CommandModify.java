package lsbdp.agile.controller;

import java.util.Date;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Route;

public class CommandModify implements Command {
	private Pair<Route, Delivery> oldDelivery;
	private Delivery newDelivery;
	private Date startTime;
	private Date endTime;
	private int duration;
	private int index;
	
	public CommandModify(Delivery d, Date sT, Date eT, int duration) {
		this.oldDelivery = CommandHandler.findByDelivery(Controller.getSchedule(), d);
		this.startTime = sT;
		this.endTime = eT;
		if(duration == 0)
			this.duration = d.getDuration();
		else
			this.duration = duration;
	}
	
	@Override
	public boolean doCommand() {
		index = CommandHandler.modifyDelivery(Controller.getMap(), Controller.getSchedule(), oldDelivery, newDelivery, startTime, endTime, duration);
		if(index == -1) {
			index = CommandHandler.lastDeletedIndex;
			CommandHandler.undoDelete(Controller.getMap(), Controller.getSchedule(), oldDelivery, index);
			Controller.openErrorPopUp("Modification impossible");
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean undoCommand() {
		CommandHandler.undoModify(Controller.getMap(), Controller.getSchedule(), newDelivery, oldDelivery, index);
		return true;
	}

}
