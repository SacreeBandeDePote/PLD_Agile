package lsbdp.agile.view;

import java.io.File;
import java.text.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
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
		WindowManager.canvasDrawer.drawIntersection(intersection, Color.BLUE, (double)10);
	}
	
	public static void unhighlightIntersection(Intersection intersection) {
		WindowManager.canvasDrawer.drawIntersection(intersection, Color.RED, (double)10);
	}
	
	public static void deleteDelivery(Delivery delivery) {
		Controller.cmdDelete(delivery);
	}
}
