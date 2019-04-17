package agenda.ihm.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class CellClickedEvent extends Event{

	private static final long serialVersionUID = 7657418563786544152L;

	private int cell;
	private MouseEvent event;

	public static final EventType<CellClickedEvent> CELL_SELECTED =
			new EventType<>(Event.ANY, "CELL_SELECTED");


	public CellClickedEvent(int cell, MouseEvent event) {
		super(CELL_SELECTED);
		this.cell = cell;
		this.event = event;
	}

	public int getCell(){
		return cell;
	}

	public MouseEvent getMouseClick(){
		return event;
	}
}
