package agenda.process.object;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class Reprise {
	
	private int id;
	private String nom;
	private String nomGroupe;
	private int idLieu;
	private ArrayList<Integer> monitrices;
	private Calendar date;
	private int heureDebut;
	private int duree;
	
	public Reprise(String nom, String nomGroupe, int idLieu, Date date, int heureDebut, int duree){
		this.nom = nom;
		this.nomGroupe = nomGroupe;
		this.idLieu = idLieu;
		this.date.setTime(date);
		this.heureDebut = heureDebut;
		this.duree = duree;
	}
	
	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getNomGroupe() {
		return nomGroupe;
	}

	public int getIdLieu() {
		return idLieu;
	}
	
	public ArrayList<Integer> getMonitrices() {
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
