package agenda.process.object;

import java.util.ArrayList;

public class Monitrice {

	private String nom;
	private ArrayList<Integer> creneaux;
	
	public Monitrice(String nom){
		this.nom = nom;
	}
	
	public String getNom(){
		return nom;
	}
}
