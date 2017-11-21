package lsbdp.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

	private int id;
	private int x;
	private int y;
	private List<Street> streets = new ArrayList<Street>();

	public Intersection(int id, int x, int y) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
	
	public List<Intersection> getNeighbors() {
		List<Intersection> neighbors = new ArrayList<Intersection>();
		
		for(Street street : this.streets) {
			neighbors.add(street.getEnd());
		}
		
		return neighbors;
	}

	public float distTo(Intersection neighbor) {
		for(Street street : this.streets) {
			if(neighbor.equals(street.getEnd()))
				return street.getLength();
		}
		return -1;
	}

	public Street getStreetTo(Intersection neighbor) {
		for(Street street : this.streets) {
			if(neighbor.equals(street.getEnd()))
				return street;
		}
		return null;
	}

}
