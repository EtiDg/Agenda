package agenda.ihm.event;

import java.util.EventListener;

import javafx.event.Event;
import javafx.event.EventType;

public class NouvelleRepriseEvent extends Event{
    private static final long serialVersionUID = 4961051306513L;
    
    public static final EventType<NouvelleRepriseEvent> NOUVELLE_REPRISE =
            new EventType<>(Event.ANY, "NOUVELLE_REPRISE");
    
    public NouvelleRepriseEvent() {
    	super(NOUVELLE_REPRISE);
    }
    
}

interface NouvelleRepriseListener extends EventListener{
	public void onNouvelleReprise(NouvelleRepriseEvent e);
}