package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Very close to NNHTSP but it improved the bound by taking account of the time window
 */
public class NNHTimeTSP extends NNHTSP {

	/**
	 * Now the bound take account of the time window. if we are to late for any non view node, we stop to explore this branch.
	 * For a delivery request with time windows, it will largely accelerate the computation of the solution
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

		if (!checkTimePossible(nonView, timeWindows, crtCost)) {
			return false; //infinite
		}

		float sum = 0.f;
		for (int i = 0; i < nonView.size(); i++) {
			nextIndex = getShortestRoute(timeCost[index], toVisit);
			sum += timeCost[index][nextIndex] + duration[nextIndex];
			index = nextIndex;
		}
		//add the shortest path to the warehouse
		sum += shortestToWarehouse(nonView, timeCost);

		return (sum + crtCost) < bestSolutionCost;
	}
}
