package agenda.process.object;

public class Lieu {
	
	private int id; 
	private String nom;
	
	public Lieu(String nom){
		this.nom = nom;
	}
	
	public Lieu(int id, String nom){
		this.id = id;
		this.nom = nom;
	}
	
	public int getId() {
		return id;
	}

	public String getNom(){
		return nom;
	}
}
