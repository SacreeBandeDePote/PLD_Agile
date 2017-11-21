package lsbdp.agile.model;
import java.sql.Time;
import java.util.ArrayList;

import javafx.util.Pair;

public class DeliverySchedule {
	private ArrayList<Pair<Intersection, Time>> schedule = new ArrayList<Pair<Intersection,Time>>();

	public ArrayList<Pair<Intersection, Time>> getSchedule() {
		return schedule;
	}

	public void setSchedule(ArrayList<Pair<Intersection, Time>> schedule) {
		this.schedule = schedule;
	}

	public DeliverySchedule(ArrayList<Pair<Intersection, Time>> schedule) {
		this.schedule = schedule;
	} 
}
