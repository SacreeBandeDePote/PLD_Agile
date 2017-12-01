package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import javax.sound.midi.Soundbank;
import java.lang.reflect.Array;
import java.util.*;

public class TemplateTSP implements TSP {

	private Integer[] bestSolution;
	private Float[] arrivalTime;
	private Float[] tempArrivalTime;
	private float bestSolutionCost;
	private boolean solutionFound;

	@Override
	public void findSolution(DeliverySchedule schedule, StreetMap map, DeliveriesRequest req) {
		Intersection warehouse = req.getWarehouse();
		List<Delivery> deliveries = req.getDeliveryList();
		Route[][] graphTSP = Dijkstra.createTSPGraph(map, warehouse, deliveries);

		float[][] timeCost = createCostGraph(graphTSP, deliveries.size());

		float[] duration = createDurationTable(deliveries);

		Date start = req.getStartingTime();
		Pair<Float, Float>[] timeWindows = createTimeWindowsTable(deliveries, start);

		bestSolution = new Integer[deliveries.size() + 1];
		bestSolutionCost = Float.MAX_VALUE;
		solutionFound = false;
		arrivalTime = new Float[deliveries.size()+1];
		tempArrivalTime = new Float[deliveries.size()+1];

		ArrayList<Integer> nonView = new ArrayList<>();
		for (int i = 0; i < deliveries.size(); i++)
			nonView.add(i);

		ArrayList<Integer> view = new ArrayList<>();
		view.add(timeCost.length - 1);

		branchAndBound(timeCost.length - 1, nonView, view, 0f, timeCost, duration, timeWindows);

		if (solutionFound) {
			System.out.println("Best Solution found : cost : " + bestSolutionCost);
			System.out.println(Arrays.toString(bestSolution));
			System.out.println(Arrays.toString(arrivalTime));

			for (int i = 0; i < bestSolution.length - 1; i++) {
				Delivery d = deliveries.get(bestSolution[i + 1]);
				Route r = graphTSP[bestSolution[i]][bestSolution[i + 1]];
				schedule.add(new Pair<>(r, d));
			}
		} else {
			System.out.println("No Solution Found");
		}
		//TODO : What to do with the come back to the warehouse ??
	}


	private boolean bound(int crtNode, ArrayList<Integer> nonView, float crtCost, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return crtCost < bestSolutionCost;
	}

	private Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new SeqIterator(nonView);
	}

	/*
		When i visit a node, it means that i manage to get to the node and that it is possible to get there. the crtCost has been increment by its duration
		 */
	private void branchAndBound(int crtNode, ArrayList<Integer> nonView, ArrayList<Integer> view, float crtCost, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		System.out.println("Time of arrival in " + crtNode + " : " + crtCost);
		if (nonView.isEmpty()) { //this is the last node that has been visited
			crtCost += timeCost[crtNode][timeCost.length - 1]; //we have to go back to the warehouse
			tempArrivalTime[tempArrivalTime.length-1] = crtCost;
			System.out.println("Solution found : cost : " + crtCost);
			if (crtCost < bestSolutionCost) { //we find a better solution
				arrivalTime = tempArrivalTime.clone();

				view.toArray(bestSolution);
				bestSolutionCost = crtCost;
				solutionFound = true;
				System.out.println("New Best Solution found : cost : " + bestSolutionCost);
				System.out.println(Arrays.toString(bestSolution));
			}
		} else {
			if (bound(crtNode, nonView, crtCost, timeCost, duration, timeWindows)) { //there are still nodes to visit
				Iterator<Integer> it = iterator(crtNode, nonView, timeCost, duration, timeWindows);
				while (it.hasNext()) {
					Integer nextNode = it.next();
					//TODO : ask the teacher if the the duration have to be in the time window
					if (timeWindows[nextNode] != null) {
						if (crtCost + timeCost[crtNode][nextNode] + duration[nextNode] > timeWindows[nextNode].getValue()) { //do we arrive to late for the delivery
							System.out.println("we arrive to late to the delivery " + nextNode);
							continue; // not useful to explore this branch
						}
					}
					view.add(nextNode);
					nonView.remove(nextNode);
					float newCrtCost = crtCost + timeCost[crtNode][nextNode] + duration[nextNode];
					if (timeWindows[nextNode] != null) {
						if (newCrtCost < timeWindows[nextNode].getKey()) { //if we arrive before the beginning of a time window, we wait !
							System.out.println("We arrive too early, actual time : " + newCrtCost);
							newCrtCost = timeWindows[nextNode].getKey();
						}
					}
					tempArrivalTime[nextNode] = newCrtCost;
					branchAndBound(nextNode, nonView, view, newCrtCost, timeCost, duration, timeWindows);
					view.remove(nextNode);
					nonView.add(nextNode);
				}
			} else {
				System.out.println("No use to explore this branch");
			}
		}
	}

	public Integer[] getBestSolution() {
		return bestSolution;
	}

	public Float[] getArrivalTime() {
		return arrivalTime;
	}

	public float getBestSolutionCost() {
		return bestSolutionCost;
	}

	public boolean isSolutionFound() {
		return solutionFound;
	}

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

	private float[] createDurationTable(List<Delivery> deliveries) {
		float[] duration = new float[deliveries.size() + 1]; //minutes
		for (int i = 0; i < duration.length - 1; i++) {
			duration[i] = deliveries.get(i).getDuration() / 60f;
		}
		duration[duration.length - 1] = 0f;

		return duration;
	}

	private Pair<Float, Float>[] createTimeWindowsTable(List<Delivery> deliveries, Date start) {
		Pair<Float, Float>[] timeWindows = new Pair[deliveries.size() + 1];
		for (int i = 0; i < timeWindows.length - 1; i++) {
			Date timeSpanStart = deliveries.get(i).getTimespanStart();
			Date timeSpanEnd = deliveries.get(i).getTimespanEnd();

			if (timeSpanEnd == null) {
				timeWindows[i] = null;
				break;
			}
			float startSpan = (timeSpanStart.getTime() - start.getTime()) / (1000f * 60f); //ms to minutes
			float endSpan = (timeSpanEnd.getTime() - start.getTime()) / (1000f * 60f); //ms to minutes

			timeWindows[i] = new Pair<>(startSpan, endSpan);
		}
		timeWindows[timeWindows.length - 1] = null;

		return timeWindows;
	}

	private Delivery getDel(List<Delivery> deliveries, Intersection target) {
		for (Delivery del : deliveries) {
			if (del.getLocation().equals(target))
				return del;
		}
		return null;
	}
}
