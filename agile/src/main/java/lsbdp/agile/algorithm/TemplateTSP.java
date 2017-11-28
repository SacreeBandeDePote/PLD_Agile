package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.util.Date;
import java.util.List;

public class TemplateTSP implements TSP{



	@Override
	public void findSolution(DeliverySchedule schedule, StreetMap map, Intersection warehouse, List<Delivery> deliveries) {

	}

	private void adaptModel(DeliverySchedule schedule, StreetMap map, Intersection warehouse, DeliveriesRequest req) {
		List<Delivery> deliveries = req.getDeliveryList();
		Route[][] graphTSP = Dijkstra.createTSPGraph(map, warehouse, deliveries);

		float[][] timeCost = new float[deliveries.size()+1][deliveries.size()+1];
		for (int i = 0; i < timeCost.length ; i++) {
			for (int j = 0; j < timeCost.length ; j++) {
				if(i == j) {
					timeCost[i][j] = 0f;
				}
				else {
					timeCost[i][j] = graphTSP[i][j].getTotalTime();
				}
			}
		}

		float[] duration = new float[deliveries.size()+1]; //minutes
		for (int i = 0; i < duration.length-1 ; i++) {
			duration[i] = deliveries.get(i).getDuration()/60f;
		}
		duration[duration.length-1] = 0f;

		Date start = req.getStartingTime();
		Pair<Float, Float>[] hourWindows = new Pair[deliveries.size()];
		for (int i = 0; i < hourWindows.length ; i++) {
			Date timeSpanStart = deliveries.get(i).getTimespanStart();
			Date timeSpanEnd = deliveries.get(i).getTimespanEnd();

			float startSpan = (timeSpanStart.getTime() - start.getTime())/(1000f*60f); //ms to minutes
			float endSpan = (timeSpanEnd.getTime() - start.getTime())/(1000f*60f); //ms to minutes

			hourWindows[i] = new Pair<>(startSpan, endSpan);
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
