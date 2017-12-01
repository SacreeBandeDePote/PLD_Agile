package lsbdp.agile.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

public class WidgetBuilder {

	public static Label createDeliveryLabel(Delivery delivery, int count) {

		Label label = new Label("Livraison n�"+count);	
		label.setId(String.valueOf(delivery.getLocation().getId()));

		return label;
	}

	public static HBox createListViewHBoxWarehouse(Intersection warehouse) {
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER_LEFT);

		Label label = new Label("Warehouse");
		label.setId(""+warehouse.getId());
		hbox.getChildren().add(label);

		hbox.setOnMouseEntered(new EventHandler<MouseEvent>(){
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

	public static Button createListViewDeleteButton(Delivery delivery) {
		Button btn = new Button();
		btn.setText("X");
		btn.setStyle("-fx-background-color : d21919");
		btn.setMaxHeight(4);
		btn.setMaxWidth(4);
		return btn;
	}

	public static HBox createListViewHBox(Delivery delivery, int count) {
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER_LEFT	);

		Label label = createDeliveryLabel(delivery, count);

		Button btn = WidgetBuilder.createListViewDeleteButton(delivery);
		hbox.getChildren().addAll(btn, label);

		hbox.setOnMouseEntered(new EventHandler<MouseEvent>(){
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

	public static Circle createDeliveryCircle(Delivery delivery, Color color,Double radius) {
		Circle circle = new Circle(radius);

		circle.setStroke(color);
		circle.setFill(color);
		circle.setId("Circle"+delivery.getLocation().getId());

		Tooltip tooltip = new Tooltip("Delivery duration : " + delivery.getDuration());
		tooltip.setAutoHide(false);
		tooltip.install(circle, tooltip);
		circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
			}	
		});

		circle.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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
		circle.setId("Circle"+warehouse.getId());

		circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightWarehouse(warehouse);
			}	
		});

		circle.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.unhighlightWarehouse(warehouse);
			}	
		});

		return circle;
	}
}
