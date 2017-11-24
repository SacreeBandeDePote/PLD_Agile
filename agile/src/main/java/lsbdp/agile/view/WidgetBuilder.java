package lsbdp.agile.view;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lsbdp.agile.model.Delivery;

public class WidgetBuilder {

	public static Label createDeliveryLabel(Delivery delivery, int count, Canvas canvas) {
		Label label = new Label("Livraison n°"+count);
		label.setId(String.valueOf(delivery.getLocation().getId()));
		
		label.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				ListViewEventHandlers.highlightIntersection(delivery.getLocation());
			}

		});
		
		label.setOnMouseExited(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				ListViewEventHandlers.unhighlightIntersection(delivery.getLocation());
			}

		});
		return label;
	}
}
