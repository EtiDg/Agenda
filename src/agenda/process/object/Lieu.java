package agenda.process.object;

public class Lieu {
	
	private long id; 
	private String nom;
	
	public Lieu(String nom){
		this.nom = nom;
		id = IdGenerator.getId();
	}
	
	public Lieu(long id, String nom){
		this.id = id;
		this.nom = nom;
	}
	
	public long getId() {
		return id;
	}

	public void setNom(String nom){
		this.nom = nom;
	}
	
	public String getNom(){
		return nom;
	}
	
	@Override
	public String toString(){
		return nom +  " " + id;
	}
}
