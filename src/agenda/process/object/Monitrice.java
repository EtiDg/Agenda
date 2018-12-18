package agenda.process.object;

import java.sql.SQLException;
import java.util.ArrayList;

import agenda.process.sql.QueryManager;

public class Monitrice {

	private int id;
	private String nom;
	boolean isCreneauxUpdated;
	private ArrayList<Creneau> creneaux;
	
	public Monitrice(String nom){
		this.nom = nom;
		isCreneauxUpdated = false;
	}
	
	public Monitrice(int id, String nom){
		this.id = id;
		this.nom = nom;
		isCreneauxUpdated = false;
	}
	
	public void updateCreneaux(){
		try{
			creneaux = QueryManager.selectCreneaux(id);
		}catch(SQLException e){
			System.out.println("erreur");
		}
		isCreneauxUpdated = true;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNom(){
		return nom;
	}
	
	public ArrayList<Creneau> getCreneaux() {
		if (!isCreneauxUpdated){
			updateCreneaux();
		}
		return creneaux;
	}

}
