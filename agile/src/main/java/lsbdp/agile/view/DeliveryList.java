package lsbdp.agile.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class DeliveryList {
	public static ListView<VBox> createListView () {
		ListView<VBox> deliveryList = new ListView<VBox>();

		ObservableList<VBox> deliveries = FXCollections.observableArrayList();
		
		VBox item1 = createDelivery("Djag", 14, 16, "8 rue liberté, VILLEURBANNE");
		VBox item2 = createDelivery("Papy", 15, 17, "12 bv orange BORDEAUX");
		
		deliveries.add(item1);
		deliveries.add(item2);
		
		deliveryList.setItems(deliveries);
		return deliveryList;
	}
	
	public static VBox createDelivery(String clientName, int timeStart, int timeEnd, String adresse) {
		VBox vboxMain = new VBox(4);
		Insets margedInsets = new Insets(0,0,0,15);
		
		Label dd = new Label();
		dd.setText("Livraison chez "+clientName);
							
		Label dh = new Label();
		dh.setText(timeStart+"h - "+timeEnd+"h");
		dh.setPadding(margedInsets);
		
		Label da = new Label();
		da.setText(adresse);
		da.setPadding(margedInsets);
		
		vboxMain.getChildren().addAll(dd,dh,da);
		return vboxMain;
	}
}
