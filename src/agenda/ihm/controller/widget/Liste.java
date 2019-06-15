package agenda.ihm.controller.widget;

import java.io.IOException;
import java.util.ArrayList;

import agenda.MainApp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Liste<T> extends AnchorPane{
	
	private BooleanProperty isMultiple; 
	@FXML private Text textTitre;
	private StringProperty titre;
	@FXML private ListView<T> listView;
    ObservableList<T> list = FXCollections.observableArrayList();
	
    public Liste() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/widget/Liste.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // initialise le titre
        titreProperty();
        isMultipleProperty();
    }
	
	public void loadListe(ArrayList<T> liste){
		listView.getItems().clear();
	    ObservableList<T> observableList = FXCollections.observableArrayList();
	    observableList.addAll(liste);
		listView.setItems(observableList);
	}
	
//	public void selectItems(ArrayList<T> objects){
//		for (int i=0; i<objects.size();i++){
//			System.out.println(objects.get(i));
//			for (int j=0; j<listView.getItems().size();j++){
//				System.out.println(listView.getItems().get(j));
//				if (objects.get(i).equals(listView.getItems().get(j))){
//					System.out.println("equal");
//					listView.getSelectionModel().select(listView.getItems().get(j));
//				}
//			}
//		}
//	}
	
	public void selectItem(T object){
		listView.getSelectionModel().select(object);
	}
	
	public ArrayList<T> getListe(){
		ArrayList<T> liste = new ArrayList<T>();
		liste.addAll(listView.getItems());
		return liste;
	}
	
	public void remove(T object){
		listView.getItems().remove(object);
	}
	
	public void removeSelectedItems(){
		listView.getItems().removeAll(listView.getSelectionModel().getSelectedItems());
		listView.getSelectionModel().clearSelection();
	}
	
	public void add(T object){
		listView.getItems().add(object);
	}
	
	public ArrayList<T> getSelectedItems(){
		return new ArrayList<T>(listView.getSelectionModel().getSelectedItems());
	}
	
	public BooleanProperty isMultipleProperty() {
		if (isMultiple == null) {
			isMultiple = new SimpleBooleanProperty(false);
		}
		if (isMultiple.get()){
			listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		} else{
			listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		}
		return isMultiple;
	}
	
	public boolean getIsMultiple(){
		return isMultiple.get();
	}
	
	public void setIsMultiple(boolean isMultiple){
		this.isMultiple.set(isMultiple);
		if (isMultiple){
			listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		} else{
			listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		}
	}
	
	public StringProperty titreProperty() {
		if (titre == null) {
			titre = new SimpleStringProperty("Titre");
		}
		textTitre.setText("Titre");
		return titre;
	}
	
	public String getTitre(){
		return titre.get();
	}
	
	public void setTitre(String titre){
		this.titre.set(titre);
		textTitre.setText(titre);
	}
	
	public ListView<T> getListView(){
		return listView;
	}
	
	
}
