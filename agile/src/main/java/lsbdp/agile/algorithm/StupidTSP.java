package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.util.List;

public class StupidTSP implements TSP {

	@Override
	public void findSolution(DeliverySchedule schedule, StreetMap map, Intersection warehouse, List<Delivery> deliveries) {
		Route[][] graphTSP = Dijkstra.createTSPGraph(map, warehouse, deliveries);
		Route r = graphTSP[graphTSP.length - 1][0];
		Delivery d = getDel(deliveries, r.getEnd());

		schedule.add(new Pair<>(r, d));

		for (int i = 0; i < graphTSP.length - 1; i++) {
			Route r1 = graphTSP[i][i + 1];
			Delivery d1 = getDel(deliveries, r.getEnd());

			schedule.add(new Pair<>(r1, d1));
		}
	}

	private Delivery getDel(List<Delivery> deliveries, Intersection target) {
		for (Delivery del : deliveries) {
			if (del.getLocation().equals(target))
				return del;
		}
		return null;
	}
}
