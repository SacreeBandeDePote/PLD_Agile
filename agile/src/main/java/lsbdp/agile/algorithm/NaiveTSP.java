package lsbdp.agile.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class NaiveTSP extends TemplateTSP{
	@Override
	protected boolean bound(int crtNode, ArrayList<Integer> nonView, float crtCost, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return crtCost < bestSolutionCost;
	}

	@Override
	protected Iterator<Integer> iterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost, float[] duration, Pair<Float, Float>[] timeWindows) {
		return new SeqIterator(nonView);
	}
}
