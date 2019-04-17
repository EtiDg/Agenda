package agenda.ihm.event;

import java.util.EventListener;

import javafx.event.Event;
import javafx.event.EventType;

public class NouvelleRepriseExceptionnelleEvent extends Event{
    private static final long serialVersionUID = 97860854604065L;
    
    public static final EventType<NouvelleRepriseEvent> NOUVELLE_REPRISE_EXCEPTIONNELLE =
            new EventType<>(Event.ANY, "NOUVELLE_REPRISE_EXCEPTIONNELLE");
    
    public NouvelleRepriseExceptionnelleEvent() {
    	super(NOUVELLE_REPRISE_EXCEPTIONNELLE);
    }
}

interface NouvelleRepriseExceptionnelleListener extends EventListener{
	public void onNouvelleRepriseExceptionnelle(NouvelleRepriseExceptionnelleEvent e);
}
