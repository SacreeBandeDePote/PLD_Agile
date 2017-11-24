package lsbdp.agile.view;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.data.SerializeXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

public class MainWindowController{

	private static int MIN_X;
	private static int MIN_Y;
	private static int MAX_X;
	private static int MAX_Y;

	private static ArrayList<Delivery> selectedDeliveries;
	
	private static Scene scene;
	private static int getMaxY(StreetMap map) {
		int maxX = 0;
		Map<Long, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
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
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
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
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
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
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
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
		System.out.println("fdffsd");
	}

	@FXML
    private void computeAlgo (ActionEvent event){
        /*if(selectedDeliveries.size() == 2) {
            computedRoute = c.calculateRoute(selectedDeliveries.get(0), selectedDeliveries.get(1), m);
            for(Street s : computedRoute.getStreets()) {
                System.out.println(s.getName());
            }
        }*/
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
			
			StreetMap map = s.serializeMapXML(f);
			System.out.println(map.toString());
			loadMap(map);
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
		MainWindow.openFileChooserDeliveries(fileChooser);

	}


	public static void LoadListView(DeliveriesRequest dr) {
		ListView<Label> listview = (ListView<Label>) scene.lookup("#listView");
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
			System.out.println(selectedDeliveries.size());
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
		System.out.println(MAX_X + "+" + MIN_X);
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		Canvas cv = new Canvas(750,750);

		Double canvasWidth = cv.getWidth();
		GraphicsContext gc = cv.getGraphicsContext2D();
		Map<Long, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext() ) {
			//TimeUnit.SECONDS.sleep(1);
			Long key = (Long) iterator.next();
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
		//colorRoads(cv, map);
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
