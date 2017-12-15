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

	/**
	 * FXML event handler that allows to reverse the intersection selection process
	 * 
	 * @param event
	 */
	@FXML
	private void quitAdditionHandler(ActionEvent event) {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			Controller.refreshIHM();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}
	}

	/**
	 * Event handler that allows to reverse the intersection selection process
	 * 
	 */
	public static void quitAdditionHandler() {
		if(WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			Controller.refreshIHM();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}
	}

/**
 * FXML event handler that allows to add a delivery
 * 
 * @param event
 */
	@FXML
	private void addDelivery(ActionEvent event) {
		if (WindowManager.mapLoaded && WindowManager.deliveriesLoaded) {
			Controller.cmdAdd();
		} else {
			MainWindow.openMessagePopup("Please load a map and a delivery file");
		}
	}

	/**
	 * FXML event handler that allows to save a delivery
	 * 
	 * @param event
	 */
	@FXML
	private void saveDeliveries(ActionEvent event) {
		if (WindowManager.deliveriesLoaded) {
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

	/**
	 * Event handler that allows to save a delivery (keyboard shortcut)
	 */
	public static void saveDeliveries() {
		if(WindowManager.deliveriesLoaded) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save your deliveries");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("XML File", "*.xml")
					);
			File f = MainWindow.openFileChooserRoadmap(fileChooser);
			Controller.saveDeliveries(f);
		} else {
			MainWindow.openMessagePopup("Please load a delivery file");
		}	
	}
	
	/**
	 * FXML event handler that allows to generate the roadmap
	 *  
	 * @param event
	 * @throws InterruptedException
	 * @throws ParseException
	 * @throws IOException
	 */
	@FXML
	private void generateRoadmapActionHandler(ActionEvent event) throws InterruptedException, ParseException, IOException {
		if (WindowManager.deliveriesLoaded) {
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

	/**
	 * Event handler that allows to generate the roadmap (keyboard shortcut)
	 * 
	 * @throws InterruptedException
	 * @throws ParseException
	 * @throws IOException
	 */
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

	/**
	 * FXML event handler for the Undo
	 * 
	 * @param event
	 */
	@FXML
	private void UndoAction(ActionEvent event) {
		Controller.undo();
	}

	/**
	 * FXML event handler for the redo
	 * 
	 * @param event
	 */
	@FXML
	private void RedoAction(ActionEvent event) {
		Controller.redo();
	}

	/**
	 * FXML event handler that allows to load to load a delivery list
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws ParseException
	 */
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

	/**
	 * FXML event handler that allows to switch between the map and the time dougnnut 
	 * 
	 * @param event
	 */
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
	
	/**
	 * FXML event handler that allows to reset the map to it's original position
	 * 
	 * @param event
	 */
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

	/**
	 * Event handler that allows to switch between the map and the time dougnnut 
	 */
	public static void switchViewHandler() {
		StackPane stackPane = (StackPane) WindowManager.getScene().lookup("#mainStackPane");
		Node back = (Node) stackPane.getChildren().get(0);
		back.toFront();
	}
	
	/**
	 * FXML event handler that allows to load a map
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws ParseException
	 */
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

	/**
	 * Event handler that highlights an intersection on the map by increasing it's size
	 * 
	 * @param intersection
	 */
	public static void highlightIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle" + intersection.getId());
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLUE);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);

		circle.setEffect(dropShadow);
		circle.setFill(Color.BLUE);
		circle.setStroke(Color.BLUE);
		circle.setStrokeWidth(8d);
	}

	/**
	 * Event handler that highlights the wharehouse dot
	 * 
	 * @param warehouse
	 */
	public static void highlightWarehouse(Intersection warehouse) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#CircleWarehouse" + warehouse.getId());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.GREEN);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);

		circle.setEffect(dropShadow);
		circle.setFill(Color.GREEN);
		circle.setStroke(Color.GREEN);
		circle.setStrokeWidth(8d);
	}

	/**
	 * Event handler that return an intersection dot to it's original state
	 * 
	 * @param intersection
	 */
	public static void unhighlightIntersection(Intersection intersection) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Circle" + intersection.getId());

		circle.setEffect(null);
		circle.setFill(Color.RED);
		circle.setStroke(Color.RED);
		circle.setStrokeWidth(1d);
	}

	/**
	 * Event handler that return a wharehouse dot to it's original state
	 * 
	 * @param warehouse
	 */
	public static void unhighlightWarehouse(Intersection warehouse) {
		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#CircleWarehouse" + warehouse.getId());

		circle.setEffect(null);
		circle.setFill(Color.GREEN);
		circle.setStroke(Color.GREEN);
		circle.setStrokeWidth(1d);
	}

	/**
	 * For a given Delivery highlight the entry in the list view
	 * 
	 * @param delivery
	 */
	public static void highlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = "Delivery-" + delivery.getLocation().getId();
		for (HBox hbox : list) {
			if (hbox.getChildren().size() > 1) {
				Label l = (Label) hbox.getChildren().get(2);
				if (l.getId().compareTo(id) == 0) {
					//hbox.setStyle("-fx-background-color : d21919");
					listview.getSelectionModel().select(hbox);
				}
			}
		}
	}

	/**
	 * Highlight the wharehouse entry list
	 */
	public static void highlightWarehouseListView() {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		listview.getSelectionModel().select(0);
	}

	/**
	 * For a given Delivery unhighlight the entry in the list view
	 * 
	 * @param delivery
	 */
	public static void unhighlightDeliveryListView(Delivery delivery) {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		ObservableList<HBox> list = listview.getItems();
		String id = "" + delivery.getLocation().getId();
		for (HBox hbox : list) {
			if (hbox.getChildren().size() > 1) {
				Label l = (Label) hbox.getChildren().get(2);
				String labelId = l.getId().substring(l.getId().lastIndexOf("-") + 1);
				if (labelId.compareTo(id) == 0) {
					//hbox.setStyle("-fx-background-color : transparent");
					listview.getSelectionModel().clearSelection();
				}
			}
		}
	}

	/**
	 * Display the information of the corresponding delivery in the time doughnut
	 * 
	 * @param overlay
	 * @param delivery
	 * 
	 * @see createVBoxDeliveryInformation
	 */
	public static void showArcInformations(Pane overlay, Delivery delivery) {
		VBox vbox = WidgetBuilder.createVBoxDelvieryInformation(overlay, delivery);
		overlay.getChildren().add(vbox);
	}

	/**
	 * Hide the information in the time doughnut
	 * 
	 * @param overlay
	 */
	public static void hideArcInformations(Pane overlay) {
		overlay.getChildren().remove(overlay.getChildren().size() - 1);
	}

	/**
	 * Highlight an arc by animating it
	 * 
	 * @param arc
	 */
	public static void highlightArc(Arc arc) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusXProperty(), arc.getRadiusX())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusXProperty(), 275)),
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusYProperty(), arc.getRadiusY())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusYProperty(), 275))
		);
		timeline.setAutoReverse(false);
		timeline.play();
	}

	/**
	 * Unighlight an arc by animating it
	 * 
	 * @param arc
	 */
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

	/**
	 * Unhighlight the wharehouse entry list
	 */
	public static void unhighlightWarehouseListView() {
		Scene scene = WindowManager.getScene();
		ListView<HBox> listview = (ListView<HBox>) scene.lookup("#listView");
		listview.getSelectionModel().clearSelection();
	}

	/**
	 * Highlight the temporary intersection dot
	 * 
	 * @param intersection
	 */
	public static void highlightTemporaryIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Tmp" + intersection.getId());

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLUE);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(0);

		circle.setEffect(dropShadow);
		circle.setStrokeWidth(6d);
	}

	/**
	 * Unhighlight the temporary intersection dot
	 * 
	 * @param intersection
	 */
	public static void unhighlightTemporaryIntersection(Intersection intersection) {

		Scene scene = WindowManager.getScene();
		Circle circle = (Circle) scene.lookup("#Tmp" + intersection.getId());

		circle.setEffect(null);
		circle.setStrokeWidth(2d);
	}

	/**
	 * Event handler that allows to delete a delivery
	 * 
	 * @param delivery
	 */
	public static void deleteDelivery(Delivery delivery) {
		Controller.cmdDelete(delivery);
	}

	/**
	 * Event Handler for the click on a temporary intersection
	 * 
	 * @param intersection
	 * 
	 * @see openAddPopUp
	 */
	public static void temporaryIntersectionClicked(Intersection intersection) {
		MainWindow.openAddPopUp(intersection);
	}

	/**
	 * Event handler that checks the input in the add popup field and add the delivery
	 * 
	 * @param intersection
	 * @param duration
	 * @param startingTime
	 * @param endTime
	 */
	public static void addDelivery(Intersection intersection, String duration, String startingTime, String endTime) {
		boolean error = false;
		int tmpDuration = Integer.parseInt(duration);
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date start = null;
		Date end = null;
		try {
			start = sdf.parse(startingTime);
		} catch (ParseException e) {
			start = null;
		}
		try {
			end = sdf.parse(endTime);
		} catch (ParseException e) {
			end = null;
		}

		if (tmpDuration < 0) {
			error = true;
			WindowManager.openErrorPopUp("Please enter a positive duration time");
		} else if (start == null && end != null || start != null && end == null) {
			error = true;
			WindowManager.openErrorPopUp("Please, specify none time or all times");
		} else if (start != null && end != null) {
			Date limit = null;
			try {
				limit = sdf.parse("18:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (start.after(end)) {
				error = true;
				WindowManager.openErrorPopUp("End time is before start time");
			} else if (start.after(limit)) {
				error = true;
				WindowManager.openErrorPopUp("Our services end at 6pm");
			}
		}
		if (!error) {
			Delivery d = new Delivery(tmpDuration, start, end, intersection, null);
			Controller.cmdAdd2(d);
		}
	}

	/**
	 * Event handler for the scroll on the map and zooms
	 * 
	 * @param g
	 * @param e
	 */
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

	/**
	 * Event Handler that allows to open the window to modify a delivery
	 * 
	 * @param delivery
	 * 
	 * @see openModifyPopup
	 */
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

	/**
	 * Event handler that checks and send the data to modify a Delivery
	 * 
	 * @param delivery
	 * @param duration
	 * @param start
	 * @param end
	 * @throws ParseException
	 */
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
