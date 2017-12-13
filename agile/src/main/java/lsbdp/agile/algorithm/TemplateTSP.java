package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract class for the Template design pattern
 * Using a branch and bound algorithm to find the solution
 * The bound method and the iterator have to be defined by the extenders
 */
public abstract class TemplateTSP implements TSP {

	private Integer[] bestSolution;
	private Float[] timeOfArrival;
	private Float[] tempTimeOfArrival;
	protected float bestSolutionCost;
	private boolean solutionFound;

	/**
	 * The method first translate the model to completely generic data, then it feed the real algorithm
	 *
	 * @param schedule Must be null and will be created by the method
	 * @param map
	 * @param req
	 */
	@Override
	public void findSolution(DeliverySchedule schedule, StreetMap map, DeliveriesRequest req) {
		schedule.setStartingTime(req.getStartingTime());
		Intersection warehouse = req.getWarehouse();
		List<Delivery> deliveries = req.getDeliveryList();
		Route[][] graphTSP = Dijkstra.createTSPGraph(map, warehouse, deliveries);

		float[][] timeCost = createCostGraph(graphTSP, deliveries.size());

		float[] duration = createDurationTable(deliveries);

		Date start = req.getStartingTime();
		Pair<Float, Float>[] timeWindows = createTimeWindowsTable(deliveries, start);

		bestSolution = new Integer[deliveries.size() + 1];
		timeOfArrival = new Float[deliveries.size() + 1];
		tempTimeOfArrival = new Float[deliveries.size() + 1];
		bestSolutionCost = Float.MAX_VALUE;
		solutionFound = false;

		ArrayList<Integer> nonView = new ArrayList<>();
		for (int i = 0; i < deliveries.size(); i++)
			nonView.add(i);

		ArrayList<Integer> view = new ArrayList<>();
		view.add(timeCost.length - 1);

		branchAndBound(timeCost.length - 1, nonView, view, 0f, timeCost, duration, timeWindows);
		if (solutionFound) {
			for (int i = 0; i < bestSolution.length - 1; i++) {
				Delivery d = deliveries.get(bestSolution[i + 1]);
				Route r = graphTSP[bestSolution[i]][bestSolution[i + 1]];
				long diffTime = start.getTime() + (long) (timeOfArrival[bestSolution[i + 1]] * 60f * 1000f);
				Date delTime = new Date(diffTime);
				d.setDeliveryTime(delTime);

				Date timeMin = d.getTimespanStart();
				Date timeMax = d.getTimespanEnd();

				Date delTimeRounded = new Date(delTime.getTime());
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
				if (timeStart.compareTo(start) < 0) {
					d.setTimespanStart(start);
					Date newTime = new Date(start.getTime() + 60 * 60000);
					d.setTimespanEnd(newTime); // on garde un créneau d'1h
				}

				schedule.add(new Pair<>(r, d));
			}
			long diffTime = start.getTime() + (long) (timeOfArrival[graphTSP.length - 1] * 60f * 1000f);
			Date delTime = new Date(diffTime);
			schedule.setEndingTime(delTime);
			Route r = graphTSP[bestSolution[bestSolution.length - 1]][graphTSP.length - 1];
			schedule.add(new Pair<>(r, null));
		}
	}


