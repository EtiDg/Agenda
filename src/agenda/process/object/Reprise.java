package agenda.process.object;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

import agenda.process.sql.QueryManager;
import javafx.util.converter.LocalTimeStringConverter;

public class Reprise {
	
	private long id;
	private String nom;
	private LocalDate date;
	private int heureDebut;
	private int heureFin;
	private long idMR;
	private Lieu lieu;
	boolean isMonitricesUpdated = false;
	private ArrayList<Monitrice> monitrices;

	
	public Reprise(String nom, LocalDate date, int heureDebut, int heureFin, long idMR, Lieu lieu, ArrayList<Monitrice> monitrices){
		this.nom = nom;
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.idMR= idMR;
		this.lieu = lieu;
		this.monitrices = monitrices;
		isMonitricesUpdated=true;
		id = IdGenerator.getId();
	}

	public Reprise(long id, String nom, LocalDate date, int heureDebut, int heureFin, long idMR, Lieu lieu,  ArrayList<Monitrice> monitrices){
		this.id = id;
		this.nom = nom;
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.idMR= idMR;
		this.lieu = lieu;
		this.monitrices = monitrices;
		isMonitricesUpdated = true;
	}
	
	public Reprise(long id, String nom, LocalDate date, int heureDebut, int heureFin, long idMR, Lieu lieu){
		this.id = id;
		this.nom = nom;
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.idMR= idMR;
		this.lieu = lieu;
		isMonitricesUpdated = false;
	}
	
	public void updateMonitrices(){
		try{
			monitrices = QueryManager.selectMonitricesDeReprise(id);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println("erreur lors de la mise � jour des monitrices");
			System.exit(0);
		}
		isMonitricesUpdated = true;
	}
	
	public long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public long getIdMR() {
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

	public LocalDate getDate() {
		return date;
	}
	
	public Date getSQLDate() {
		return Date.valueOf(date);
	}

	public int getHeureDebut() {
		return heureDebut;
	}

	public int getHeureFin() {
		return heureFin;
	}
	
	@Override
	public String toString(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH'h'mm");
		return nom + ":    " 
			+ formatter.format(date) + "    " 
			+ formatterHeure.format(LocalDateTime.of(0, 1, 1, (heureDebut-heureDebut%60)/60, heureDebut%60)) + "-" 
			+ formatterHeure.format(LocalDateTime.of(0, 1, 1, (heureFin-heureFin%60)/60, heureFin%60))
			+ " " + lieu.getNom();
//		return nom + ":    " 
//			+ formatter.format(date) + "    " 
//			+ ((heureDebut-heureDebut%60)/60) + "h" + heureDebut%60 + "-" 
//			+ ((heureFin-heureFin%60)/60) + "h" + heureFin%60
//			+ " " + lieu.getNom();
	}

}
