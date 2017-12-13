package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * a naive tsp but with the NearestIterator
 */
public class NaiveNearItTSP extends NaiveTSP {
	@Override
	protected Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new NearestIterator(crtNode, nonView, timeCost);
	}
}
