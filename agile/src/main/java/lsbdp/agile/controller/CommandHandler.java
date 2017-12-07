package lsbdp.agile.controller;

import java.util.Date;

import javafx.util.Pair;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

public class CommandHandler {

	//General Handler
	public static Pair<Route, Delivery> findByDelivery(DeliverySchedule schedule, Delivery d) {
		for(Pair<Route, Delivery> p : schedule) {
			if(p.getValue() == d)
				return p;
		}
		return null;
	}
	public boolean isOK(DeliverySchedule schedule, Delivery delivery) {
		
		return true;
	}
	
	//Handler CommandDelete
	public static int deleteDelivery(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> element) {
		//Sauvegarde l'index de la delivery
		int index = schedule.indexOf(element);
		
		//Calcule la nouvelle route
		if(index < schedule.size() - 2) {
			Delivery endDelivery = schedule.get(index+1).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, element.getKey().getStartingPoint() , endDelivery.getLocation());
			schedule.set(index+1, new Pair<>(newRoute, endDelivery));
			//Enleve la delivery
		}
		schedule.remove(index);
		return index;
	}
	public static void undoDelete(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> element, int index) {
		//Remet l'element de base
		schedule.add(index, element);
		//Recalcule la delivery suivante
		if(index < schedule.size() - 2) {
			Delivery startDelivery = schedule.get(index).getValue();
			Delivery endDelivery = schedule.get(index+1).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation() , endDelivery.getLocation());
			schedule.set(index+1, new Pair<>(newRoute, endDelivery));
		}
	}
	
	//Handler CommandAdd
	public static void addDelivery(StreetMap map, DeliverySchedule schedule, Delivery d) {
		int index = 2;
		Delivery prevDelivery = schedule.get(index-1).getValue();
		Delivery nextDelivery = schedule.get(index).getValue();
		
		Route route = Dijkstra.performDijkstra(map, prevDelivery.getLocation(), d.getLocation());
		Route nextRoute = Dijkstra.performDijkstra(map, d.getLocation(), nextDelivery.getLocation());
		
		schedule.set(index, new Pair<>(nextRoute, nextDelivery));
		//d.setDeliveryTime(prevDelivery.getDeliveryTime().getTime() + (long) prevDelivery.getDuration() + (long) route.getTotalTime());
		schedule.add(index, new Pair<>(route, d));
	}
	
	//Handler CommandModify
	public static int modifyDelivery(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> oldDelivery, Delivery newDelivery, Date sT, Date eT, int duration) {
		int index = deleteDelivery(map, schedule, oldDelivery);
		newDelivery = new Delivery(duration, sT, eT, oldDelivery.getValue().getLocation(), null);
		addDelivery(map, schedule, newDelivery);
		return index;
	}
	public static void undoModify(StreetMap map, DeliverySchedule schedule, Delivery newDelivery, Pair<Route, Delivery> oldDelivery, int index) {
		deleteDelivery(map, schedule, findByDelivery(schedule, newDelivery));
		undoDelete(map, schedule, oldDelivery, index);
	}

}
