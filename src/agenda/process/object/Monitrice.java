package agenda.process.object;

import java.util.ArrayList;

public class Monitrice {

	private int id;
	private String nom;
	private ArrayList<Integer> creneaux;
	
	public Monitrice(String nom){
		this.nom = nom;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNom(){
		return nom;
	}
	
	public ArrayList<Integer> getCreneaux() {
		return creneaux;
	}

}
