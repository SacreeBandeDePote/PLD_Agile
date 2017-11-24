package lsbdp.agile.view;

import java.awt.GraphicsConfiguration;
import java.awt.font.GraphicAttribute;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.sun.deploy.Environment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.data.SerializeXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

public class MainWindowController{

	private static int MIN_X;
	private static int MIN_Y;
	private static int MAX_X;
	private static int MAX_Y;


	private static Scene scene;
	private static AnchorPane canvasAnchorPane;

	private static int getMaxY(StreetMap map) {
		int maxX = 0;
		Map<Double, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext()) {
			Double key = (Double) iterator.next();
			Intersection intersection = intersections.get(key);
			if(maxX < intersection.getY()) {
				maxX = intersection.getY();
			}
		}
		return maxX+5;
	}

	private static int getMinY(StreetMap map) {
		int minX = 100000000;
		Map<Double, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext()) {
			Double key = (Double) iterator.next();
			Intersection intersection = intersections.get(key);
			if(minX > intersection.getY()) {
				minX = intersection.getY();
			}
		}
		return minX+5;
	}

	private static int getMaxX(StreetMap map) {
		int maxX = 0;
		Map<Double, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext()) {
			Double key = (Double) iterator.next();
			Intersection intersection = intersections.get(key);
			if(maxX < intersection.getX()) {
				maxX = intersection.getX();
			}
		}
		return maxX+5;
	}

	private static int getMinX(StreetMap map) {
		int minX = 100000000;
		Map<Double, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext()) {
			Double key = (Double) iterator.next();
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
		System.out.println("fdffsd");
	}
	
	@FXML
	private void LoadMapActionHandler(ActionEvent event) throws InterruptedException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your map file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		MainWindow.openFileChooser(fileChooser);
	}

	@FXML
	private void LoadDeliveriesActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your deliverires file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		File f = MainWindow.openFileChooserDeliveries(fileChooser);

	}


	public static void LoadListView(DeliveriesRequest r) {
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		Canvas cv = (Canvas) ap.getChildren().get(0);
		GraphicsContext gc = cv.getGraphicsContext2D();
		ArrayList<Delivery> list = new ArrayList<Delivery>();
		list = r.getDeliveryList();
		for(Delivery d : list) {
			Intersection inter = d.getLocation();

			Double x = normalizeX((double) inter.getX(), cv.getWidth());
			Double y = normalizeY((double) inter.getY(), cv.getHeight());
			gc.setFill(Color.RED);
			gc.strokeOval(x, y, 5, 5);
			gc.fillOval(x, y, 5, 5);
		}
	}

	public static void initializer(Scene sc) {
		scene = sc;
		c = new Controller();
		//AnchorPane sp = (AnchorPane) sc.lookup("#canvasAnchorPane");
		//sp.setDividerPosition(0, 0.8);

		//sp.getItems().add(cv);
	}
  
	@FXML
	private void loadCanvas(MouseEvent event) {

		ObservableList<Intersection> l = FXCollections.observableArrayList();
		//l.addAll(new Intersection(0,0,0),new Intersection(0,15,52),new Intersection(0,7,69),new Intersection(0,74,4));
		/*
		Canvas cv = new Canvas(750,750);
		StreetMap map = new StreetMap();
		loadMap(map, cv);
		ap.getChildren().add(cv);*/
	}

	public static void loadMap(StreetMap map) throws InterruptedException {

		MAX_X = getMaxX(map);
		MIN_X = getMinX(map);
		MAX_Y = getMaxY(map);
		MIN_Y = getMinY(map);
		System.out.println(MAX_X + "+" + MIN_X);

		HBox ap = (HBox) scene.lookup("#canvasHBox");
		Canvas cv = new Canvas(750,750);

		Double canvasWidth = cv.getWidth();
		GraphicsContext gc = cv.getGraphicsContext2D();
		int count = 0;
		Map<Double, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext() ) {
			//TimeUnit.SECONDS.sleep(1);
			Double key = (Double) iterator.next();
			Intersection intersection = intersections.get(key);
			//if(intersection.getNeighboors().size() != 0) {
			Double startX = normalizeX((double) intersection.getX(), canvasWidth);
			Double startY = normalizeY((double) intersection.getY(), cv.getHeight());

			List<Street> neighbors = intersection.getStreets();

			gc.setFill(Color.BLUE);
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(1);
			gc.fillOval(startX, startY, 1, 1);
			gc.strokeOval(startX, startY, 1, 1);
			for(Street inter : neighbors) {
				Intersection end = inter.getEnd();
				Double endX = normalizeX((double) inter.getEnd().getX(), canvasWidth);
				Double endY = normalizeY((double) inter.getEnd().getY(), cv.getHeight()); //pute
				gc.setLineWidth(1);
				gc.strokeLine(startX, startY, endX, endY);

		for(Intersection intersection : map.values()) {
			if(intersection.getNeighbors().size() != 0) {
				Double startX = normalizeX((double) intersection.getX(), canvasWidth);
				Double startY = normalizeY((double) intersection.getY(), canvasWidth);
				List<Intersection> neighbors = intersection.getNeighbors();
	
				gc.setFill(Color.BLUE);
				gc.setStroke(Color.BLUE);
				gc.setLineWidth(1);
				gc.fillOval(startX, startY, 8, 8);
				gc.strokeOval(startX, startY, 8, 8);
				for(Intersection inter : neighbors) {
					Double endX = normalizeX((double) inter.getX(), canvasWidth);
					Double endY = normalizeY((double) inter.getY(), canvasWidth);
					gc.setLineWidth(3);
					gc.strokeLine(startX+4, startY+4, endX+4, endY+4);
				}
			}

			count++;
		}
		gc.strokeLine(0, 0, canvasWidth, 0);
		gc.strokeLine(0, 0, 0, canvasWidth);
		gc.strokeLine(canvasWidth, canvasWidth, canvasWidth, 0);
		gc.strokeLine(canvasWidth, canvasWidth, 0, canvasWidth);
		//colorRoads(cv, map);
		ap.getChildren().add(cv);
	}
	
	public static void loadDeliveryRequest(DeliveriesRequest dr) {
		ListView<Delivery> listView = (ListView)scene.lookup("#listView");
		ObservableList<Delivery> list = FXCollections.observableArrayList();
		for(Delivery d: dr.getDeliveryList()){
			list.add(d);
		}
		listView.setItems(list);
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedDeliveries = new ArrayList();
		listView.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
			selectedDeliveries.removeAll(selectedDeliveries);
			for(Delivery d: listView.getSelectionModel().getSelectedItems()){
				selectedDeliveries.add(d);
			}
        });
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
