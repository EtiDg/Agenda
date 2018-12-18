package agenda.process.object;

public class Groupe {

	private String nom;
	private boolean isVacances;
	private boolean isTreveHivernale;
	private ModeleDeReprise modeleDeReprise;
	
	public Groupe(String nom, boolean isVacances, boolean isTreveHivernale, ModeleDeReprise modeleDeReprise){
		this.nom = nom;
		this.isVacances = isVacances;
		this.isTreveHivernale = isTreveHivernale;
		this.modeleDeReprise = modeleDeReprise;
	}
	
	public Boolean getIsVacances() {
		return isVacances;
	}

	public Boolean getIsTreveHivernale() {
		return isTreveHivernale;
	}

	public ModeleDeReprise getModeleDeReprise() {
		return modeleDeReprise;
	}

	public String getNom(){
		return nom;
	}
}
