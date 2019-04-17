package agenda.ihm.event;


import javafx.event.Event;
import javafx.event.EventType;

public class NouveauModeleEvent extends Event {

	private static final long serialVersionUID = 681735067465L;
    
    public static final EventType<NouveauModeleEvent> NOUVEAU_MODELE =
            new EventType<>(Event.ANY, "NOUVEAU_MODELE");
    
    public NouveauModeleEvent() {
    	super(NOUVEAU_MODELE);
    }
}

