package lsbdp.agile.algorithm;

import lsbdp.agile.model.*;

import java.util.*;

public class Dijkstra {
	private static final long UNDEFINED = -1l;

	public static Route performDijkstra(StreetMap map, Intersection start, Intersection end) {
		Route route = new Route(start);

		Map<Long, Float> distances = new HashMap<>();
		Map<Long, Long> previous = new HashMap<>();
		Comparator<Intersection> comparator = new RouteComparator(distances);
		PriorityQueue<Intersection> nonView = new PriorityQueue<>(map.size(), comparator);

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
		while (!nonView.peek().equals(end)) {
			Intersection current = nonView.poll();
			if (current.equals(end))
				break;
			for (Intersection neighbor : current.getNeighbors()) {
				float dist = distances.get(current.getId()) + current.distTo(neighbor);
				if (dist < distances.get(neighbor.getId())) {
					distances.put(neighbor.getId(), dist);
					previous.put(neighbor.getId(), current.getId());
					nonView.remove(neighbor);
					nonView.add(neighbor);
				}
			}
			nonView.remove(current);
		}

		long routeId = end.getId();
		while (routeId != start.getId()) {
			route.addStreetToTop(map.get(previous.get(routeId)).getStreetTo(map.get(routeId)));
			routeId = previous.get(routeId);
		}
		return route;
	}

	public static Route[][] createTSPGraph(StreetMap map, Intersection warehouse, List<Delivery> deliveries) {
		Route[][] graphTSP = new Route[deliveries.size() + 1][deliveries.size() + 1];

		for (int i = 0; i < graphTSP.length - 1; i++) {
			for (int j = 0; j < graphTSP.length - 1; j++) {
				if (i != j)
					graphTSP[i][j] = Dijkstra.performDijkstra(map, deliveries.get(i).getLocation(), deliveries.get(j).getLocation());
			}
			graphTSP[i][graphTSP.length - 1] = Dijkstra.performDijkstra(map, deliveries.get(i).getLocation(), warehouse);
		}

		for (int j = 0; j < graphTSP.length - 1; j++) {
			graphTSP[graphTSP.length - 1][j] = Dijkstra.performDijkstra(map, warehouse, deliveries.get(j).getLocation());
		}

		return graphTSP;
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