package lsbdp.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
	public final float MEAN_SPEED = 250f; //m*min^-1
	private List<Street> streets = new ArrayList<Street>();
	private Intersection startingPoint;
	
	public Intersection getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Intersection startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Route(Intersection startingPoint) {
		this.startingPoint = startingPoint;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public void addStreetToTop(Street street) {
		streets.add(0, street);
	}

	public Intersection getEnd() {
		return streets.get(streets.size()-1).getEnd();
	}
	
	public float getTotalLength() {
		float sum = 0f;
		
		for(Street street : streets) {
			sum += street.getLength();
		}
		return sum;
	}

	public String toString() {
		String s = "Point de départ : X " + startingPoint.getX() + " | Y " + startingPoint.getY() + "\r\n";
		s+= "DEBUT DU TRAJET\r\n";
		String previousStreet = null;		
		float length = 0;
		for(Street street: streets) {
			if(streets.indexOf(street)==0) {
				previousStreet = street.getName();
				length = street.getLength();
			}else if(streets.indexOf(street)==streets.size()-1){
				if(street.getName().compareTo(previousStreet)==0) {
					length+= street.getLength();
					s+= "Prendre la route " + street.getName() + " sur une longueur de " + ((int)(length/10)*10) + "m et vous �tes arriv�.";
					s+= "\r\n";
				}else {
					s+= "Prendre la route " + previousStreet + " sur une longueur de " + ((int)(length/10)*10) + "m jusqu'à l'intersection avec " + street.getName();
					s+= "\r\n";
					
					
					double angleCos;
					double angleSin;
					Intersection firstIntersection;
					int index = streets.indexOf(street);
					if(index<2) firstIntersection = startingPoint;
					else firstIntersection = streets.get(index-2).getEnd();
					angleCos = CalculeCos(firstIntersection, streets.get(index-1).getEnd(), street.getEnd());
					angleSin = CalculeSin(firstIntersection, streets.get(index-1).getEnd(), street.getEnd());

					if(Math.abs(angleSin)< 0.2) s+= "Continuez sur " + street.getName() + "\r\n";
					else if(angleSin<0) s+= "Tournez � gauche sur " + street.getName() + "\r\n";
					else s+= "Tournez � droite sur " + street.getName() + "\r\n";
					
					
					s+= "Prendre la route " + street.getName() + " sur une longueur de " + ((int)(street.getLength()/10)*10) + "m et vous �tes arriv�.";
					s+= "\r\n";
				}
				s+="FIN DU TRAJET";
			}else {
				if(street.getName().compareTo(previousStreet)==0) {	
					length += street.getLength();
				}else{
					if(length==0) length = street.getLength();
					s+= "Prendre la route " + previousStreet + " sur une longueur de " + ((int)(length/10)*10) + "m jusqu'à l'intersection avec " + street.getName();
					s+= "\r\n";
					previousStreet = street.getName();
					length = 0;
					int index = streets.indexOf(street);
					double angleCos;
					double angleSin;
					Intersection firstIntersection;
					if(index<2) firstIntersection = startingPoint;
					else firstIntersection = streets.get(index-2).getEnd();
					angleCos = CalculeCos(firstIntersection, streets.get(index-1).getEnd(), street.getEnd());
					angleSin = CalculeSin(firstIntersection, streets.get(index-1).getEnd(), street.getEnd());

					if(Math.abs(angleSin)< 0.2) s+= "Continuez sur " + street.getName() + "\r\n";
					else if(angleSin<0) s+= "Tournez � gauche sur " + street.getName() + "\r\n";
					else s+= "Tournez � droite sur " + street.getName() + "\r\n";
				}
			}
		}
		return s;
	}

	public float getTotalTime() {
		return getTotalLength()/MEAN_SPEED;
	}
	
	public static double CalculeCos(Intersection first, Intersection second, Intersection third) {
		//Calcule sens d'arriv�e
		int deltaFromX = second.getX() - first.getX();
		int deltaFromY = second.getY() - first.getY();
		int deltaToX = third.getX() - second.getX();
		int deltaToY = third.getY() - second.getY();
		
		double angleSin = deltaFromX * deltaToY - deltaFromY * deltaToX;
		angleSin = angleSin/ ( Math.sqrt(Math.pow(deltaFromX, 2) + Math.pow(deltaToX, 2)) * Math.sqrt(Math.pow(deltaFromY, 2) + Math.pow(deltaToY, 2)));
		double angleCos = deltaFromX * deltaToX + deltaFromY * deltaToY;
		angleCos = angleCos/ ( Math.sqrt(Math.pow(deltaFromX, 2) + Math.pow(deltaToX, 2)) * Math.sqrt(Math.pow(deltaFromY, 2) + Math.pow(deltaToY, 2)));
		return angleCos;
	}
	
	public static double CalculeSin(Intersection first, Intersection second, Intersection third) {
		//Calcule sens d'arriv�e
		int deltaFromX = second.getX() - first.getX();
		int deltaFromY = second.getY() - first.getY();
		int deltaToX = third.getX() - second.getX();
		int deltaToY = third.getY() - second.getY();
		
		double angleSin = deltaFromX * deltaToY - deltaFromY * deltaToX;
		angleSin = angleSin/ ( Math.sqrt(Math.pow(deltaFromX, 2) + Math.pow(deltaToX, 2)) * Math.sqrt(Math.pow(deltaFromY, 2) + Math.pow(deltaToY, 2)));
		return angleSin;
	}
}
