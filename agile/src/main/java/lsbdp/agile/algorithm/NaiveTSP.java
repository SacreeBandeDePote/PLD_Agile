package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * a naive TSP with no heuristic and the sequential iterator
 */
public class NaiveTSP extends TemplateTSP {
	@Override
	protected boolean bound(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows, float crtCost) {
		return true;
	}

	@Override
	protected Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new SeqIterator(nonView);
	}
}
