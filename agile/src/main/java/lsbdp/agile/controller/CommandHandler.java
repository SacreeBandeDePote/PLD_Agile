package lsbdp.agile.controller;

import javafx.util.Pair;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.model.*;

import java.util.Date;

public class CommandHandler {

	public static int lastDeletedIndex;

	/**
	 * Finds a pair when only knowing its delivery
	 * 
	 * @param schedule		the present route
	 * @param d				the delivery to find in the schedule
	 * @return the pair defining the requested delivery
	 */
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

	/**
	 * Deletes a delivery
	 * 
	 * @param map			the map being used
	 * @param schedule		the present route
	 * @param element		the delivery to delete
	 * @return the index where the deleted delivery was placed
	 */
	public static int deleteDelivery(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> element) {
		// Sauvegarde l'index de la delivery
		int index = schedule.indexOf(element);

		// Calcule la nouvelle route
		if (index < schedule.size() - 2) {
			Delivery endDelivery = schedule.get(index + 1).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, element.getKey().getStartingPoint(),
					endDelivery.getLocation());
			schedule.set(index + 1, new Pair<>(newRoute, endDelivery));
		}
		if (index == schedule.size() - 2) {
			Route endRoute = schedule.get(index + 1).getKey();
			Route newRoute = Dijkstra.performDijkstra(map, element.getKey().getStartingPoint(),
					endRoute.getEnd());
			schedule.set(index + 1, new Pair<>(newRoute, null));
		}
		// Enleve la delivery
		schedule.remove(index);
		return index;
	}
	
	/**
	 * Puts back a deleted delivery
	 * 
	 * @param map			the map being used
	 * @param schedule		the present route
	 * @param element		the deleted delivery to put back
	 * @param index			the index where the deleted delivery was placed
	 */
	public static void undoDelete(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> element, int index) {
		// Remet l'element de base
		schedule.add(index, element);
		// Recalcule la delivery suivante
		if (index < schedule.size() - 2) {
			Delivery startDelivery = schedule.get(index).getValue();
			Delivery endDelivery = schedule.get(index + 1).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation(), endDelivery.getLocation());
			schedule.set(index + 1, new Pair<>(newRoute, endDelivery));
		}
		if (index == schedule.size() - 2 && index != 0) {
			Delivery startDelivery = schedule.get(index).getValue();
			Route endRoute = schedule.get(index + 1).getKey();
			Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation(), endRoute.getEnd());
			schedule.set(index + 1, new Pair<>(newRoute, null));
		}
		if (index == 0) {
			Delivery startDelivery = schedule.get(index).getValue();
			Route newRoute = Dijkstra.performDijkstra(map, startDelivery.getLocation(), element.getKey().getStartingPoint());
			schedule.set(index + 1, new Pair<>(newRoute, null));
		}
	}

	/**
	 * Tests if it is possible to add a delivery and does so if possible
	 * 
	 * @param map			the map being used
	 * @param schedule		the present route
	 * @param d				the delivery to add
	 * @return true if succeeded in adding the delivery, false otherwise
	 */
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
					((prevDelivery != null)
							? (prevDelivery.getDeliveryTime().getTime() + prevDelivery.getDuration() * 1000) : schedule.getStartingTime().getTime()));
			d.setDeliveryTime(arrival);

			Date timeMin = d.getTimespanStart();
			Date timeMax = d.getTimespanEnd();
			Date delTimeRounded = new Date(arrival.getTime());
			long decaMinute = (delTimeRounded.getTime()) / (1000 * 60 * 10);
			decaMinute = Math.round(decaMinute);
			long millisec = decaMinute * (1000 * 60 * 10);
			delTimeRounded.setTime(millisec);

			Date timeStart = new Date(delTimeRounded.getTime() - 30 * 60000); // -30min
			Date timeEnd = new Date(delTimeRounded.getTime() + 30 * 60000); // +30min
			d.setTimespanStart(timeStart);
			d.setTimespanEnd(timeEnd);
			if (timeMin != null) {
				if (timeStart.compareTo(timeMin) < 0) {
					d.setTimespanStart(timeMin);
					Date newTime = new Date(timeMin.getTime() + 60 * 60000);
					d.setTimespanEnd(newTime); // on garde un créneau d'1h
				}

				if (timeEnd.compareTo(timeMax) > 0) {
					d.setTimespanEnd(timeMax);
					Date newTime = new Date(timeMax.getTime() - 60 * 60000);
					d.setTimespanStart(newTime); // on garde un créneau d'1h
				}
			}
			if (timeStart.compareTo(schedule.getStartingTime()) < 0) {
				d.setTimespanStart(schedule.getStartingTime());
				Date newTime = new Date(schedule.getStartingTime().getTime() + 60 * 60000);
				d.setTimespanEnd(newTime); // on garde un créneau d'1h
			}

			schedule.add(index, new Pair<>(route, d));
			return true;
		}
		return false;
	}

	/**
	 * Modifies the selected delivery by replacing it with a new one
	 * 
	 * @param map			the map being used
	 * @param schedule		the present route
	 * @param oldDelivery	the delivery that is going to be modified
	 * @param newDelivery	the new delivery
	 * @param sT				the starting time of the new delivery
	 * @param eT				the ending time of the new delivery
	 * @param duration		the duration of the new delivery
	 * @return the index where the old delivery was placed
	 */
	public static int modifyDelivery(StreetMap map, DeliverySchedule schedule, Pair<Route, Delivery> oldDelivery,
									 Delivery newDelivery, Date sT, Date eT, int duration) {
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

	public static void undoModify(StreetMap map, DeliverySchedule schedule, Delivery newDelivery,
								  Pair<Route, Delivery> oldDelivery, int index) {
		deleteDelivery(map, schedule, findByDelivery(schedule, newDelivery));
		undoDelete(map, schedule, oldDelivery, index);
	}

	/**
	 * @param map
	 * @param schedule the present route
	 * @param d        the new delivery to add
	 * @return the index where we put the new delivery or -1 if not possible
	 */
	private static int indexAdd(StreetMap map, DeliverySchedule schedule, Delivery d) {
		Date crtDate = (Date) schedule.getStartingTime().clone();

		for (Pair<Route, Delivery> elem : schedule) {
			Delivery next = elem.getValue();
			Route prevToNext = elem.getKey();
			Route prevToD = Dijkstra.performDijkstra(map, prevToNext.getStartingPoint(), d.getLocation());

			long timeOfArrival = crtDate.getTime() + ((long) prevToD.getTotalTime()) * 60000;

			if (d.getTimespanEnd() != null) {
				if ((timeOfArrival < d.getTimespanStart().getTime())
						|| ((timeOfArrival + d.getDuration() * 1000) > d.getTimespanEnd().getTime())) {
					if (next != null)
						crtDate.setTime(next.getDeliveryTime().getTime() + next.getDuration() * 1000);
					else
						return schedule.size() - 1;
					continue;
				}
			}

			if (next == null) {
				return schedule.size() - 1;
			}

			Route dToNext = Dijkstra.performDijkstra(map, next.getLocation(), d.getLocation());
			long newTime = timeOfArrival + ((long) dToNext.getTotalTime()) * 60000 + d.getDuration() * 1000;

			if (checkOffsetTime(schedule, next, newTime)) {
				return schedule.indexOf(elem);
			}
			crtDate.setTime(next.getDeliveryTime().getTime() + next.getDuration() * 1000);
		}

		return -1;
	}

	/**
	 * This recursive method check in cascade if we can move the time of arrival of the deliveries without exiting
	 * the time window given to the client
	 *
	 * @param schedule the present route
	 * @param current  the delivery on witch we check if its possible to move the time of arrival
	 * @param newTime  the new time where we arrive to the current delivery
	 * @return true if it is possible to move the the time of arrival of the deliveries
	 */
	private static boolean checkOffsetTime(DeliverySchedule schedule, Delivery current, long newTime) {
		if (current == null) {
			if (newTime < 18 * 60 * 60 * 1000) { // < 18h
				schedule.setEndingTime(new Date(newTime));
				return true;
			}

			return false;

		}
		if (newTime < current.getDeliveryTime().getTime()) {
			return true;
		} else if (newTime < current.getTimespanEnd().getTime()) {
			Pair<Route, Delivery> p = findByDelivery(schedule, current);
			Delivery next = schedule.get(schedule.indexOf(p) + 1).getValue();
			Route r = schedule.get(schedule.indexOf(p) + 1).getKey();
			long nextTime = newTime + (long) r.getTotalTime() * 60000 + current.getDuration() * 1000;
			if (checkOffsetTime(schedule, next, nextTime)) {
				current.setDeliveryTime(new Date(newTime));
				return true;
			}
			return false;
		} else
			return false;
	}

}
