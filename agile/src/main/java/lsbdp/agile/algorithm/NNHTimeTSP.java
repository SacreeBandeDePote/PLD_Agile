package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;

public class NNHTimeTSP extends ShortestLessCostTSP{

	public boolean checkTimePossible(int crtNode, ArrayList<Integer> nonView, Pair<Float, Float>[] timeWindows) {
		float startTime = timeWindows[crtNode].getKey();
		System.out.println("STARTTIME :" + startTime);

		for (Integer s : nonView) {
			System.out.println("TIME :" + timeWindows[s].getValue());
			if (timeWindows[s].getValue() < startTime) {
				return false;
			}
		}
		return true;
	}
}
