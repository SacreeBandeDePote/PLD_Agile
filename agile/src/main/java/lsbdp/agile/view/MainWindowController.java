package lsbdp.agile.view;

import java.awt.GraphicsConfiguration;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	private static int MIN_X = 12000;
	private static int MIN_Y = 19000;
	private static int MAX_X = 27000;
	private static int MAX_Y = 38000;
	
	private static Scene scene;
	private static AnchorPane canvasAnchorPane;
	
	private static Controller c;
	
	private static StreetMap m;
	private static DeliveriesRequest deliveriesRequest;
	private static List<Delivery> selectedDeliveries;
	private static Route computedRoute;
	
	@FXML
	private static SplitPane mainSplitPane;

	@FXML
	private void LoadMapActionHandler(ActionEvent event) throws ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your map file");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("XML File", "*.xml")
		);
		File f = MainWindow.openFileChooser(fileChooser);
		m = c.addMap(f);
		loadMap(m);
	}

	@FXML
	private void LoadDeliveriesActionHandler(ActionEvent event) throws ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your deliverires file");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("XML File", "*.xml")
		);
		File f = MainWindow.openFileChooser(fileChooser);
		deliveriesRequest = c.addDeliveriesRequest(f);
		loadDeliveryRequest(deliveriesRequest);
	}
	
	@FXML
	private void computeAlgo (ActionEvent event){
		if(selectedDeliveries.size() == 2) {
			computedRoute = c.calculateRoute(selectedDeliveries.get(0), selectedDeliveries.get(1), m);
			for(Street s : computedRoute.getStreets()) {
				System.out.println(s.getName());
			}
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

	
	public Circle createCircle() {
		Circle c = new Circle();
		c.setRadius(40);
		c.setFill(Color.BLUE);
		
		Circle c2 = new Circle();
		c2.setRadius(20);
		c2.setFill(Color.RED);
		
		Tooltip tt = new Tooltip();
		tt.setGraphic(c2);
		tt.install(c, tt);
		return c;
	}
	

	public static void loadMap(StreetMap map) {
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		Canvas cv = new Canvas(850,850);
		
		Double canvasWidth = cv.getWidth();
		GraphicsContext gc = cv.getGraphicsContext2D();

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
		}
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
		Double newY = (y-MIN_Y)/(MAX_Y-MIN_X);
		newY *= height;
		return newY;
	}
}
