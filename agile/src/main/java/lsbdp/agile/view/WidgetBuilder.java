package lsbdp.agile.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

public class WidgetBuilder {

	private static double sourceX = 0;
	private static double sourceY = 0;
	private static double anchorX = 0;
	private static double anchorY = 0;

	/**
	 * Creates the Label for the Delivery entry in the listView
	 * 
	 * @param delivery
	 * @param count
	 * @return
	 */
	public static Label createDeliveryLabel(Delivery delivery, int count) {

		String timespanStart = delivery.getTimespanStart().getHours() + ":" + delivery.getTimespanStart().getMinutes();
		String timespanEnd = delivery.getTimespanEnd().getHours() + ":" + delivery.getTimespanEnd().getMinutes();
		Label label = new Label("Livraison #"+count + ", from " + timespanStart + " to " + timespanEnd);	

		label.setId("Delivery-"+String.valueOf(delivery.getLocation().getId()));

		return label;
	}

	/**
	 * Create the circle in the center of the time dougnnut
	 * 
	 * @return
	 */
	public static Circle createFakeHole() {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;
		Circle circle = new Circle(200);
		circle.setCenterX(centerX);
		circle.setCenterY(centerY);
		Color color = Color.rgb(244, 244, 244);
		circle.setFill(color);


		return circle;
	}

	/**
	 * Create the arc corresponding to a Travel, also set the event handlers
	 * 
	 * @param start
	 * @param duration
	 * @return
	 * 
	 * @see highlightArc
	 * @see unhighlightArc
	 */
	public static Arc createArcTravel(double start, double duration) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;

		Arc arc = new Arc();

		arc.setCenterX(centerX);
		arc.setCenterY(centerY);
		arc.setRadiusX(250f);
		arc.setRadiusY(250f);
		arc.setStartAngle(start);
		arc.setLength(duration);
		arc.setType(ArcType.ROUND);
		arc.setFill(new Color(0, 0.8, 0, 1));

