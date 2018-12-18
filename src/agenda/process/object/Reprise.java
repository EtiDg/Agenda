package agenda.process.object;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import agenda.process.sql.QueryManager;

public class Reprise {
	
	private int id;
	private String nom;
	private Calendar date;
	private int heureDebut;
	private int duree;
	private int idMR;
	private Lieu lieu;
	boolean isMonitricesUpdated = false;
	private ArrayList<Monitrice> monitrices;

	
	public Reprise(String nom, Date date, int heureDebut, int duree, int idMR, Lieu lieu){
		this.nom = nom;
		this.date.setTime(date);
		this.heureDebut = heureDebut;
		this.duree = duree;
		this.idMR= idMR;
		this.lieu = lieu;
	}

	public Reprise(int id, String nom, Date date, int heureDebut, int duree, int idMR, Lieu lieu){
		this.id = id;
		this.nom = nom;
		this.date.setTime(date);
		this.heureDebut = heureDebut;
		this.duree = duree;
		this.idMR= idMR;
		this.lieu = lieu;
		isMonitricesUpdated = false;
	}
	
	public void updateMonitrices(){
		try{
			monitrices = QueryManager.selectMonitricesDeReprise(id);
		}catch(SQLException e){
			System.out.println("erreur");
		}
		isMonitricesUpdated = true;
	}
	
	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public int getIdMR() {
		return idMR;
	}

	public Lieu getLieu() {
		return lieu;
	}
	
	public ArrayList<Monitrice> getMonitrices() {
		if(!isMonitricesUpdated){
			updateMonitrices();
		}
		return monitrices;
	}
	
	public Date getDate() {
		return new java.sql.Date(date.getTimeInMillis());
	}

	public int getHeureDebut() {
		return heureDebut;
	}

	public int getDuree() {
		return duree;
	}

}
