package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {
	private Dijkstra dijkstra;
	private Intersection warehouse;
	private DeliveriesRequest deliveries;
	private StreetMap map;
	private TSP tsp;

	public Scheduler(StreetMap map, Intersection warehouse, DeliveriesRequest deliveries, String tspType) {
		this.warehouse = warehouse;
		this.deliveries = deliveries;
		this.map = map;

		switch (tspType) {
			case "stupid":
				tsp = new StupidTSP();
				break;
			case "glouton":
				tsp = new GloutonTSP();
				break;
		}
	}

	public DeliverySchedule findSchedule() {
		DeliverySchedule schedule = new DeliverySchedule();

		tsp.findSolution(schedule, map, deliveries);
		return schedule;
	}
}
