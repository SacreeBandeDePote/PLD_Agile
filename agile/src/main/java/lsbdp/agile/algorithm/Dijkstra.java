package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {
    StreetMap map;

    public Dijkstra(StreetMap map) {
        super();
        this.map = map;
    }

    public Route performDijkstra(Intersection start, Intersection end) {
        Route route = new Route(start);

        //Map<IdIntersection, Distance from start>
        float[] distances = new float[map.size()];
        int[] previous = new int[map.size()];
        Comparator<Intersection> comparator = new RouteComparator(distances);
        PriorityQueue<Intersection> nonView = new PriorityQueue<Intersection>(10, comparator);

        //init the non view list
        for (Intersection node : map.values()) {
            if (node.getId() == start.getId())
                distances[node.getId()] = 0;
            else
                distances[node.getId()] = Integer.MAX_VALUE; //infinity

            previous[node.getId()] = -1; //undefine
            nonView.add(node);
        }

        //We know that a treated node has its shortest route
        //then we stop as soon as we go to the end node
        while (!nonView.peek().equals(end)) {
            Intersection current = nonView.poll();
            for (Intersection neighbor : current.getNeighbors()) {
                float dist = distances[current.getId()] + current.distTo(neighbor);
                if (dist < distances[neighbor.getId()]) {
                    distances[neighbor.getId()] = dist;
                    previous[neighbor.getId()] = current.getId();
                }
            }
        }

        int routeId = end.getId();
        while (routeId != -1) {
            route.addStreetToTop(map.get(previous[routeId]).getStreetTo(map.get(routeId)));
            routeId = previous[routeId];
        }
        return route;
    }

}

class RouteComparator implements Comparator<Intersection> {

    float[] distances;

    public RouteComparator(float[] distances) {
        this.distances = distances;
    }

    @Override
    public int compare(Intersection o1, Intersection o2) {
        return (int) (distances[o1.getId()] - distances[o2.getId()]); //increasing order
    }
}