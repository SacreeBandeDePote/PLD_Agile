package lsbdp.agile.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.algorithm.Scheduler;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.*;

import java.io.File;
import java.text.ParseException;
import java.util.*;

public class WindowManager{

	private static int MIN_X;
	private static int MIN_Y;
	private static int MAX_X;
	private static int MAX_Y;
	private static Scene scene;

	public static CanvasDrawer canvasDrawer = null;
	
	private static StreetMap streetMap;
	private static DeliveriesRequest deliveriesRequest;
	private static Canvas cv;
	private static Button computeButton;
	
		private static ArrayList<Delivery> selectedDeliveries;
		
	public static void initializer (Scene scene) {
		WindowManager.scene = scene;
		SplitPane sp = (SplitPane) scene.lookup("#mainSplitPane");
		sp.getDividers().get(0).setPosition(0.85);
	}
	
	public static void colorDeliverySchedule (DeliverySchedule ds) {
		for (Pair<Route, Delivery> p : ds) {
			colorRoute(p.getKey());
			colorIntersection(p.getValue().getLocation());
		}
	}

	public static void colorRoute(Route route) {
		Intersection startingPoint = route.getStartingPoint();
		List<Street> streets =  route.getStreets();

		for( Street street : streets) {
			Intersection end = street.getEnd();
			canvasDrawer.drawStreet(startingPoint, end, Color.RED);
			startingPoint = end;

		}

	}
	
	public static void colorIntersection(Intersection inter) {
		canvasDrawer.drawIntersection(inter, Color.RED, (double)10);
	}

	public static void colorDeliveryRequest(DeliveriesRequest r) {
		deliveriesRequest = r;
		Pane overlay = (Pane) scene.lookup("#overlay");

		ArrayList<Delivery> list = new ArrayList<Delivery>();
		list = r.getDeliveryList();
		
		for(Delivery d : list) {
			canvasDrawer.drawDelivery(overlay, d, Color.RED, 5d);
		}
		Intersection wh = r.getWarehouse();
		canvasDrawer.drawWarehouse(overlay, wh, Color.GREEN, 5d);	
	}


	public static void loadListView(DeliveriesRequest dr) {
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		computeButton = (Button)scene.lookup("#computeButton");
		
		ObservableList<HBox> ol = FXCollections.observableArrayList();
		selectedDeliveries = new ArrayList<Delivery>();
		
		ol.add(WidgetBuilder.createListViewHBoxWarehouse(dr.getWarehouse()));

		int cpt = 1;
		for(Delivery d : dr.getDeliveryList()) {
			ol.add(WidgetBuilder.createListViewHBox(d, cpt));
			cpt++;
		}
		listview.getItems().clear();
		listview.setItems(ol);
		listview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Si on a besoin de calculer une route entre deux intersections  JUST IN CASE
		/*listview.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv) -> {
			selectedDeliveries.clear();
			for (Label l : listview.getSelectionModel().getSelectedItems()) {
				selectedDeliveries.add(dr.getDeliveryByIntersectionId(Long.parseLong(l.getId())));
			}
			if(selectedDeliveries.size() == 2) {
				computeButton.setDisable(false);
			} else {
				computeButton.setDisable(true);
			}
		}
		); */
	}
	
	public static void drawMap(StreetMap map) {
		if(canvasDrawer == null) {
			canvasDrawer = new CanvasDrawer(map.getMaxX(), map.getMinX(), map.getMaxY(), map.getMinY(), scene);	
		}
		canvasDrawer.drawMap(map, scene);
	}
	
	public static Scene getScene() {
		return scene;
	}
}
