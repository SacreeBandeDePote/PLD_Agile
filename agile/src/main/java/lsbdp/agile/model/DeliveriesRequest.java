package lsbdp.agile.model;

import java.util.Date;
import java.util.ArrayList;


public class DeliveriesRequest {
	private Date startingTime;
	private Intersection warehouse;
	private ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
	
	

	public DeliveriesRequest(Date startingTime, Intersection warehouse, ArrayList<Delivery> deliveryList) {
		this.startingTime = startingTime;
		this.warehouse = warehouse;
		this.deliveryList = deliveryList;
	}
	
	public Date getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Date startingTime) {
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
