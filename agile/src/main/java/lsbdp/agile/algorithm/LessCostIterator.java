package lsbdp.agile.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class LessCostIterator implements Iterator<Integer> {
	private PriorityQueue<Integer> neighbors;

	public LessCostIterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost) {
		neighbors = new PriorityQueue<>(nonView.size(), new Comparator<Integer>() {
			@Override
			public int compare(Integer integer, Integer t1) {
				return (int) (timeCost[crtNode][integer] - timeCost[crtNode][t1]);
			}
		});
		for (Integer s : nonView) {
			neighbors.add(s);
		}
	}


	@Override
	public boolean hasNext() {
		return !neighbors.isEmpty();
	}

	@Override
	public Integer next() {
		return neighbors.poll();
	}

}
