package agenda.ihm.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ValiderMonitriceEvent extends Event{
	
    private static final long serialVersionUID = 985486524562787524L;
    
    
    public static final EventType<ValiderModeleEvent> VALIDER_MONITRICE =
            new EventType<>(Event.ANY, "VALIDER_MONITRICE");
    
    
    public ValiderMonitriceEvent() {
    		super(VALIDER_MONITRICE);
    }
    
}
