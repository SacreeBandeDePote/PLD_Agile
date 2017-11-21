package lsbdp.agile.model;

import java.util.HashMap;

public class StreetMap extends HashMap<Float, Intersection> {

	private Map<Float, Intersection> intersections = new HashMap<Float, Intersection>();

	public Map<Float, Intersection> getMapIntersections() {
		return intersections;
	}
	
	public Intersection getIntersection(Float id) {
		return intersections.get(id);
	}

	public void addIntersection(Intersection intersection) {
		this.intersections.put(intersection.getId(), intersection);
	}
}