	/**
	 * This method implements the different heuristic for exploring the solutions
	 *
	 * @param crtNode     actual delivery that is visited
	 * @param nonView     list of the non view nodes
	 * @param timeCost    the complete graph with the time duration in minutes of the route between each delivery
	 * @param duration    table containing all the delivery duration in minutes
	 * @param timeWindows table containing all the time window in minutes of each delivery (0 min is the start of the tour)
	 * @param crtCost     the current time passed in minutes from the start of the tour to the current node
	 * @return true if the heuristic is less than the best solution, meaning we continue to explore this branch
	 */
	protected abstract boolean bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows, float crtCost);

	/**
	 * This method implement the way of choosing the next node to explore in a given level of exploration
	 *
	 * @param crtNode     actual delivery that is visited
	 * @param nonView     list of the non view nodes
	 * @param timeCost    the complete graph with the time duration in minutes of the route between each delivery
	 * @param duration    table containing all the delivery duration in minutes
	 * @param timeWindows table containing all the time window in minutes of each delivery (0 min is the start of the tour)
	 * @return
	 */
	protected abstract Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows);

	/**
	 * This method is the heart of the algorithm, it implements a basic branch and bound algorithm
	 *
	 * @param crtNode     actual delivery that is visited
	 * @param nonView     list of the non view nodes
	 * @param view        list of all the viewed nodes
	 * @param crtCost     the current time passed in minutes from the start of the tour to the current node
	 * @param timeCost    the complete graph with the time cost of the route between each delivery
	 * @param duration    table containing all the delivery duration
	 * @param timeWindows table containing all the given time window of each delivery (start and end)
	 */
	private void branchAndBound(int crtNode, ArrayList<Integer> nonView, ArrayList<Integer> view, float crtCost, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		//When i visit a node, it means that i manage to get to the node and that it is possible to get there. the crtCost has been increment by its duration
		if (nonView.isEmpty()) { //this is the last node that has been visited
			crtCost += timeCost[crtNode][timeCost.length - 1]; //we have to go back to the warehouse
			if (crtCost < bestSolutionCost) { //we find a better solution
				bestSolutionCost = crtCost;
				view.toArray(bestSolution);
				timeOfArrival = tempTimeOfArrival.clone();
				timeOfArrival[timeOfArrival.length - 1] = bestSolutionCost;
				solutionFound = true;
			}
		} else {
			//there are still nodes to visit
			if (bound(crtNode, nonView, timeCost, duration, timeWindows, crtCost)) {
				Iterator<Integer> it = iterator(crtNode, nonView, timeCost, duration, timeWindows);
				while (it.hasNext()) {
					Integer nextNode = it.next();
					//TODO : ask the teacher if the the duration have to be in the time window
					//TODO : answer -> we take account of the duration of delivery
					if (timeWindows[nextNode] != null)
						if (crtCost + timeCost[crtNode][nextNode] + duration[nextNode] > timeWindows[nextNode].getValue()) //do we arrive to late for the delivery
							continue; // not useful to explore this branch

					view.add(nextNode);
					nonView.remove(nextNode);
					float newCrtCost = crtCost + timeCost[crtNode][nextNode];
					if (timeWindows[nextNode] != null)
						if (newCrtCost < timeWindows[nextNode].getKey()) //if we arrive before the beginning of a time window, we wait !
							newCrtCost = timeWindows[nextNode].getKey();

					tempTimeOfArrival[nextNode] = newCrtCost;
					newCrtCost += duration[nextNode];
					branchAndBound(nextNode, nonView, view, newCrtCost, timeCost, duration, timeWindows);
					view.remove(nextNode);
					nonView.add(nextNode);
				}
			}
		}
	}

	/**
	 * @param routeGraph   a complete graph of shortest Route between the deliveries given by Dijkstra.createTSPGraph
	 * @param nbDeliveries
	 * @return a complete graph of the time duration in minutes of the route between the deliveries
	 */
	private float[][] createCostGraph(Route[][] routeGraph, int nbDeliveries) {
		float[][] timeCost = new float[nbDeliveries + 1][nbDeliveries + 1];
		for (int i = 0; i < timeCost.length; i++) {
			for (int j = 0; j < timeCost.length; j++) {
				if (i == j) {
					timeCost[i][j] = 0f;
				} else {
					timeCost[i][j] = routeGraph[i][j].getTotalTime();
				}
			}
		}
		return timeCost;
	}

	/**
	 * @param deliveries
	 * @return a table with the duration in minutes of all the deliveries
	 */
	private float[] createDurationTable(List<Delivery> deliveries) {
		float[] duration = new float[deliveries.size() + 1]; //minutes
		for (int i = 0; i < duration.length - 1; i++) {
			duration[i] = deliveries.get(i).getDuration() / 60f;
		}
		duration[duration.length - 1] = 0f;

		return duration;
	}

	/**
	 * @param deliveries list of the client deliveries
	 * @param start      time where we begin the tour
	 * @return Pairs of starting and ending of the time windows in minutes windows given by the client (the origin of time is the start of the rout)
	 */
	private Pair<Float, Float>[] createTimeWindowsTable(List<Delivery> deliveries, Date start) {
		Pair<Float, Float>[] timeWindows = new Pair[deliveries.size() + 1];
		for (int i = 0; i < timeWindows.length - 1; i++) {
			Date timeSpanStart = deliveries.get(i).getTimespanStart();
			Date timeSpanEnd = deliveries.get(i).getTimespanEnd();

			if (timeSpanEnd == null) {
				timeWindows[i] = null;
				continue;
			}
			float startSpan = (timeSpanStart.getTime() - start.getTime()) / (1000f * 60f); //ms to minutes
			float endSpan = (timeSpanEnd.getTime() - start.getTime()) / (1000f * 60f); //ms to minutes

			timeWindows[i] = new Pair<>(startSpan, endSpan);
		}
		timeWindows[timeWindows.length - 1] = null;

		return timeWindows;
	}

	/**
	 * @param nonView     list of the non view nodes
	 * @param timeWindows table containing all the given time window of each delivery (start and end)
	 * @param crtCost     the current time passed in minutes from the start of the tour to the current node
	 * @return false if we are already late to any remaining delivery
	 */
	protected boolean checkTimePossible(ArrayList<Integer> nonView, Pair<Float, Float>[] timeWindows, float crtCost) {
		for (Integer s : nonView) {
			if (timeWindows[s] == null) {
			} else if (timeWindows[s].getValue() < crtCost) {
				return false;
			}
		}
		return true;
	}
}
