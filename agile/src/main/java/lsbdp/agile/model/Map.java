package lsbdp.agile.model;

import java.util.ArrayList;

public class Map {

	private ArrayList<Intersection> intersections = new ArrayList<Intersection>();

	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}

	public void addIntersection(Intersection intersection) {
		this.intersections.add(intersection);
	}
}
