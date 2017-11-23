package lsbdp.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

	private long id;
	private int x;
	private int y;
	private List<Street> streets = new ArrayList<Street>();

	public Intersection(long id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;

	}

	public List<Street> getStreets() {
		return streets;
	}

	public void addStreet(Street street) {
		this.streets.add(street);
	}

	@Override
	public String toString() {
		return "Noeud [id=" + id + ", x=" + x + ", y=" + y + "]";
	}

	public boolean equals(Intersection other) {
		return this.getId() == other.getId();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	//TODO : Beware, two intersections may have multiple links
	public List<Intersection> getNeighbors() {
		List<Intersection> neighbors = new ArrayList<Intersection>();
		
		for(Street street : this.streets) {
			neighbors.add(street.getEnd());
		}
		
		return neighbors;
	}

	//TODO : Beware, two intersections may have multiple links (get the shotest)
	public float distTo(Intersection neighbor) {
		for(Street street : this.streets) {
			if(neighbor.equals(street.getEnd()))
				return street.getLength();
		}
		return -1;
	}

	//TODO : Beware, two intersections may have multiple links (get the shotest)
	public Street getStreetTo(Intersection neighbor) {
		for(Street street : this.streets) {
			if(neighbor.equals(street.getEnd()))
				return street;
		}
		return null;
	}

}
