package lsbdp.agile.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

public class WidgetBuilder {

	public static Label createDeliveryLabel(Delivery delivery, int count) {
		
		Label label = new Label("Livraison n°"+count);	
		label.setId(String.valueOf(delivery.getLocation().getId()));
		
		return label;
	}
	
	public static HBox createListViewHBox(Delivery delivery, int count) {
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER_LEFT	);
		
		Label label = createDeliveryLabel(delivery, count);
		
		Button btn = new Button();
		btn.setText("X");
		btn.setStyle("-fx-background-color : d21919");
		
		btn.setMaxHeight(4);
		btn.setMaxWidth(4);

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
		
		hbox.getChildren().addAll(btn, label);
		return hbox;
	}
	
	public static Circle createIntersectionCircle(Delivery delivery, Color color,Double radius) {
		Circle circle = new Circle(radius);
		
        circle.setStroke(color);
        circle.setFill(color);
        circle.setId("Circle"+delivery.getLocation().getId());
        
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.mapDeliveryMouseEnter(delivery);
			}	
        });
        
        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.mapDeliveryMouseExit(delivery);
			}	
        });
        
        return circle;
	}
}
