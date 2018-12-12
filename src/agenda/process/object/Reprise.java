package agenda.process.object;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class Reprise {
	
	private int id;
	private String nom;
	private String nomGroupe;
	private String nomLieu;
	private ArrayList<String> monitrices;
	private Calendar date;
	private int heureDebut;
	private int duree;
	
	public Reprise(String nom, String nomGroupe, String nomLieu, Date date, int heureDebut, int duree){
		this.nom = nom;
		this.nomGroupe = nomGroupe;
		this.nomLieu = nomLieu;
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

	public String getNomLieu() {
		return nomLieu;
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
