package lsbdp.agile.model;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class DeliverySchedule extends ArrayList<Pair<Route, Delivery>>{
	
	public Intersection getWhareHouse() {
		return this.get(0).getKey().getStartingPoint();
	}
	
	public String toString() {
		Intersection entrepot = this.getWhareHouse();
		String s = "Entrepôt\r\n";
		s += "Localisation" + entrepot.getX() + " " + entrepot.getY() + "\r\n";
		s += "8h00: départ"; //TODO implement stratingTime
		
		for(int i = 0; i < this.size() ; i++) {
			Pair<Route, Delivery> pair = this.get(i);
			
			if(i == 0) {
				s += "------  Itinéraire Entrepôt -> Livraison 1  ------";
			} else {
				s += "------  Itinéraire Livraison " + i + " -> Livraison " + i + "  ------";
			}
			
			s += "\r\n";
			
			pair.getKey().toString(); //Call Route toString method
			
			s += "--------------------------------------------------\r\n\r\n";
			
			if(i == this.size()-1) {
				s += "Entrepôt\r\n";
				s += "Localisation" + entrepot.getX() + " " + entrepot.getY() + "\r\n";
				s += "10h00: arrivée\r\n"; // TODO implement eta :)
			} else {
				s += "Livraison " + i;
				s += "Localisation" + pair.getValue().getLocation().getX() + " | " + pair.getValue().getLocation().getY() + "\r\n";
				s += pair.getValue().getTimespanStart() + ": arrivée\r\n";
				s += "     |\r\n";
				s += pair.getValue().getTimespanStart() + ": départ";
				s += "\r\n\r\n";
			}
		}
		return s;
     } 
}
