package lsbdp.agile.algorithm;

import java.util.Iterator;
import java.util.List;


/**
 * Simple iterator tha give the elements in the reverse order it got them
 */
public class SeqIterator implements Iterator<Integer> {
	private Integer[] neighbors;
	private int nbNeighbors;

	public SeqIterator(List<Integer> nonView) {
		neighbors = new Integer[nonView.size()];
		nbNeighbors = 0;
		for (Integer s : nonView) {
			neighbors[nbNeighbors++] = s;
		}
	}

	@Override
	public boolean hasNext() {
		return nbNeighbors > 0;
	}

	@Override
	public Integer next() {
		return neighbors[--nbNeighbors];
	}
}
