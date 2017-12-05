package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

public class NNHTimeTSP extends ShortestLessCostTSP{

	@Override
	protected float bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
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

	public boolean checkTimePossible(int crtNode, ArrayList<Integer> nonView, Pair<Float, Float>[] timeWindows) {
		float startTime = timeWindows[crtNode].getKey();

		for (Integer s : nonView) {
			if (timeWindows[s].getValue() < startTime) {
				return false;
			}
		}
		return true;
	}
}
