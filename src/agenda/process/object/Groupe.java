package agenda.process.object;

public class Groupe {

	private String nom;
	private boolean isVacances;
	private boolean isTreveHivernale;
	private int idMR;
	
	public Groupe(String nom, boolean isVacances, boolean isTreveHivernale, int idMR){
		this.nom = nom;
		this.isVacances = isVacances;
		this.isTreveHivernale = isTreveHivernale;
		this.idMR = idMR;
	}
	
	public Boolean getIsVacances() {
		return isVacances;
	}

	public Boolean getIsTreveHivernale() {
		return isTreveHivernale;
	}

	public int getIdMR() {
		return idMR;
	}

	public String getNom(){
		return nom;
	}
}
