package lsbdp.agile.controller;

import java.util.Date;

import lsbdp.agile.model.Delivery;

public class CommandModify implements Command {
	private Delivery delivery;
	private Date startTime;
	private Date endTime;
	private int duration;
	
	public CommandModify(Delivery d, Date sT, Date eT, int duration) {
		this.delivery = d;
		this.startTime = sT;
		this.endTime = eT;
		if(duration == 0)
			this.duration = d.getDuration();
		else
			this.duration = duration;
	}
	
	@Override
	public boolean doCommand() {
		CommandHandler.modifyDelivery(Controller.getMap(), Controller.getSchedule(), delivery, startTime, endTime, duration);
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		return true;
	}

}
