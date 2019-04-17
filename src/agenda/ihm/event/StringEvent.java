package agenda.ihm.event;

import java.util.EventListener;

import javafx.event.Event;
import javafx.event.EventType;

public class StringEvent extends Event {
	
    private static final long serialVersionUID = 68541556431L;
    
    private String valeur;
    
    public static final EventType<StringEvent> STRING_EVENT =
            new EventType<>(Event.ANY, "STRING_EVENT");
    
    public StringEvent(String valeur) {
    	super(STRING_EVENT);
    	this.valeur = valeur;
    }
    
    public String getValeur(){
    	return valeur;
    }
    
    interface StringListener extends EventListener{
    	public void onString(StringEvent e);
    }

}