		arc.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				EventHandlers.highlightArc(arc);
			}
		});

		arc.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.unhighlightArc(arc);
			}
		});

		return arc;
	}

	/**
	 * Create the JavaFx VBox containing the legend of the time doughnut
	 * 
	 * @param overlay
	 * @return
	 */
	public static VBox createLegend(Pane overlay) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()-50;

		Label captionLabel = new Label("Caption");
		Label freeTimeLabel = new Label("Free Time");
		Rectangle ftRect = new Rectangle(25, 10);
		ftRect.setFill(Color.LIGHTBLUE);
		freeTimeLabel.setGraphic(ftRect);

		Label travelLabel = new Label("Travel time");
		Rectangle tRect = new Rectangle(25, 10);
		tRect.setFill(Color.LIGHTGREEN);
		travelLabel.setGraphic(tRect);

		Label deliveryLabel = new Label("Delivery time");
		Rectangle dRect = new Rectangle(25, 10);
		dRect.setFill(Color.RED);
		deliveryLabel.setGraphic(dRect);

		VBox vbox = new VBox(captionLabel, freeTimeLabel, travelLabel, deliveryLabel);
		vbox.setId("CaptionBox");
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefHeight(50);
		vbox.setPrefWidth(300);
		vbox.setLayoutX(centerX-150);
		vbox.setLayoutY(centerY-100);
		vbox.setStyle("-fx-background-color : F0F0F0;"
				+ "-fx-border-radius : 15;"
				+ "-fx-border-color : silver;"
				+ "-fx-background-radius : 15;");
		return vbox;
	}

	/**
	 * Create the JavaFx VBox containing the wharehouse entry of the listview
	 * 
	 * @param warehouse
	 * @return
	 * 
	 * @see highlightWarehouse
	 * @see unhighlightWarehouse
	 */
	public static HBox createListViewHBoxWarehouse(Intersection warehouse) {
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER_LEFT);

		Label label = new Label("Warehouse");
		label.setId("Warehouse-"+warehouse.getId());
		hbox.getChildren().add(label);

		hbox.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				EventHandlers.highlightWarehouse(warehouse);
			}
		});
		hbox.setOnMouseClicked(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.highlightWarehouse(warehouse);
			}
		});
		hbox.setOnMouseExited(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.unhighlightWarehouse(warehouse);
			}
		});

		return hbox;
	}

	/**
	 *	Creates the JavaFx Arc corresponding to a Delivery 
	 *
	 * @param overlay
	 * @param delivery
	 * @param start
	 * @param duration
	 * @return
	 * 
	 * @see highlightDeliveryListView
	 * @see showArcInformations
	 * @see highlightArc
	 * @see hideArcInformations
	 */
	public static Arc createArcDelivery(Pane overlay, Delivery delivery, double start, double duration) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;
		Arc arc = new Arc();
		arc.setCenterX(centerX);
		arc.setCenterY(centerY);
		arc.setRadiusX(250f);
		arc.setRadiusY(250f);
		arc.setStartAngle(start);
		arc.setLength(duration);
		arc.setType(ArcType.ROUND);
		arc.setFill(Color.RED);

		arc.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
				EventHandlers.showArcInformations(overlay, delivery);
				EventHandlers.highlightArc(arc);
			}
		});

		arc.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
				EventHandlers.hideArcInformations(overlay);
				EventHandlers.unhighlightArc(arc);
			}
		});

		arc.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			}
		});

		return arc;
	}

	/**
	 * Creates the JavaFx Arc corresponding to a free time window
	 * 
	 * @param angle
	 * @param duration
	 * @param start
	 * @param end
	 * @return
	 * 
	 * @see openAddPopUp
	 */
	public static Arc createArcFreeTime(double angle, double duration, Date start, Date end) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;

		Tooltip tooltip = new Tooltip("Click to add a delivery");
		
		Arc arc = new Arc();
		Tooltip.install(arc, tooltip);
		arc.setCenterX(centerX);
		arc.setCenterY(centerY);
		arc.setRadiusX(250f);
		arc.setRadiusY(250f);
		arc.setStartAngle(angle);
		arc.setLength(duration);
		arc.setType(ArcType.ROUND);
		arc.setFill(Color.LIGHTBLUE);

		arc.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				WindowManager.getScene().setCursor(Cursor.HAND);
				EventHandlers.highlightArc(arc);
			}

		});
		arc.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				WindowManager.getScene().setCursor(Cursor.DEFAULT);
				EventHandlers.unhighlightArc(arc);
			}

		});

		arc.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MainWindow.openAddPopUp(start, end);
			}
		});
		return arc;
	}


	/**
	 * Creates the JavaFx Listview delete Button correponding to a delivery
	 * 
	 * @param delivery
	 * @return
	 * 
	 * @see deleteDelivery
	 */
	public static Button createListViewDeleteButton(Delivery delivery) {
		Button btn = new Button();
		btn.setText("X");
		btn.setStyle("-fx-background-radius : 40");
		btn.setMaxHeight(4);
		btn.setMaxWidth(4);


		btn.setOnMouseClicked(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.deleteDelivery(delivery);		
			}                            // disable mouse events for all children

		});

		return btn;
	}

	/**
	 * create the JavaFx modify button corresponding to a delivery
	 * 
	 * @param delivery
	 * @return
	 * 
	 * @see openModifyPopUp
	 */
	private static Button createListViewModifyButton(Delivery delivery) {
		ImageView iv = new ImageView(new Image(WidgetBuilder.class.getResourceAsStream("/gearIcon.png")));
		iv.setFitWidth(15);
		iv.setFitHeight(15);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
		Button btn = new Button();
		btn.setStyle("-fx-background-radius : 40");
		btn.setGraphic(iv);
		
		btn.setOnMouseClicked(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.openModifyPopUp(delivery);		
				}                            // disable mouse events for all children

		});
		
		return btn;
	}

	/**
	 * create a JavaFx HBox for the entry of the listview
	 * 
	 * @param delivery
	 * @param count
	 * @return
	 * 
	 * @see highlightIntersection
	 * @see unhighlightIntersection
	 */
	public static HBox createListViewHBox(Delivery delivery, int count) {
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER_LEFT	);

		Label label = createDeliveryLabel(delivery, count);

		Button deleteBtn = WidgetBuilder.createListViewDeleteButton(delivery);
		Button modifyBtn = WidgetBuilder.createListViewModifyButton(delivery);
		hbox.getChildren().addAll(deleteBtn, modifyBtn, label);

		hbox.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				EventHandlers.highlightIntersection(delivery.getLocation());
			}
		});

		hbox.setOnMouseClicked(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.highlightIntersection(delivery.getLocation());

			}
		});

		hbox.setOnMouseExited(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.unhighlightIntersection(delivery.getLocation());
			}
		});

		return hbox;
	}


	/**
	 * Creates the JavaFx VBox containing informations of the selected Delivery
	 * 
	 * @param overlay
	 * @param delivery
	 * @return
	 */
	public static VBox createVBoxDelvieryInformation(Pane overlay, Delivery delivery) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");


		Label idLabel = new Label("Delivery on intersection " + delivery.getLocation().getId());
		Label durationLabel = new Label("Duration : " + delivery.getDuration()/60 + " min");
		Label timeStartWindowLabel = new Label("Start of time window : " + sdf.format(delivery.getTimespanStart()));
		Label timeEndWindowLabel = new Label("End of time window : " + sdf.format(delivery.getTimespanEnd()));
		VBox vbox = new VBox(idLabel, durationLabel, timeStartWindowLabel, timeEndWindowLabel);
		vbox.setId("InformationBox");
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefHeight(50);
		vbox.setPrefWidth(300);
		vbox.setLayoutX(centerX-150);
		vbox.setLayoutY(centerY-100);
		vbox.setStyle("-fx-background-color : F0F0F0;"
				+ "-fx-border-radius : 15;"
				+ "-fx-border-color : silver;"
				+ "-fx-background-radius : 15;");
		return vbox;
	}

	/**
	 * Creates the JavaFx Circle in the overlay corresponding to a delivery
	 * 
	 * @param delivery
	 * @param color
	 * @param radius
	 * @return
	 */
	public static Circle createDeliveryCircle(Delivery delivery, Color color,Double radius) {
		Circle circle = new Circle(radius);

		circle.setStroke(color);
		circle.setFill(color);
		circle.setId("Circle"+delivery.getLocation().getId());

		Tooltip tooltip = new Tooltip("Delivery on Intersection number : " + delivery.getLocation().getId()
				+ "\nDuration : " + delivery.getDuration());
		tooltip.setAutoHide(false);
		Tooltip.install(circle, tooltip);
		circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
				EventHandlers.highlightIntersection(delivery.getLocation());
			}	
		});

		circle.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.unhighlightIntersection(delivery.getLocation());
				EventHandlers.unhighlightDeliveryListView(delivery);
			}	
		});

		return circle;
	}

	/**
	 * Creates the JavaFx Circle corresponding to an intersection which allows to add a delivery
	 * 
	 * @param intersection
	 * @param color
	 * @param radius
	 * @return
	 * 
	 * @see highlightTemporaryIntersection
	 * @see unhighlightTemporaryIntersection
	 * @see temporaryIntersectionClicked
	 */
	public static Circle createTemporaryIntersectionCircle(Intersection intersection, Color color, Double radius) {
		Circle circle = new Circle(radius);

		circle.setStroke(color);
		circle.setFill(color);
		circle.setId("Tmp"+intersection.getId());

		circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightTemporaryIntersection(intersection);
			}
		});

		circle.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.unhighlightTemporaryIntersection(intersection);
			}
		});

		circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.temporaryIntersectionClicked(intersection);
			}
		});

		return circle;
	}

	/**
	 * creates the JavaFx Circle corresponding to the warehouse in the overlay
	 * 
	 * @param warehouse
	 * @param color
	 * @param radius
	 * @return
	 * 
	 * @see highlightWarehouseListView
	 * @see highlightWarehouse
	 * @see unhighlightWarehouseListView
	 * @see unhighlightWarehouse
	 */
	public static Circle createWarehouseCircle(Intersection warehouse, Color color,Double radius) {
		Circle circle = new Circle(radius);

		circle.setStroke(color);
		circle.setFill(color);
		circle.setId("CircleWarehouse"+warehouse.getId());

		circle.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightWarehouseListView();
				EventHandlers.highlightWarehouse(warehouse);
			}	
		});

		circle.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.unhighlightWarehouseListView();
				EventHandlers.unhighlightWarehouse(warehouse);
			}	
		});

		return circle;
	}

	/**
	 * creates the JavaFx Group containing the map and the overlay
	 * 
	 * @param canvas
	 * @param overlay
	 * @return
	 * 
	 * @see zoom
	 */
	public static Group createDrawGroup(Canvas canvas, Pane overlay) {
		Group g = new Group(canvas, overlay);
		g.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent e) {
				EventHandlers.zoom(g,e);
			}

		});

		g.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				sourceX = g.getTranslateX();
				sourceY = g.getTranslateY();
				anchorX = e.getX();
				anchorY = e.getY();
			}
		});

		g.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				// the getScale multiplicator is to adjust the translation to the level of zoom
				double deltaX = (g.getScaleX()/2) * (sourceX + e.getX() - anchorX);
				double deltaY = (g.getScaleX()/2) * (sourceY + e.getY() - anchorY);				


				g.setTranslateX(deltaX);
				g.setTranslateY(deltaY);
			}
		});

		return g;
	}




}
