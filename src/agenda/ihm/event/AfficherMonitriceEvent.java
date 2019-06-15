package agenda.ihm.event;

import agenda.process.object.ModeleDeReprise;
import agenda.process.object.Monitrice;
import javafx.event.Event;
import javafx.event.EventType;

public class AfficherMonitriceEvent extends Event{

    private static final long serialVersionUID = 987668534685346532L;
    
    Monitrice monitrice;
    
    public static final EventType<AfficherModeleDeRepriseEvent> AFFICHER_MONITRICE =
            new EventType<>(Event.ANY, "AFFICHER_MONITRICE");
    
    
    public AfficherMonitriceEvent(Monitrice monitrice) {
    		super(AFFICHER_MONITRICE);
    		this.monitrice = monitrice;
    }
    
    public Monitrice getModeleDeReprise(){
    	return monitrice;
    }
}
