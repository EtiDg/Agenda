package agenda.ihm.event;

import java.util.EventListener;

import agenda.process.object.ModeleDeReprise;
import javafx.event.Event;
import javafx.event.EventType;

public class AfficherModeleDeRepriseEvent extends Event{

    private static final long serialVersionUID = 89608543654130L;
    
    ModeleDeReprise modeleDeReprise;
    
    public static final EventType<AfficherModeleDeRepriseEvent> AFFICHER_MODELE_DE_REPRISE =
            new EventType<>(Event.ANY, "AFFICHER_MODELE_DE_REPRISE");
    
    
    public AfficherModeleDeRepriseEvent(ModeleDeReprise modeleDeReprise) {
    		super(AFFICHER_MODELE_DE_REPRISE);
    		this.modeleDeReprise = modeleDeReprise;
    }
    
    public ModeleDeReprise getModeleDeReprise(){
    	return modeleDeReprise;
    }
}

interface AfficherModeleDeRepriseListener extends EventListener{
	public void onAffichageModeleDeReprise(AfficherModeleDeRepriseEvent e);
}
