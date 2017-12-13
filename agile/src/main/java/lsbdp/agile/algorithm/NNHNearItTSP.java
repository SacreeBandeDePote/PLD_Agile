package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Same as NNHTSP but with the NearestIterator
 */
public class NNHNearItTSP extends NNHTSP {
	@Override
	protected Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new NearestIterator(crtNode, nonView, timeCost);
	}
}
