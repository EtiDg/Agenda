package agenda.process.object;

import java.util.ArrayList;

public class ModeleDeReprise {
	
	private int id;
	private String nom;
	private ArrayList<Integer> reprises;

	public ModeleDeReprise(String nom){
		this.nom = nom;
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}
	
	public ArrayList<Integer> getReprises() {
		return reprises;
	}

	
}
