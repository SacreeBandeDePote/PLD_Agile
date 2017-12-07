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

	public static int lastDeletedIndex;

	//General Handler
	public static Pair<Route, Delivery> findByDelivery(DeliverySchedule schedule, Delivery d) {
		for (Pair<Route, Delivery> p : schedule) {
			if (p.getValue() == d)
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
		if (index < schedule.size() - 2) {
			Delivery endDelivery = schedule.get(index + 1).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, element.getKey().getStartingPoint(), endDelivery.getLocation());
			schedule.set(index + 1, new Pair<>(newRoute, endDelivery));
			//Enleve la delivery
		}
		schedule.remove(index);
		return index;
	}

	public static void undoDelete(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> element, int index) {
		//Remet l'element de base
		schedule.add(index, element);
		//Recalcule la delivery suivante
		if (index < schedule.size() - 2) {
			Delivery startDelivery = schedule.get(index).getValue();
			Delivery endDelivery = schedule.get(index + 1).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation(), endDelivery.getLocation());
			schedule.set(index + 1, new Pair<>(newRoute, endDelivery));
		}
	}

	//Handler CommandAdd
	public static boolean addDelivery(StreetMap map, DeliverySchedule schedule, Delivery d) {
		int index = indexAdd(map, schedule, d);
		if (index >= 0) {
			Delivery nextDelivery = schedule.get(index).getValue();
			Delivery prevDelivery = (index == 0) ? null : schedule.get(index - 1).getValue();
			Intersection start = (index == 0) ? schedule.getWarehouse() : prevDelivery.getLocation();
			Intersection end = (index == schedule.size() - 1) ? schedule.getWarehouse() : nextDelivery.getLocation();


			Route route = Dijkstra.performDijkstra(map, start, d.getLocation());
			Route nextRoute = Dijkstra.performDijkstra(map, d.getLocation(), end);

			schedule.set(index, new Pair<>(nextRoute, nextDelivery));
			Date arrival = new Date(((long) route.getTotalTime()) * 60000 +
					((prevDelivery != null) ? (prevDelivery.getDeliveryTime().getTime() + prevDelivery.getDuration() * 1000) : 0));
			d.setDeliveryTime(arrival);
			schedule.add(index, new Pair<>(route, d));
			return true;
		}
		return false;
	}

	//Handler CommandModify
	public static int modifyDelivery(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> oldDelivery, Delivery newDelivery, Date sT, Date eT, int duration) {
		int index = deleteDelivery(map, schedule, oldDelivery);
		lastDeletedIndex = index;
		newDelivery = new Delivery(duration, sT, eT, oldDelivery.getValue().getLocation(), null);
		boolean addIsOk = addDelivery(map, schedule, newDelivery);
		if (!addIsOk) {
			return -1;
		} else {
			return index;
		}
	}

	public static void undoModify(StreetMap map, DeliverySchedule schedule, Delivery newDelivery, Pair<Route, Delivery> oldDelivery, int index) {
		deleteDelivery(map, schedule, findByDelivery(schedule, newDelivery));
		undoDelete(map, schedule, oldDelivery, index);
	}

	private static int indexAdd(StreetMap map, DeliverySchedule schedule, Delivery d) {
		Date crtDate = schedule.getStartingTime();

		for (Pair<Route, Delivery> elem : schedule) {
			Delivery next = elem.getValue();
			Route prevToNext = elem.getKey();
			Route prevToD = Dijkstra.performDijkstra(map, prevToNext.getStartingPoint(), d.getLocation());

			long timeOfArrival = crtDate.getTime() + ((long) prevToD.getTotalTime()) * 60000;

			if (d.getTimespanEnd() != null) {
				if ((timeOfArrival < d.getTimespanStart().getTime()) || ((timeOfArrival + d.getDuration() * 1000) > d.getTimespanEnd().getTime())) {
					if(next != null)
						crtDate.setTime(next.getDeliveryTime().getTime() + next.getDuration() * 1000);
					continue;
				}
			}
			if (next == null) {
				return schedule.size() - 1;
			}
			Route dToNext = Dijkstra.performDijkstra(map, next.getLocation(), d.getLocation());
			long newTime = timeOfArrival + ((long) dToNext.getTotalTime()) * 60000;

			if (newTime <= next.getDeliveryTime().getTime()) {
				return schedule.indexOf(elem);
			}
			crtDate.setTime(next.getDeliveryTime().getTime() + next.getDuration() * 1000);
		}

		return -1;
	}

}
