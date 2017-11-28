package lsbdp.agile.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lsbdp.agile.model.Delivery;

public class WidgetBuilder {

	public static Label createDeliveryLabel(Delivery delivery, int count) {
		
		Label label = new Label("Livraison n°"+count);
		
		label.setId(String.valueOf(delivery.getLocation().getId()));
		
		label.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				EventHandlers.highlightIntersection(delivery.getLocation());
			}

		});
		
		label.setOnMouseExited(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.unhighlightIntersection(delivery.getLocation());
			}
		});
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

		hbox.getChildren().addAll(btn, label);
		return hbox;
	}
}
