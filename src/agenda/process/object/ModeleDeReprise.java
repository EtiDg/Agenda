package agenda.process.object;

import java.sql.SQLException;
import java.util.ArrayList;

import agenda.process.sql.QueryManager;

public class ModeleDeReprise {
	
	private long id;
	private String nom;
	boolean isReprisesUpdated;
	private ArrayList<Reprise> reprises;
	
	public ModeleDeReprise(String nom){
		this.nom = nom;
		isReprisesUpdated = false;
		id = IdGenerator.getId();
	}
	
	public ModeleDeReprise(long id, String nom){
		this.id = id;
		this.nom = nom;
		isReprisesUpdated = false;
	}
	
	public void updateReprises(){
		try{
			reprises = QueryManager.selectReprisesDeModele(id);
			isReprisesUpdated = true;
		}catch(SQLException e){
			System.out.println("erreur");
		}
	}

	public long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public ArrayList<Reprise> getReprises() {
		if(!isReprisesUpdated){
			updateReprises();
		}
		return reprises;
	}

	@Override
	public String toString(){
		return nom + " " + id;
	}
	
}
