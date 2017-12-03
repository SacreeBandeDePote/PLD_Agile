package lsbdp.agile.view;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.effect.DropShadow;
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
import lsbdp.agile.model.StreetMap;

public class EventHandlers {

	@FXML
	private void addDelivery(ActionEvent event) {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
		Controller.cmdAdd();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery request");
		}
	}
	
	@FXML
	private void saveDeliveries(ActionEvent event) {
		if(WindowManager.deliveriesLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save your deliveries");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("XML File", "*.xml")
					);
			File f = MainWindow.openFileChooserRoadmap(fileChooser);
		} else {
			MainWindow.openMessagePopup("Please load a delivery file");
		}	
	}
	
	@FXML
	private void generateRoadmapActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		if(WindowManager.deliveriesLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save your roadMap");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("txt File", "*.txt")
					);
			File f = MainWindow.openFileChooserRoadmap(fileChooser);
		} else {
			MainWindow.openMessagePopup("Please load a delivery file");
		}	
	}
	
	@FXML
	private void UndoAction(ActionEvent event) {
		Controller.undo();
	}
	
	@FXML
	private void RedoAction(ActionEvent event) {
		Controller.redo();
	}
	
	@FXML
	private void LoadDeliveriesActionHandler(ActionEvent event) throws InterruptedException, ParseException {
			if(WindowManager.mapLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose your deliverires file");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("XML File", "*.xml")
					);
			File f = MainWindow.openFileChooserDeliveries(fileChooser);
			Controller.loadDeliveryRequest(f);
			WindowManager.deliveriesLoaded = true;
			} else {
				MainWindow.openMessagePopup("Please load a map");
			}
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
		WindowManager.mapLoaded = true;
	}
	
	@FXML
	private void calculateSchedule(ActionEvent event) {
		// DeliverySchedule schedule = Controller.findDeliverySchedule();
	}
	
	public static void highlightIntersection(Intersection intersection) {
		
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLUE);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);
		
		circle.setEffect(dropShadow);
		circle.setFill(Color.BLUE);
		circle.setStroke(Color.BLUE);
		circle.setStrokeWidth(8d);
	}
	
	public static void highlightWarehouse(Intersection warehouse) {
			
			Scene scene = WindowManager.getScene();
			Circle circle = (Circle) scene.lookup("#CircleWarehouse"+warehouse.getId());
			
			DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(Color.GREEN);
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			
			circle.setEffect(dropShadow);
			circle.setFill(Color.GREEN);
			circle.setStroke(Color.GREEN);
			circle.setStrokeWidth(8d);
		}
	
	public static void unhighlightIntersection(Intersection intersection) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());
		
		circle.setEffect(null);
		circle.setFill(Color.RED);
		circle.setStroke(Color.RED);
		circle.setStrokeWidth(1d);	
	}
	
	public static void unhighlightWarehouse(Intersection warehouse) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#CircleWarehouse"+warehouse.getId());
		
		circle.setEffect(null);
		circle.setFill(Color.GREEN);
		circle.setStroke(Color.GREEN);
		circle.setStrokeWidth(1d);	
	}
	
	public static void highlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = "Delivery-"+delivery.getLocation().getId();
		for(HBox hbox : list) {
			if (hbox.getChildren().size() > 1) {
				Label l = (Label) hbox.getChildren().get(1);
				if(l.getId().compareTo(id) == 0) {
					//hbox.setStyle("-fx-background-color : d21919");
					listview.getSelectionModel().select(hbox);
				}
			}
		}
	}
	
	public static void highlightWarehouseListView() {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		listview.getSelectionModel().select(0);
	}
	
	public static void unhighlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = ""+delivery.getLocation().getId();
		for(HBox hbox : list) {
			if (hbox.getChildren().size() > 1) {
				Label l = (Label) hbox.getChildren().get(1);
				String labelId = l.getId().substring(l.getId().lastIndexOf("-")+1);
				if(labelId.compareTo(id) == 0) {
					//hbox.setStyle("-fx-background-color : transparent");
					listview.getSelectionModel().clearSelection();
				}
			}
		}
	}
	
	public static void unhighlightWarehouseListView () {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		listview.getSelectionModel().clearSelection();
	}
		
	public static void highlightTemporaryIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Tmp"+intersection.getId());
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLUE);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);
		
		circle.setEffect(dropShadow);
		circle.setStrokeWidth(6d);
	}
	
	public static void unhighlightTemporaryIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Tmp"+intersection.getId());
		
		circle.setEffect(null);
		circle.setStrokeWidth(2d);
	}
	
	public static void deleteDelivery(Delivery delivery) {
		Controller.cmdDelete(delivery);
	}

	public static void temporaryIntersectionClicked(Intersection intersection) {
		MainWindow.openAddPopUp(intersection);
	}

	public static void addDelivery(Intersection intersection, String duration, String startingTime, String endTime) {
		int tmpDuration = Integer.parseInt(duration);
		Date start = new Date(10);
		Date end = new Date(11);
		Delivery d = new Delivery(tmpDuration, start, end, intersection);
		Controller.cmdAdd2(d);
	}
	
}
