package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

public class CommandHandler {

	//Handler CommandDelete
	public static Pair<Route, Delivery> findByDelivery(DeliverySchedule schedule, Delivery d) {
		for(Pair<Route, Delivery> p : schedule) {
			if(p.getValue() == d)
				return p;
		}
		return null;
	}
	public static int deleteDelivery(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> element) {
		//Sauvegarde l'index de la delivery
		int index = schedule.indexOf(element);
		
		//Calcule la nouvelle route
		Delivery startDelivery = schedule.get(index-1).getValue();
		Delivery endDelivery = schedule.get(index+1).getValue();
		Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation() , endDelivery.getLocation());
		
		schedule.set(index+1, new Pair<>(newRoute, endDelivery));
		//Enleve la delivery
		schedule.remove(index);
		return index;
	}
	public static void undoDelete(StreetMap map,DeliverySchedule schedule, Pair<Route, Delivery> element, int index) {
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
		Delivery firstDelivery = schedule.get(index-1).getValue();
		Delivery nextDelivery = schedule.get(index).getValue();
		
		Route firstRoute = Dijkstra.performDijkstra(map, firstDelivery.getLocation(), d.getLocation());
		
		Route nextRoute = Dijkstra.performDijkstra(map, d.getLocation(), nextDelivery.getLocation());
		
		schedule.add(index-1, new Pair<>(firstRoute, d));
		schedule.add(index, new Pair<>(nextRoute,nextDelivery));
		
	}
	public static void undoAdd() {
		
	}
	
	//Handler CommandModify
	public static void modifyDelivery() {
		
	}
	public static void undoModify() {
		
	}

}
