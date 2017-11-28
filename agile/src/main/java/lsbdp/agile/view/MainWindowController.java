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

	public static CanvasDrawer canvasDrawer;
	
	private static StreetMap streetMap;
	private static DeliveriesRequest deliveriesRequest;
	private static Canvas cv;
	private static Button computeButton;
	
	private static Scene scene;
	private static ArrayList<Delivery> selectedDeliveries;
	
	@FXML
	private MenuItem calculateButton;

	@FXML
	private static SplitPane mainSplitPane;

	//Moved
	@FXML
	private void calculateSchedule(ActionEvent event) {
		Scheduler sc = new Scheduler(streetMap, deliveriesRequest.getWarehouse(), deliveriesRequest.getDeliveryList(), "glouton");
		DeliverySchedule ds =  sc.findSchedule();
		for( Pair<Route,Delivery> p : ds) {
			colorRoute(p.getKey());
		}
	}

	//Moved
	@FXML
	private void computeAlgo (ActionEvent event){
		Dijkstra dj = new Dijkstra(streetMap);
		Route r = dj.performDijkstra(selectedDeliveries.get(0).getLocation(), selectedDeliveries.get(1).getLocation());
		colorRoute(r);
	}

	//Moved
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
	
	//Moved
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
		Intersection startingPoint = route.getStartingPoint();
		List<Street> streets =  route.getStreets();

		for( Street street : streets) {
			Intersection end = street.getEnd();
			canvasDrawer.drawStreet(startingPoint, end, Color.RED);
			startingPoint = end;

		}

	}

	public static void colorIntersection(DeliveriesRequest r) {
		deliveriesRequest = r;
		loadListView(r);
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		cv = (Canvas) ap.getChildren().get(0);
		ArrayList<Delivery> list = new ArrayList<Delivery>();
		list = r.getDeliveryList();
		for(Delivery d : list) {
			Intersection inter = d.getLocation();
			canvasDrawer.drawIntersection(inter, Color.RED, (double)10);

		}
		Intersection wh = r.getWarehouse();
		canvasDrawer.drawIntersection(wh, Color.LIMEGREEN, (double)10);
	}


	public static void loadListView(DeliveriesRequest dr) {
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		computeButton = (Button)scene.lookup("#computeButton");
		
		ObservableList<HBox> ol = FXCollections.observableArrayList();
		selectedDeliveries = new ArrayList<Delivery>();
		
		Label warehouse = new Label("Warehouse");
		warehouse.setId(String.valueOf(dr.getWarehouse().getId()));
		//ol.add(warehouse);

		int cpt = 1;
		for(Delivery d : dr.getDeliveryList()) {
			//Label l = WidgetBuilder.createDeliveryLabel(d, cpt);
			ol.add(WidgetBuilder.createListViewHBox(d, cpt));
			cpt++;
			//ol.add(l);
		}
		listview.getItems().clear();
		listview.setItems(ol);
		listview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
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
		);*/
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
		cv = new Canvas(750,750);

		canvasDrawer = new CanvasDrawer(MAX_X, MIN_X, MAX_Y, MIN_Y, cv);
		
		Double canvasWidth = cv.getWidth();
		GraphicsContext gc = cv.getGraphicsContext2D();
		Map<Long, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext() ) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			canvasDrawer.drawIntersection(intersection, Color.GREY,(double)1);
			
			List<Street> neighbors = intersection.getStreets();
			for(Street inter : neighbors) {
				canvasDrawer.drawStreet(intersection, inter.getEnd(), Color.GREY);
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
