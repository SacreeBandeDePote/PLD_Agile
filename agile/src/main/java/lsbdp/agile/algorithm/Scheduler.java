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
        this.dijkstra = new Dijkstra(map);
        this.warehouse = warehouse;
        this.deliveries = deliveries;

        graphTSP = new Route[deliveries.size()+1][deliveries.size()+1];

        switch (tspType) {
        case "stupid" :
            tsp = new StupidTSP();
            break;
        case "glouton" :
        	tsp = new StupidTSP();
        	break;
        }
    }

    public DeliverySchedule findSchedule() {
        DeliverySchedule schedule = new DeliverySchedule();
        createTSPGraph();

        tsp.findSolution(schedule, graphTSP, deliveries);
        return schedule;
    }

    private void createTSPGraph() {
        for(int i=0 ; i<graphTSP.length-1 ; i++) {
            for(int j=0 ; j<graphTSP.length-1 ; j++) {
                if (i!=j)
                    graphTSP[i][j] = dijkstra.performDijkstra(deliveries.get(i).getLocation(), deliveries.get(j).getLocation());
            }
            graphTSP[i][graphTSP.length-1] = dijkstra.performDijkstra(deliveries.get(i).getLocation(), warehouse);
        }

        for (int j=0 ; j<graphTSP.length-1 ; j++) {
            graphTSP[graphTSP.length-1][j] = dijkstra.performDijkstra(warehouse, deliveries.get(j).getLocation());
        }
    }
}
