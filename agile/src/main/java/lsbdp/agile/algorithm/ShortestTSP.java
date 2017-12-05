package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

public class ShortestTSP extends NaiveTSP{

	@Override
	protected float bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows, float crtCost) {
		ArrayList<Integer> toVisit = new ArrayList<>(nonView);
		int nextIndex;
		int index = crtNode;
		float sum = 0.f;
		for (int i = 0; i < nonView.size(); i++) {
			nextIndex = getShortestRoute(timeCost[index], toVisit);
			sum += timeCost[index][nextIndex];
			index = nextIndex;
		}
		//ajout du chemin le plus court vers le warehouse
		sum += timeCost[index][timeCost.length - 1];

		return sum;
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
}
