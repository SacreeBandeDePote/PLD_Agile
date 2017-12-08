package lsbdp.agile.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventHandlers {

	@FXML
	private void quitAdditionHandler(ActionEvent event) {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			Controller.refreshIHM();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}
	}
	
	public static void quitAdditionHandler() {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			Controller.refreshIHM();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}
	}


	@FXML
	private void addDelivery(ActionEvent event) {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			Controller.cmdAdd();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}
	}

	@FXML
	private void saveDeliveries(ActionEvent event) {
		if(WindowManager.deliveriesLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save your deliveries");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("XML File", "*.xml")
					);
			File f = MainWindow.openFileChooserRoadmap(fileChooser);
			if (f != null)
				Controller.saveDeliveries(f);
		} else {
			MainWindow.openMessagePopup("Please load a delivery file");
		}	
	}

	@FXML
	private void generateRoadmapActionHandler(ActionEvent event) throws InterruptedException, ParseException, IOException {
		if(WindowManager.deliveriesLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save your roadMap");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("txt File", "*.txt")
					);
			File f = MainWindow.openFileChooserRoadmap(fileChooser);
			if (f != null)
				Controller.generateRoadmapActionHandler(f);
		} else {
			MainWindow.openMessagePopup("Please load a delivery file");
		}	
	}
	
	public static void generateRoadmapActionHandler() throws InterruptedException, ParseException, IOException {
		if(WindowManager.deliveriesLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save your roadMap");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("txt File", "*.txt")
					);
			File f = MainWindow.openFileChooserRoadmap(fileChooser);
			Controller.generateRoadmapActionHandler(f);
		} else {
			MainWindow.openMessagePopup("Please load a delivery file");
		}
	}

	@FXML
	private void UndoAction(ActionEvent event) {
		Controller.undo();
	}

	@FXML
	private void RedoAction(ActionEvent event) {
		Controller.redo();
	}

	@FXML
	private void LoadDeliveriesActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		if(WindowManager.mapLoaded ) {
			if(!WindowManager.deliveriesLoaded) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose your deliveries file");
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("XML File", "*.xml")
						);
				File f = MainWindow.openFileChooserDeliveries(fileChooser);
				if (f != null) {
					Controller.loadDeliveryRequest(f);
					WindowManager.deliveriesLoaded = true;
				}
			} else {
				MainWindow.openMessagePopup("A delivery file is already loaded, please reload a map");
			}
		} else {
			MainWindow.openMessagePopup("Please load a map");
		}
	}

	@FXML
	private void switchViewHandler(ActionEvent event) {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			StackPane stackPane = (StackPane) WindowManager.getScene().lookup("#mainStackPane");
			Node back = (Node) stackPane.getChildren().get(0);
			back.toFront();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}

	}
	
	@FXML
	private void resetView(ActionEvent event) {
		StackPane stackPane = (StackPane) WindowManager.getScene().lookup("#mainStackPane");
		Group drawGroup = null;
		for (Node node : stackPane.getChildren()) {
			if (node.getClass() == Group.class)  drawGroup = (Group) node;
		}
		drawGroup.setTranslateX(0d);
		drawGroup.setTranslateY(0d);
		drawGroup.setScaleX(1d);
		drawGroup.setScaleY(1d);

	}
	
	public static void switchViewHandler() {
		StackPane stackPane = (StackPane) WindowManager.getScene().lookup("#mainStackPane");
		Node back = (Node) stackPane.getChildren().get(0);
		back.toFront();
	}

	@FXML
	private void LoadMapActionHandler(ActionEvent event) throws InterruptedException, ParseException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose your map file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML File", "*.xml")
				);
		File f = MainWindow.openFileChooser(fileChooser);
		if (f != null) {
			Controller.loadMap(f);
			WindowManager.mapLoaded = true;
			WindowManager.deliveriesLoaded = false;
		}
	}

	@FXML
	private void calculateSchedule(ActionEvent event) {
		// DeliverySchedule schedule = Controller.findDeliverySchedule();
	}

	public static void highlightIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLUE);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);

		circle.setEffect(dropShadow);
		circle.setFill(Color.BLUE);
		circle.setStroke(Color.BLUE);
		circle.setStrokeWidth(8d);
	}

	public static void highlightWarehouse(Intersection warehouse) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#CircleWarehouse"+warehouse.getId());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.GREEN);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);

		circle.setEffect(dropShadow);
		circle.setFill(Color.GREEN);
		circle.setStroke(Color.GREEN);
		circle.setStrokeWidth(8d);
	}

	public static void unhighlightIntersection(Intersection intersection) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle"+intersection.getId());

		circle.setEffect(null);
		circle.setFill(Color.RED);
		circle.setStroke(Color.RED);
		circle.setStrokeWidth(1d);	
	}

	public static void unhighlightWarehouse(Intersection warehouse) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#CircleWarehouse"+warehouse.getId());

		circle.setEffect(null);
		circle.setFill(Color.GREEN);
		circle.setStroke(Color.GREEN);
		circle.setStrokeWidth(1d);	
	}

	public static void highlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = "Delivery-"+delivery.getLocation().getId();
		for(HBox hbox : list) {
			if (hbox.getChildren().size() > 1) {
				Label l = (Label) hbox.getChildren().get(2);
				if(l.getId().compareTo(id) == 0) {
					//hbox.setStyle("-fx-background-color : d21919");
					listview.getSelectionModel().select(hbox);
				}
			}
		}
	}

	public static void highlightWarehouseListView() {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		listview.getSelectionModel().select(0);
	}

	public static void unhighlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = ""+delivery.getLocation().getId();
		for(HBox hbox : list) {
			if (hbox.getChildren().size() > 1) {
				Label l = (Label) hbox.getChildren().get(2);
				String labelId = l.getId().substring(l.getId().lastIndexOf("-")+1);
				if(labelId.compareTo(id) == 0) {
					//hbox.setStyle("-fx-background-color : transparent");
					listview.getSelectionModel().clearSelection();
				}
			}
		}
	}

	public static void showArcInformations(Pane overlay, Delivery delivery) {
		VBox vbox = WidgetBuilder.createVBoxDelvieryInformation(overlay, delivery);
		overlay.getChildren().add(vbox);
	}

	public static void hideArcInformations(Pane overlay) {
		overlay.getChildren().remove(overlay.getChildren().size()-1);
	}

	public static void highlightArc(Arc arc) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusXProperty(), arc.getRadiusX())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusXProperty(), 275)),
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusYProperty(), arc.getRadiusY()	)),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusYProperty(), 275))
				);
		timeline.setAutoReverse(false);
		timeline.play();
	}

	public static void unhighlightArc(Arc arc) {
		Timeline timeline = new Timeline();

		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusXProperty(), arc.getRadiusX())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusXProperty(), 250)),
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusYProperty(), arc.getRadiusY())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusYProperty(), 250))
				);
		timeline.setAutoReverse(false);
		timeline.play();
	}

	public static void unhighlightWarehouseListView () {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		listview.getSelectionModel().clearSelection();
	}

	public static void highlightTemporaryIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Tmp"+intersection.getId());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLUE);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);

		circle.setEffect(dropShadow);
		circle.setStrokeWidth(6d);
	}

	public static void unhighlightTemporaryIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Tmp"+intersection.getId());

		circle.setEffect(null);
		circle.setStrokeWidth(2d);
	}

	public static void deleteDelivery(Delivery delivery) {
		Controller.cmdDelete(delivery);
	}


	public static void temporaryIntersectionClicked(Intersection intersection) {
		MainWindow.openAddPopUp(intersection);
	}

	public static void addDelivery(Intersection intersection, String duration, String startingTime, String endTime) {
		int tmpDuration = Integer.parseInt(duration);
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date start = null;
		Date end = null;
		try {
			start = sdf.parse(startingTime);
			end = sdf.parse(endTime);
		} catch (ParseException e) {
			start = null;
			end = null;
		}
		Delivery d = new Delivery(tmpDuration, start, end, intersection, null);
		Controller.cmdAdd2(d);
	}

	public static void zoom(Group g, ScrollEvent e) {
		double zoomIntensity = 0.01;
		double scrollDelta = e.getDeltaY();

		double newScaleX = g.getScaleX() + zoomIntensity*scrollDelta;
		double newScaleY = g.getScaleY() + zoomIntensity*scrollDelta;

		if (newScaleX <= 1 && newScaleY <= 1) {
			newScaleX = 1d;
			newScaleY = 1d;
		}
			g.setScaleX(newScaleX);
			g.setScaleY(newScaleY);
		
	}

	public static void openModifyPopUp(Delivery delivery) {
		String duration = Integer.toString(delivery.getDuration());
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String start = "HH:mm:ss";
		String end = "HH:mm:ss";
		try {
			if (delivery.getTimespanStart() != null) {
				start = sdf.format(delivery.getTimespanStart());
			} 
			if (delivery.getTimespanEnd() != null) {
				end = sdf.format(delivery.getTimespanEnd());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Popup pop = PopupWindowManager.createModifyPopup(delivery, duration, start, end);
		MainWindow.openModifyPopUp(pop);
	}

	public static void modifyDelivery(Delivery delivery, String duration, String start, String end) throws ParseException {
		int d = Integer.parseInt(duration);
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
			if (start.compareTo("HH:mm:ss") != 0) startDate = sdf.parse(start);
			if (end.compareTo("HH:mm:ss") != 0) endDate = sdf.parse(end);
		
		Controller.cmdModify(delivery, startDate, endDate, d);

	}


}
