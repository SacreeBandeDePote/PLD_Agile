package lsbdp.model;

public class Troncon {

	private float longueur;
	private String nomRue;
	private Noeud destination;
	private Noeud origine;
	
	public float getLongueur() {
		return longueur;
	}
	public void setLongueur(float longueur) {
		this.longueur = longueur;
	}
	public String getNomRue() {
		return nomRue;
	}
	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}
	public Noeud getDestination() {
		return destination;
	}
	public void setDestination(Noeud destination) {
		this.destination = destination;
	}
	public Noeud getOrigine() {
		return origine;
	}
	public void setOrigine(Noeud origine) {
		this.origine = origine;
	}
	
}
