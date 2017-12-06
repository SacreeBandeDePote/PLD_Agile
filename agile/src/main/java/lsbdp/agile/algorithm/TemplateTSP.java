package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.lang.reflect.Array;
import java.util.*;

public abstract class TemplateTSP implements TSP {

	private Integer[] bestSolution;
	private Float[] timeOfArrival;
	private Float[] tempTimeOfArrival;
	protected float bestSolutionCost;
	private boolean solutionFound;

	private int count;

	@Override
	public void findSolution(DeliverySchedule schedule, StreetMap map, DeliveriesRequest req) {
		schedule.setStartingTime(req.getStartingTime());
		count = 0;
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
				long diffTime = start.getTime() + (long)(timeOfArrival[bestSolution[i + 1]]*60f*1000f);
				Date delTime = new Date(diffTime);
				d.setDeliveryTime(delTime);
				schedule.add(new Pair<>(r, d));
			}
			long diffTime = start.getTime() + (long)(timeOfArrival[graphTSP.length - 1]*60f*1000f);
			Date delTime = new Date(diffTime);
			schedule.setEndingTime(delTime);
			Route r = graphTSP[bestSolution[bestSolution.length - 1]][graphTSP.length - 1];
			schedule.add(new Pair<>(r, null));
		}

		//TODO : What to do with the come back to the warehouse ??
	}


	protected abstract float bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows, float crtCost);

	protected abstract Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows);

	/*
	When i visit a node, it means that i manage to get to the node and that it is possible to get there. the crtCost has been increment by its duration
	 */
	private void branchAndBound(int crtNode, ArrayList<Integer> nonView, ArrayList<Integer> view, float crtCost, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		count++;
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
			if (crtCost + bound(crtNode, nonView, timeCost, duration, timeWindows, crtCost) < bestSolutionCost) { //there are still nodes to visit
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
				continue;
			}
			float startSpan = (timeSpanStart.getTime() - start.getTime()) / (1000f * 60f); //ms to minutes
			float endSpan = (timeSpanEnd.getTime() - start.getTime()) / (1000f * 60f); //ms to minutes

			timeWindows[i] = new Pair<>(startSpan, endSpan);
		}
		timeWindows[timeWindows.length - 1] = null;

		return timeWindows;
	}
}
