package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {
    private Dijkstra dijkstra;
    private Intersection warehouse;
    private List<Delivery> deliveries;
    private Route[][] graphTSP;
    private TSP tsp;

    public Scheduler(StreetMap map, Intersection warehouse, List<Delivery> deliveries, String tspType) {
        //this.dijkstra = new Dijkstra(map);
        this.warehouse = warehouse;
        this.deliveries = deliveries;

        graphTSP = new Route[deliveries.size()+1][deliveries.size()+1];

        switch (tspType) {
        case "stupid" :
            tsp = new StupidTSP();
            break;
        case "glouton" :
        	tsp = new GloutonTSP();
        	break;
        }
    }

    public DeliverySchedule findSchedule() {
        DeliverySchedule schedule = new DeliverySchedule();
        //createTSPGraph();

        //tsp.findSolution(schedule, graphTSP, deliveries);
        return schedule;
    }
}
