package lsbdp.agile.controller;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;

public class CommandDelete extends Command {
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
		//Enleve un Delivery d'une DeliverySchedule, prendre ses voisins et calculer leur plus court chemin, et changer la route
		//algo.deleteDelivery(d);
		 /*
		index = schedule.indexOf(element);
		Pair < Route, Delivery > newElement;
		
		startDelivery = schedule.get(index-1).getRight();
		endDelivery = schedule.get(index+1).getRight();
		newRoute = algo.compute ( startDelivery , endDelivery );
		
		schedule.set(index+1,Pair<newRoute, endDelivery>);
		schedule.remove(index);
		*/
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		//
		return true;
	}

}