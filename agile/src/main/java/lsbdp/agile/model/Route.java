package lsbdp.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
	private List<Street> streets = new ArrayList<Street>();
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

	public Route(List<Street> streets, Intersection startingPoint) {
		this.streets = streets;
		this.startingPoint = startingPoint;
	}
}
