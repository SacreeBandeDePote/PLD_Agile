package lsbdp.agile.model;

import java.sql.Time;
import java.util.ArrayList;

public class DeliveryRequest {
	private Time startingTime;
	private Intersection warehouse;
	private ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
	
	
	public DeliveryRequest(Time startingTime, Intersection warehouse, ArrayList<Delivery> deliveryList) {
		this.startingTime = startingTime;
		this.warehouse = warehouse;
		this.deliveryList = deliveryList;
	}
	
	public Time getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Time startingTime) {
		this.startingTime = startingTime;
	}

	public Intersection getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Intersection warehouse) {
		this.warehouse = warehouse;
	}
	public ArrayList<Delivery> getDeliveryList() {
		return deliveryList;
	}

	public void setDeliveryList(ArrayList<Delivery> deliveryList) {
		this.deliveryList = deliveryList;
	}


}
