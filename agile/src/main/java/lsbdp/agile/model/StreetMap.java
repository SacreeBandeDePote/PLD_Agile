package lsbdp.agile.model;

import java.util.HashMap;
import java.util.Map;

public class StreetMap {

	private Map<Integer, Intersection> intersections = new HashMap<Integer, Intersection>();

	public Map<Integer, Intersection> getMapIntersections() {
		return intersections;
	}
	
	public Intersection getIntersection(int id) {
		return intersections.get(id);
	}

	public void addIntersection(Intersection intersection) {
		this.intersections.put(intersection.getId(), intersection);
	}
}
