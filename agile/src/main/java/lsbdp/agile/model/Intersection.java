package lsbdp.agile.model;

import java.util.*;

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

	public List<Intersection> getNeighbors() {
		Set<Intersection> neighbors = new HashSet<>();
		
		for(Street street : this.streets) {
			neighbors.add(street.getEnd());
		}
		
		return new ArrayList<>(neighbors);
	}

	public float distTo(Intersection neighbor) {
		return getStreetTo(neighbor).getLength();
	}

	public Street getStreetTo(Intersection neighbor) {
		List<Street> streets = new ArrayList<>();
		for(Street street : this.streets) {
			if(neighbor.equals(street.getEnd()))
				streets.add(street);
		}
		if(streets.size() == 0)
			return null;
		if(streets.size() == 1)
			return streets.get(0);

		streets.sort(new Comparator<Street>() {
			@Override
			public int compare(Street street, Street t1) {
				return (int) (street.getLength()-street.getLength());
			}
		});

		return streets.get(0);
	}

}
