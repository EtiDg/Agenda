package agenda.ihm.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

public class NouvellePageEvent extends Event{

    private static final long serialVersionUID = 458673546845257427L;
    
    Node page;
    
    public static final EventType<NouvellePageEvent> NOUVELLE_PAGE =
            new EventType<>(Event.ANY, "NOUVELLE_PAGE");
    
    
    public NouvellePageEvent(Node page) {
    		super(NOUVELLE_PAGE);
    		this.page = page;
    }
    
    public Node getPage(){
    	return page;
    }
}
