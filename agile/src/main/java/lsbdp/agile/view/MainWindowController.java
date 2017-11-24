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

public class MainWindowController{

	private static int MIN_X;
	private static int MIN_Y;
	private static int MAX_X;
	private static int MAX_Y;

	private static StreetMap streetMap;
	private static DeliveriesRequest deliveriesRequest;

	private static Button computeButton;
	
	private static Scene scene;
	private static ArrayList<Delivery> selectedDeliveries;
	
	private static int getMaxY(StreetMap map) {
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
			SerializeXML s = new  SerializeXML();
			streetMap = s.serializeMapXML(f);
			loadMap(streetMap);
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

	}

	public static void colorRoute(Route route) {
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		Canvas cv = (Canvas) ap.getChildren().get(0);
		GraphicsContext gc = cv.getGraphicsContext2D();
		Intersection startingPoint = route.getStartingPoint();
		List<Street> streets =  route.getStreets();

		for( Street street : streets) {
			Double startX = normalizeX((double) startingPoint.getX(), cv.getWidth());
			Double startY = normalizeY((double) startingPoint.getY(), cv.getHeight());
			Intersection end = street.getEnd();

			Double endX = normalizeX((double) end.getX(), cv.getWidth());
			Double endY = normalizeY((double) end.getY(), cv.getHeight());

			gc.setStroke(Color.ORANGE);
			gc.setLineWidth(2);
			gc.strokeLine(startX, startY, endX, endY);

			startingPoint = end;

		}

	}

	public static void colorIntersection(DeliveriesRequest r) {
		deliveriesRequest = r;
		loadListView(r);
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		
		Canvas cv = (Canvas) ap.getChildren().get(0);
		GraphicsContext gc = cv.getGraphicsContext2D();
		ArrayList<Delivery> list = new ArrayList<Delivery>();
		list = r.getDeliveryList();
		for(Delivery d : list) {
			Intersection inter = d.getLocation();
			Double x = normalizeX((double) inter.getX(), cv.getWidth());
			Double y = normalizeY((double) inter.getY(), cv.getHeight());
			gc.strokeOval(x-3, y-3, 6, 6);
			gc.setStroke(Color.RED);
			gc.setFill(Color.RED);
			gc.fillOval(x-3, y-3, 6, 6);

		}
		Intersection wh = r.getWarehouse();
		Double x = normalizeX((double) wh.getX(), cv.getWidth());
		Double y = normalizeY((double) wh.getY(), cv.getHeight());
		gc.setStroke(Color.LIMEGREEN);
		gc.setFill(Color.LIMEGREEN);
		gc.strokeOval(x-5, y-5, 10, 10);
		gc.fillOval(x-5, y-5, 10, 10);
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
			Label l = new Label("Livraison n°"+cpt);
			l.setId(String.valueOf(d.getLocation().getId()));
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
	

	public static void initializer(Scene sc) {

		scene = sc;
		//AnchorPane sp = (AnchorPane) sc.lookup("#canvasAnchorPane");
		//sp.setDividerPosition(0, 0.8);

		//sp.getItems().add(cv);
	}



	@FXML
	private void loadCanvas(MouseEvent event) {

		FXCollections.observableArrayList();
	}

	public static void loadMap(StreetMap map) throws InterruptedException {

		MAX_X = getMaxX(map);
		MIN_X = getMinX(map);
		MAX_Y = getMaxY(map);
		MIN_Y = getMinY(map);
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		Canvas cv = new Canvas(750,750);

		Double canvasWidth = cv.getWidth();
		GraphicsContext gc = cv.getGraphicsContext2D();
		Map<Long, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext() ) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			Double startX = normalizeX((double) intersection.getX(), canvasWidth);
			Double startY = normalizeY((double) intersection.getY(), cv.getHeight());

			List<Street> neighbors = intersection.getStreets();

			gc.setFill(Color.BLUE);
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(1);
			gc.fillOval(startX, startY, 1, 1);
			gc.strokeOval(startX, startY, 1, 1);
			for(Street inter : neighbors) {
				inter.getEnd();
				Double endX = normalizeX((double) inter.getEnd().getX(), canvasWidth);
				Double endY = normalizeY((double) inter.getEnd().getY(), cv.getHeight()); //pute
				gc.setLineWidth(1);
				gc.strokeLine(startX, startY, endX, endY);
			}
		}
		gc.strokeLine(0, 0, canvasWidth, 0);
		gc.strokeLine(0, 0, 0, canvasWidth);
		gc.strokeLine(canvasWidth, canvasWidth, canvasWidth, 0);
		gc.strokeLine(canvasWidth, canvasWidth, 0, canvasWidth);
		ap.getChildren().clear();
		ap.getChildren().add(cv);
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
	}


}
