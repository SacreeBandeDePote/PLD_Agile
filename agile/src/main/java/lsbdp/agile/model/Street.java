package lsbdp.agile.model;

public class Street {

	private float length;
	private String name;
	private Intersection end;
	
	public Street(float length, String name, Intersection end) {
		super();
		this.length = length;
		this.name = name;
		this.end = end;
	}
	
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Intersection getEnd() {
		return end;
	}
	public void setEnd(Intersection end) {
		this.end = end;
	}
	
}
