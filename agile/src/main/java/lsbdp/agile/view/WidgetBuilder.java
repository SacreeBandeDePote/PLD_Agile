package lsbdp.agile.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
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
import lsbdp.agile.controller.Controller;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

public class WidgetBuilder {

	private static double sourceX = 0;
	private static double sourceY = 0;
	private static double anchorX = 0;
	private static double anchorY = 0;

	public static Label createDeliveryLabel(Delivery delivery, int count) {

		Label label = new Label("Livraison #"+count);	

		label.setId("Delivery-"+String.valueOf(delivery.getLocation().getId()));

		return label;
	}

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
		arc.setFill(Color.LIGHTGREEN);

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

	public static Arc createArcFreeTime(double angle, double duration, Date start, Date end) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;

		Arc arc = new Arc();

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
				EventHandlers.highlightArc(arc);
			}

		});
		arc.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
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


	public static VBox createVBoxDelvieryInformation(Pane overlay, Delivery delivery) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");


		Label idLabel = new Label("Delivery on intersection " + 12457);
		Label durationLabel = new Label("Duration : " + delivery.getDuration());
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

	public static Circle createDeliveryCircle(Delivery delivery, Color color,Double radius) {
		Circle circle = new Circle(radius);

		circle.setStroke(color);
		circle.setFill(color);
		circle.setId("Circle"+delivery.getLocation().getId());

		Tooltip tooltip = new Tooltip("Delivery on Intersection number : " + delivery.getLocation().getId()
				+ "\nDuration : " + delivery.getDuration());
		tooltip.setAutoHide(false);
		tooltip.install(circle, tooltip);
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
