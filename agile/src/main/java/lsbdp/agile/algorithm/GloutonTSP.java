package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DEPRECATED !!
 * A Glutton implementation of the TSP witch will always go the the nearest point possible
 */
public class GloutonTSP implements TSP {

	@Override
	public void findSolution(DeliverySchedule schedule, StreetMap map, DeliveriesRequest req) {
		Intersection warehouse = req.getWarehouse();
		List<Delivery> deliveries = req.getDeliveryList();
		Route[][] graphTSP = Dijkstra.createTSPGraph(map, warehouse, deliveries);
		List<Integer> view = new ArrayList<>();
		Route r = findShortest(graphTSP[graphTSP.length - 1], graphTSP.length - 1, view);
		Delivery d = getDel(deliveries, r.getEnd());
		schedule.add(new Pair<>(r, d));

		int lastView = 0;
		while (view.size() < graphTSP.length - 1) {
			lastView = view.get(view.size() - 1);
			r = findShortest(graphTSP[lastView], lastView, view);
			d = getDel(deliveries, r.getEnd());

			schedule.add(new Pair<>(r, d));
		}

		r = graphTSP[lastView][graphTSP.length - 1];
		d = getDel(deliveries, r.getEnd());
		schedule.add(new Pair<>(r, d));
	}

	private Route findShortest(Route[] line, int current, List<Integer> view) {
		Route r = null;
		int pick = 0;
		for (int i = 0; i < line.length - 1; i++) {
			if (view.contains(i) || i == current) {
				continue;
			}
			if (r == null) {
				r = line[i];
				pick = i;
			} else {
				if (r.getTotalLength() > line[i].getTotalLength()) {
					r = line[i];
					pick = i;
				}
			}
		}

		view.add(pick);
		return r;
	}

	private Delivery getDel(List<Delivery> deliveries, Intersection target) {
		for (Delivery del : deliveries) {
			if (del.getLocation().equals(target))
				return del;
		}
		return null;
	}
}
