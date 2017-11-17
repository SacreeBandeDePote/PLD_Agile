package lsbdp.agile.model;

import java.util.ArrayList;

public class Intersection {

	private int id;
	private int x;
	private int y;
	private ArrayList<Street> neighboors = new ArrayList<Street>();
	
	public Intersection(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		
	}
	
	public ArrayList<Street> getNeighboors() {
		return neighboors;
	}

	public void addStreet(Street street) {
		this.neighboors.add(street);
	}

	@Override
	public String toString() {
		return "Noeud [id=" + id + ", x=" + x + ", y=" + y + "]";
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
}
