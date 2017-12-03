package lsbdp.agile.algorithm;

import java.util.ArrayList;
import java.util.Iterator;

public class LessCostIterator implements Iterator<Integer> {
	private Integer[] neighbors;
	private int nbNeighbors;

	public LessCostIterator(int crtNode, ArrayList<Integer> nonView, float[][] timeCost) {
		neighbors = new Integer[nonView.size()];
		nbNeighbors = 0;
		int i = 0;
		float[] cost = new float[nonView.size()];
		for (Integer s : nonView) {
			cost[i] = timeCost[crtNode][s];
			neighbors[nbNeighbors++] = s;
			i++;
		}


		boolean tab_en_ordre = false;
		int taille = nbNeighbors;
		while (!tab_en_ordre) {
			tab_en_ordre = true;
			for (int j = 0; j < taille - 1; j++) {
				if (cost[j] < cost[j + 1]) {
					float pivot = cost[j];
					cost[j] = cost[j + 1];
					cost[j + 1] = pivot;

					int pivot2 = neighbors[j];
					neighbors[j] = neighbors[j + 1];
					neighbors[j + 1] = pivot2;

					tab_en_ordre = false;
				}
			}
			taille--;
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
