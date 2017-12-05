package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class NaiveLessCostTSP extends NaiveTSP {
	@Override
	protected Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new LessCostIterator(crtNode, nonView, timeCost);
	}
}
