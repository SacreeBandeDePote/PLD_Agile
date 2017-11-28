package lsbdp.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
	public final float MEAN_SPEED = 25f; //m*min^-1
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

	public List<Street> getStreets() {
		return streets;
	}

	public void addStreetToTop(Street street) {
		streets.add(0, street);
	}

	public Intersection getEnd() {
		return streets.get(streets.size()-1).getEnd();
	}
	
	public float getTotalLength() {
		float sum = 0f;
		
		for(Street street : streets) {
			sum += street.getLength();
		}
		return sum;
	}

	public float getTotalTime() {
		return getTotalLength()/MEAN_SPEED;
	}
}
