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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.algorithm.Scheduler;
import lsbdp.agile.data.SerializeXML;
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

	private static CanvasDrawer canvasDrawer = null;
	
	private static StreetMap streetMap;
	private static DeliveriesRequest deliveriesRequest;
	private static Canvas cv;
	private static Button computeButton;
	
		private static ArrayList<Delivery> selectedDeliveries;
		
	public static void initializer (Scene scene) {
		WindowManager.scene = scene;
	}
	
	/*private static int getMaxY(StreetMap map) {
		int maxX = 0;
		Map<Long, Intersection> intersections = map;
		Set<Long> keys = intersections.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			if(maxX < intersection.getY()) {
				maxX = intersection.getY();
			}
		}
		return maxX+5;
	}

	private static int getMinY(StreetMap map) {
		int minX = 100000000;
		Map<Long, Intersection> intersections = map;
		Set<Long> keys = intersections.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			if(minX > intersection.getY()) {
				minX = intersection.getY();
			}
		}
		return minX+5;
	}

	private static int getMaxX(StreetMap map) {
		int maxX = 0;
		Map<Long, Intersection> intersections = map;
		Set<Long> keys = intersections.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			if(maxX < intersection.getX()) {
				maxX = intersection.getX();
			}
		}
		return maxX+5;
	}

	private static int getMinX(StreetMap map) {
		int minX = 100000000;
		Map<Long, Intersection> intersections = map;
		Set<Long> keys = intersections.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			if(minX > intersection.getX()) {
				minX = intersection.getX();
			}
		}
		return minX-5;
	}

	@FXML
	private MenuItem calculateButton;

	@FXML
	private static SplitPane mainSplitPane;

	@FXML
	private void calculateSchedule(ActionEvent event) {
		Scheduler sc = new Scheduler(streetMap, deliveriesRequest.getWarehouse(), deliveriesRequest.getDeliveryList(), "glouton");
		DeliverySchedule ds =  sc.findSchedule();
		for( Pair<Route,Delivery> p : ds) {
			colorRoute(p.getKey());
		}

	}

	@FXML
	private void computeAlgo (ActionEvent event){
		Dijkstra dj = new Dijkstra(streetMap);
		Route r = dj.performDijkstra(selectedDeliveries.get(0).getLocation(), selectedDeliveries.get(1).getLocation());
		colorRoute(r);
	}

	@FXML
	private void LoadMapActionHandler(ActionEvent event) throws InterruptedException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your map file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		File f = MainWindow.openFileChooser(fileChooser);
		try {
			streetMap = SerializeXML.serializeMapXML(f);
			initializeMap(streetMap);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void LoadDeliveriesActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your deliverires file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		File f = MainWindow.openFileChooserDeliveries(fileChooser, streetMap);

	}*/
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
		loadListView(r);
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		cv = (Canvas) ap.getChildren().get(0);
		ArrayList<Delivery> list = new ArrayList<Delivery>();
		list = r.getDeliveryList();
		for(Delivery d : list) {
			Intersection inter = d.getLocation();
			colorIntersection(inter);

		}
		Intersection wh = r.getWarehouse();
		colorIntersection(wh);	
	}


	public static void loadListView(DeliveriesRequest dr) {
		ListView<Label> listview = (ListView<Label>) scene.lookup("#listView");
		computeButton = (Button)scene.lookup("#computeButton");
		
		ObservableList<Label> ol = FXCollections.observableArrayList();
		selectedDeliveries = new ArrayList<Delivery>();
		
		Label warehouse = new Label("Warehouse");
		warehouse.setId(String.valueOf(dr.getWarehouse().getId()));
		ol.add(warehouse);

		int cpt = 1;
		for(Delivery d : dr.getDeliveryList()) {
			Label l = WidgetBuilder.createDeliveryLabel(d, cpt, cv);
			cpt++;
			ol.add(l);
		}
		listview.getItems().clear();
		listview.setItems(ol);
		listview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		listview.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv) -> {
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
		);
	}
	

	public static void drawMap(StreetMap map) {
		if(canvasDrawer == null) {
			canvasDrawer = new CanvasDrawer(map.getMaxX(), map.getMinX(), map.getMaxY(), map.getMinY(), scene);	
		}
		canvasDrawer.drawMap(map, scene);
	}


/*
	@FXML
	private void loadCanvas(MouseEvent event) {

		FXCollections.observableArrayList();
	}

	

	public static Double normalizeX(Double x, Double width) {
		Double newX = (x-MIN_X)/(MAX_X-MIN_X);
		newX *= width;
		return newX;
	}
	public static Double normalizeY(Double y, Double height) {
		Double newY = (y-MIN_Y)/(MAX_Y-MIN_Y);
		newY *= height;
		return newY;
	}*/


}
