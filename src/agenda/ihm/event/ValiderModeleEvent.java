package agenda.ihm.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ValiderModeleEvent extends Event{
	
    private static final long serialVersionUID = 8675435665436735L;
    
    
    public static final EventType<ValiderModeleEvent> VALIDER_MODELE =
            new EventType<>(Event.ANY, "VALIDER_MODELE");
    
    
    public ValiderModeleEvent() {
    		super(VALIDER_MODELE);
    }
    
}
