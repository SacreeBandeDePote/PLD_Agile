package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * A fast TSP with a non admissible heuristic. It will not necessarily give the best solution but it will give it quickly
 */
public class NNHTSP extends NaiveTSP {

	/**
	 * The heuristic always pass through the nearest remaining node and sum the cost. For the return to the warehouse,
	 * It take the route from ,the nearest non view delivery from the warehouse, to the warehouse.
	 * It i not admissible but give good solution.
	 *
	 * @param crtNode     actual delivery that is visited
	 * @param nonView     list of the non view nodes
	 * @param timeCost    the complete graph with the time duration in minutes of the route between each delivery
	 * @param duration    table containing all the delivery duration in minutes
	 * @param timeWindows table containing all the time window in minutes of each delivery (0 min is the start of the tour)
	 * @param crtCost     the current time passed in minutes from the start of the tour to the current node
	 * @return true if the heuristic is less than the best solution, meaning we continue to explore this branch
	 */
	@Override
	protected boolean bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows, float crtCost) {
		ArrayList<Integer> toVisit = new ArrayList<>(nonView);
		int nextIndex;
		int index = crtNode;
		float sum = 0.f;
		for (int i = 0; i < nonView.size(); i++) {
			nextIndex = getShortestRoute(timeCost[index], toVisit);
			sum += timeCost[index][nextIndex] + duration[nextIndex];
			index = nextIndex;
		}
		//ajout du chemin le plus court vers le warehouse
		sum += shortestToWarehouse(nonView, timeCost);

		return (sum + crtCost) < bestSolutionCost;
	}

	protected int getShortestRoute(float[] timeCostCrtNode, ArrayList<Integer> nonView) {
		int index = nonView.get(0);
		float min = timeCostCrtNode[index];
		int i = -1;
		int indexNonView = 0;
		for (Integer s : nonView) {
			i++;
			if (timeCostCrtNode[s] < min) {

				min = timeCostCrtNode[s];
				index = s;
				indexNonView = i;
			}
		}
		nonView.remove(indexNonView);
		return index;
	}

	protected float shortestToWarehouse(ArrayList<Integer> nonView, float[][] timeCost) {
		float min = Float.MAX_VALUE;
		for (Integer i : nonView) {
			if (min > timeCost[i][timeCost.length - 1])
				min = timeCost[i][timeCost.length - 1];
		}
		return min;
	}
}
