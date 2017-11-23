package lsbdp.agile.algorithm;

import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

import java.util.*;

public class Dijkstra {
    private StreetMap map;
    private static final long UNDEFINED = -1l;

    public Dijkstra(StreetMap map) {
        super();
        this.map = map;
    }

    public Route performDijkstra(Intersection start, Intersection end) {
        Route route = new Route(start);

        Map<Long, Float> distances = new HashMap<>();
        Map<Long, Long> previous = new HashMap<>();
        Comparator<Intersection> comparator = new RouteComparator(distances);
        List<Intersection> nonView = new ArrayList<>();

        //init the non view list
        for (Intersection node : map.values()) {
            if (node.getId() == start.getId())
                distances.put(node.getId(), 0.f);
            else
                distances.put(node.getId(), Float.MAX_VALUE); //infinity

            previous.put(node.getId(), UNDEFINED); //undefine
            nonView.add(node);
        }

        //We know that a treated node has its shortest route
        //then we stop as soon as we go to the end node
        while (!nonView.get(0).equals(end)) {
            Intersection current =  nonView.get(0);
            if (current.equals(end))
                break;
            for (Intersection neighbor : current.getNeighbors()) {
                float dist = distances.get(current.getId()) + current.distTo(neighbor);
                if (dist < distances.get(neighbor.getId())) {
                    distances.put(neighbor.getId(), dist);
                    previous.put(neighbor.getId(), current.getId());
                }
            }
            nonView.remove(0);
            nonView.sort(comparator);
        }

        long routeId = end.getId();
        while (routeId != start.getId()) {
            route.addStreetToTop(map.get(previous.get(routeId)).getStreetTo(map.get(routeId)));
            routeId = previous.get(routeId);
        }
        return route;
    }

}

class RouteComparator implements Comparator<Intersection> {

    private Map<Long, Float> distances;

    RouteComparator(Map<Long, Float> distances) {
        this.distances = distances;
    }

    public int compare(Intersection o1, Intersection o2) {
        return (int) (distances.get(o1.getId()) - distances.get(o2.getId())); //increasing order
    }
}