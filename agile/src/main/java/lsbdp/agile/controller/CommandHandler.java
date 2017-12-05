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
		if(index < schedule.size() - 1) {
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
		Delivery startDelivery = schedule.get(index).getValue();
		Delivery endDelivery = schedule.get(index+1).getValue();
		Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation() , endDelivery.getLocation());
		
		schedule.set(index+1, new Pair<>(newRoute, endDelivery));
	}
	
	//Handler CommandAdd
	public static void addDelivery(StreetMap map, DeliverySchedule schedule, Delivery d) {
		int index = 2;
		Delivery prevDelivery = schedule.get(index-1).getValue();
		Delivery nextDelivery = schedule.get(index).getValue();
		
		Route route = Dijkstra.performDijkstra(map, prevDelivery.getLocation(), d.getLocation());
		Route nextRoute = Dijkstra.performDijkstra(map, d.getLocation(), nextDelivery.getLocation());
		
		schedule.set(index, new Pair<>(nextRoute, nextDelivery));
		schedule.add(index, new Pair<>(route, d));
		
	}
	
	//Handler CommandModify
	public static void modifyDelivery(StreetMap map, DeliverySchedule schedule, Delivery d, Date sT, Date eT, int duration) {
		deleteDelivery(map, schedule, findByDelivery(schedule, d));
		Delivery newDelivery = new Delivery(duration, sT, eT, d.getLocation(), null);
		addDelivery(map, schedule, newDelivery);
	}
	public static void undoModify() {
		
	}

}
