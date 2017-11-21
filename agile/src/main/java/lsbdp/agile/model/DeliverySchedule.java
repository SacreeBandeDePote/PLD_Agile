package lsbdp.agile.model;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class DeliverySchedule {
	private List<Pair<Route, Delivery>> schedule = new ArrayList<Pair<Route,Delivery>>();

	public List<Pair<Route, Delivery>> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Pair<Route, Delivery>> schedule) {
		this.schedule = schedule;
	}

	public DeliverySchedule(List<Pair<Route, Delivery>> schedule) {
		this.schedule = schedule;
	} 
}
