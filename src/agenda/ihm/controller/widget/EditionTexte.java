package agenda.ihm.controller.widget;

import agenda.ihm.event.StringEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class EditionTexte extends Edition{

	private StringProperty valeur;
	@FXML private Text textValeur;
	private TextField textField;

	
	public EditionTexte(){
		super();
        valeurProperty();
        textField = new TextField(valeur.get());
	}
	
	@Override
	public void handleEditer(MouseEvent e){
		if (e.getButton() == MouseButton.PRIMARY){
			editer(textField);
	        textField.requestFocus(); 
	        textField.selectAll();
		}
	}
	
	@Override
	public void handleValider(MouseEvent e){
		if (e.getButton() == MouseButton.PRIMARY){
			fireEvent(new StringEvent(valeur.get()));
			setValeur(textField.getText());
			valider(textValeur);
	        textField.requestFocus(); 
	        textField.selectAll();
	        fireEvent(new StringEvent(textField.getText()));
		}
	}
	
	public final StringProperty valeurProperty() {
		if (valeur == null) {
			valeur = new SimpleStringProperty("valeur");
		}
		return valeur;
	}
	
	public String getValeur(){
		return valeur.get();
	}
	
	public void setValeur(String titre){
		this.valeur.set(titre);
		textValeur.setText(titre);
	}
	
}
