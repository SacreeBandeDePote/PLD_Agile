package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

public class NNHTSP extends NaiveTSP {

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
			if(min > timeCost[i][timeCost.length - 1])
				min = timeCost[i][timeCost.length - 1];
		}
		return min;
	}
}
