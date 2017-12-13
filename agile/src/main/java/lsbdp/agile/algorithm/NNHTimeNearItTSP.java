package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Same as NNHTimeTSP but with NearestIterator
 */
public class NNHTimeNearItTSP extends NNHTimeTSP {
	@Override
	protected Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new NearestIterator(crtNode, nonView, timeCost);
	}
}
