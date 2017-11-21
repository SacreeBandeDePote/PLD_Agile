package lsbdp.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Intersection {

	private Float id;
	private int x;
	private int y;
	private List<Street> streets = new ArrayList<Street>();

	public Intersection(Float id, int x, int y) {
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

	public Float getId() {
		return id;
	}

	public void setId(Float id) {
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
	
	public List<Intersection> getNeighboors() {
		List<Intersection> neighboors = new ArrayList<Intersection>();
		
		for(Street street : this.streets) {
			neighboors.add(street.getEnd());
		}
		
		return neighboors;
	}

}
