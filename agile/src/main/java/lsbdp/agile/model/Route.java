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

	public double getRouteDuration() {
		double time = 0;
		double totalLenght = 0;
		List<Street> streets = getStreets();
		for(Street street : streets) {
			totalLenght += street.getLength();
		}
		time =  (totalLenght/4.16667);
		return time;
	}
	
	public String toString() {
		String s = "Point de dÃ©part : X " + startingPoint.getX() + " | Y " + startingPoint.getY() + "\r\n";
		s+= "DEBUT DU TRAJET\r\n";
		String previousStreet = null;		
		float length = 0;
		for(Street street: streets) {
			if(streets.indexOf(street)==0) {
				previousStreet = street.getName();
				length = street.getLength();
			}else if(streets.indexOf(street)==streets.size()-1){
				length += street.getLength();
				s+= "Prendre la route " + street.getName() + " sur une longueur de " + ((int)(length/10)*10) + "m et vous êtes arrivé.";
				s+= "\r\n";
				s+="FIN DU TRAJET";
			}else {
				if(street.getName().compareTo(previousStreet)==0) {	
					length += street.getLength();
				}else{
					if(length==0) length = street.getLength();
					s+= "Prendre la route " + previousStreet + " sur une longueur de " + ((int)(length/10)*10) + "m jusqu'Ã  l'intersection avec " + street.getName();
					s+= "\r\n";
					previousStreet = street.getName();
					length = 0;
//					int index = streets.indexOf(street);
//					if(index<2) CalculeAngle(startingPoint, streets.get(index-1).getEnd(), street.getEnd());
//					else CalculeAngle(startingPoint, streets.get(index-1).getEnd(), street.getEnd());
				}
			}
		}
		return s;
	}

	public float getTotalTime() {
		return getTotalLength()/MEAN_SPEED;
	}
	
//	public static String CalculeAngle(Intersection first, Intersection second, Intersection third) {
//		//Calcule sens d'arrivée
//		int deltaFromX = second.getX() - first.getX();
//		int deltaFromY = second.getY() - first.getY();
//		int deltaToX = third.getX() - second.getX();
//		int deltaToY = third.getY() - third.getY();
//		double angle = (-deltaFromX)*(deltaToX)+(-deltaFromY)*(deltaToY);
//		angle /= Math.sqrt( Math.pow(deltaFromX, 2) + Math.pow(deltaFromY, 2));
//		angle /= Math.sqrt( Math.pow(deltaToX  , 2) + Math.pow(deltaToY  , 2));
//		angle = Math.acos(angle) * 180 / Math.PI;
//		System.out.println(angle);
//		return "calcul ANgle = " + angle;
//	}
}
