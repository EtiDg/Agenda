package agenda.ihm.event;

import javafx.event.Event;
import javafx.event.EventType;

public class NouveauGroupeEvent extends Event{

	private static final long serialVersionUID = 9865489653168754L;
    
    public static final EventType<NouveauGroupeEvent> NOUVEAU_GROUPE =
            new EventType<>(Event.ANY, "NOUVEAU_GROUPE");
    
    public NouveauGroupeEvent() {
    	super(NOUVEAU_GROUPE);
    }
}