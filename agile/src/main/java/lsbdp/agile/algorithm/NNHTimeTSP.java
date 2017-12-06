package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

public class NNHTimeTSP extends NNHTSP {

	@Override
	protected boolean bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows, float crtCost) {
		ArrayList<Integer> toVisit = new ArrayList<>(nonView);
		int nextIndex;
		int index = crtNode;
		
		if( !checkTimePossible(nonView,timeWindows,crtCost) ){
			return false; //infinite
		}
		
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
}
