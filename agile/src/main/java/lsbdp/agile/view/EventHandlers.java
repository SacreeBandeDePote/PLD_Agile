package lsbdp.agile.view;

import java.io.File;
import java.text.ParseException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.algorithm.Scheduler;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;

public class EventHandlers {

	@FXML
	private void LoadDeliveriesActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your deliverires file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		File f = MainWindow.openFileChooserDeliveries(fileChooser);
		Controller.loadDeliveryRequest(f);
	}
	
	@FXML
	private void LoadMapActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your map file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		File f = MainWindow.openFileChooser(fileChooser);
		Controller.loadMap(f);
	}
	
	@FXML
	private void calculateSchedule(ActionEvent event) {
		// DeliverySchedule schedule = Controller.findDeliverySchedule();
	}

	@FXML
	private void shortestPathButtonHandler (ActionEvent event){
		/*Dijkstra dj = new Dijkstra(streetMap);
		Route r = dj.performDijkstra(selectedDeliveries.get(0).getLocation(), selectedDeliveries.get(1).getLocation());
		colorRoute(r);*/
	}
	
	public static void highlightIntersection(Intersection intersection) {
		
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());
		
		circle.setFill(Color.BLUE);
		circle.setStroke(Color.BLUE);
		circle.setStrokeWidth(8d);
	}
	
	public static void highlightWarehouse(Intersection warehouse) {
			
			Scene scene = WindowManager.getScene();
			Circle circle = (Circle) scene.lookup("#Circle"+warehouse.getId());
			
			circle.setFill(Color.GREEN);
			circle.setStroke(Color.GREEN);
			circle.setStrokeWidth(8d);
		}
	
	public static void unhighlightIntersection(Intersection intersection) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());
		
		circle.setFill(Color.RED);
		circle.setStroke(Color.RED);
		circle.setStrokeWidth(1d);	
	}
	
	public static void unhighlightWarehouse(Intersection warehouse) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+warehouse.getId());
		
		circle.setFill(Color.GREEN);
		circle.setStroke(Color.GREEN);
		circle.setStrokeWidth(1d);	
	}
	
	public static void highlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = ""+delivery.getLocation().getId();
		for(HBox hbox : list) {
			Label l = (Label) hbox.getChildren().get(1);
			if(l.getId().compareTo(id) == 0) {
				//hbox.setStyle("-fx-background-color : d21919");
				listview.getSelectionModel().select(hbox);
			}
		}
	}
	
	public static void unhighlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = ""+delivery.getLocation().getId();
		for(HBox hbox : list) {
			Label l = (Label) hbox.getChildren().get(1);
			if(l.getId().compareTo(id) == 0) {
				//hbox.setStyle("-fx-background-color : transparent");
				listview.getSelectionModel().clearSelection();
			}
		}
	}
	
	public static void deleteDelivery(Delivery delivery) {
		Controller.cmdDelete(delivery);
	}
	
}
