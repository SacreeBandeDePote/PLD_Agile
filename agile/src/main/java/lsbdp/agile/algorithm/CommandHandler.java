package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;

public class CommandHandler {

	public static int deleteDelivery(DeliverySchedule schedule, Pair<Route, Delivery> element) {
		//Enleve un Delivery d'une DeliverySchedule, prendre ses voisins et calculer leur plus court chemin, et changer la route
		
		int index = schedule.indexOf(element);
		Pair <Route, Delivery> newElement;
		
		Delivery startDelivery = schedule.get(index-1).getValue();
		Delivery endDelivery = schedule.get(index+1).getValue();
		Route newRoute = Dijkstra.performDijkstra(startDelivery , endDelivery);
		
		schedule.set(index+1, new Pair<>(newRoute, endDelivery));
		schedule.remove(index);
		return index;
	}

}
