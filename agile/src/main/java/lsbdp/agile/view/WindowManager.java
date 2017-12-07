package lsbdp.agile.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WindowManager{

	private static Scene scene;

	public static boolean mapLoaded = false;
	public static boolean deliveriesLoaded = false;

	public static CanvasDrawer canvasDrawer = null;

	private static ArrayList<Delivery> selectedDeliveries;

	public static void initializer (Scene scene) {
		WindowManager.scene = scene;
		
		
		/*HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setId("canvasHBox");
		hb.setStyle("-fx-background-color: derive(#ececec,26.4%);");*/
		
		
		StackPane sPane = (StackPane) scene.lookup("#mainStackPane");


		sPane.setStyle("-fx-background-color: derive(#ececec,26.4%)");
		//sPane.getChildren().add(hb);
		SplitPane sp = (SplitPane) scene.lookup("#mainSplitPane");
		sp.getDividers().get(0).setPosition(0.85);
		new Controller();
		KeyCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_ANY);
		KeyCombination ctrlY = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_ANY);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(ctrlZ.match(arg0)) {
					Controller.undo();
				}
				if(ctrlY.match(arg0)) {
					Controller.redo();
				}
			}

		});
	}
	
	public static void loadTimeDoughnut(DeliverySchedule schedule) {
		HBox hboxTimeDgnt = (HBox) scene.lookup("#timeDoughnutHBox");
		Pane overlay = new Pane();
		overlay.setPrefWidth(hboxTimeDgnt.getWidth());
		overlay.setPrefHeight(hboxTimeDgnt.getHeight());
		CanvasDrawer.fillTimeDoughnut(overlay, schedule, scene);
		hboxTimeDgnt.getChildren().clear();
		hboxTimeDgnt.getChildren().add(overlay);
	}
	
	public static void colorDeliverySchedule (DeliverySchedule ds) {
		Pane overlay = (Pane) scene.lookup("#overlay");
		
		overlay.getChildren().clear();
		Intersection warehouse = ds.get(0).getKey().getStartingPoint();
		canvasDrawer.drawWarehouse(overlay, warehouse, Color.GREEN, 5d);
		for (Pair<Route, Delivery> p : ds) {
				colorRoute(p.getKey(), p.getValue());
		}
	}

	public static void colorRoute(Route route, Delivery delivery) {
		Pane overlay = (Pane) scene.lookup("#overlay");
		Intersection startingPoint = route.getStartingPoint();
		List<Street> streets       = route.getStreets();


		for( Street street : streets) {
			if(delivery != null) {
				canvasDrawer.drawDelivery(overlay, delivery, Color.RED, 5d);
			}
			Intersection end = street.getEnd();
			canvasDrawer.drawStreetOverlay(overlay, startingPoint, end, Color.RED);
			startingPoint = end;
		}
	}

	public static void colorDeliveryRequest(DeliveriesRequest r) {
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
		Iterator<Long> iterator  = keys.iterator();

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

	public static void openErrorPopUp(String msg) {
		MainWindow.openMessagePopup(msg);
	}
}
