package lsbdp.agile.model;

import java.util.ArrayList;

public class Route {
	private ArrayList<Street> streets = new ArrayList<Street>();
	private Intersection startingPoint;
	
	public Intersection getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Intersection startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Route(Intersection startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Route(ArrayList<Street> streets, Intersection startingPoint) {
		this.streets = streets;
		this.startingPoint = startingPoint;
	}
	
	
	
}
