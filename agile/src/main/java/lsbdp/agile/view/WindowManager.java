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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.algorithm.Scheduler;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.*;

import java.io.File;
import java.text.ParseException;
import java.util.*;

import com.sun.javafx.scene.canvas.CanvasHelper.CanvasAccessor;

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
		Controller controller = new Controller();
	}
	
	public static void colorDeliverySchedule (DeliverySchedule ds) {
		Pane overlay = (Pane) scene.lookup("#overlay");
		overlay.getChildren().clear();
		Intersection warehouse = ds.get(0).getKey().getStartingPoint();
		canvasDrawer.drawWarehouse(overlay, warehouse, Color.GREEN, 5d);
		for (Pair<Route, Delivery> p : ds) {
			System.out.println(p.getKey() +" " + p.getValue());
			if(p.getKey() != null && p.getValue() != null) {
				colorRoute(p.getKey(), p.getValue());
			}
		}
	}

	public static void colorRoute(Route route, Delivery delivery) {
		Pane overlay = (Pane) scene.lookup("#overlay");
		Intersection startingPoint = route.getStartingPoint();
		List<Street> streets       = route.getStreets();
		

		for( Street street : streets) {
			canvasDrawer.drawDelivery(overlay, delivery, Color.RED, 5d);
			Intersection end = street.getEnd();
			canvasDrawer.drawStreetOverlay(overlay, startingPoint, end, Color.RED);
			startingPoint = end;
		}
	}

	public static void colorDeliveryRequest(DeliveriesRequest r) {
		deliveriesRequest        = r;
		Pane overlay             = (Pane) scene.lookup("#overlay");
		ArrayList<Delivery> list = new ArrayList<Delivery>();
		list                     = r.getDeliveryList();
		
		overlay.getChildren().clear();
		for(Delivery d : list) {
			canvasDrawer.drawDelivery(overlay, d, Color.RED, 5d);
		}
		Intersection wh = r.getWarehouse();
		canvasDrawer.drawWarehouse(overlay, wh, Color.GREEN, 5d);	
	}


	public static void loadListView(DeliverySchedule ds) {
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		computeButton = (Button)scene.lookup("#computeButton");
		
		ObservableList<HBox> ol = FXCollections.observableArrayList();
		selectedDeliveries = new ArrayList<Delivery>();
		
		ol.add(WidgetBuilder.createListViewHBoxWarehouse(ds.get(0).getKey().getStartingPoint()));

		int cpt = 1;
		for(Pair<Route,Delivery> p : ds) {
				if (p.getValue() != null) {
				ol.add(WidgetBuilder.createListViewHBox(p.getValue(), cpt));
				cpt++;
			}
		}
		listview.getItems().clear();
		listview.setItems(ol);
		listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
	
	public static void highlightAll(StreetMap map, DeliverySchedule schedule) {
		Pane overlay = (Pane) scene.lookup("#overlay");
		Set<Long> keys     = map.keySet();
		Iterator iterator  = keys.iterator();
		
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = map.get(key);
			Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());
			if(circle == null) {
				canvasDrawer.drawTemporaryIntersection(overlay, intersection, Color.GRAY, 3d);
			}
		}
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
