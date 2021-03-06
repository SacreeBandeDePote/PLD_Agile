package lsbdp.agile.model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.util.Pair;

public class DeliverySchedule extends ArrayList<Pair<Route, Delivery>>{
	
	private Date startingTime;
	private Date endingTime;
	
	public DeliverySchedule() {
		this.startingTime = null;
		this.endingTime = null;
		for(Pair<Route, Delivery> p: this) {
			if(p == null) {
				this.remove(p);
			}
		}
	}
	public Intersection getWarehouse() {
		return this.get(0).getKey().getStartingPoint();
	}
	
	public String toString() {
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

		Intersection entrepot = this.getWarehouse();
		String s = "Entrepôt\r\n";
		s += "Localisation : Coordonnées X " + entrepot.getX() + " | Y " + entrepot.getY() + "\r\n";
		s += "Heure de départ : " + formater.format(this.startingTime) +"\r\n";
		
		for(Pair<Route, Delivery> pair : this) {
			int pairIndex = this.indexOf(pair);
			if(pairIndex == 0) {
				s += "---------  Itinéraire Entrepôt -> Livraison 1  ---------\r\n";
			} else if (pairIndex == this.size()-1) {
				s += "---------  Itinéraire Livraison " + pairIndex  + " -> Entrepôt  ---------\r\n";

			} else {
				s += "---------  Itinéraire Livraison " + pairIndex  + " -> Livraison " + (pairIndex+1) + "  ---------\r\n";
			}
			s += pair.getKey().toString();
			s += "\r\n";
						
			s += "------------------------------------------------------------------------------------------------\r\n";
			s += "------------------------------------------------------------------------------------------------\r\n\r\n";
			
			if(pairIndex  == this.size()-1) {
				s += "Entrepôt\r\n";
				s += "Localisation : Coordonnées X " + entrepot.getX() + " | Y " + entrepot.getY() + "\r\n";
				s += "Heure d'arrivée estimée : " + formater.format(this.endingTime) + "\r\n";	
			} else {
				s += "LIVRAISON " + (pairIndex+1) + "\r\n";
				s += "Localisation : Coordonnées X  " + pair.getValue().getLocation().getX() + "| Y " + pair.getValue().getLocation().getY() + "\r\n";
				s += "Heure d'arrivée sur place : " + formater.format(pair.getValue().getDeliveryTime()) + "\r\n";
				s += "      |\r\n";
				s += "Heure de départ : " + formater.format(pair.getValue().getDateAfterDuration()) + "\r\n\r\n";
			}
		}
		return s;
     }

	public Date getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(Date endingTime) {
		this.endingTime = endingTime;
	}

	public Date getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	} 
}
