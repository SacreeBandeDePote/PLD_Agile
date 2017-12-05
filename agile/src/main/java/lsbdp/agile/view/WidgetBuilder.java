package lsbdp.agile.view;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.sun.deploy.jcp.controller.cacheviewer.DelAppInfo;

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
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

public class WidgetBuilder {
	
	private static double deltaX = 0;
	private static double deltaY = 0;

	public static Label createDeliveryLabel(Delivery delivery, int count) {
		
		Label label = new Label("Livraison n�"+count + ", duration : " + delivery.getDuration()+"s");	
		
		label.setId("Delivery-"+String.valueOf(delivery.getLocation().getId()));
		
		return label;
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

	public static Button createListViewDeleteButton(Delivery delivery) {
		Button btn = new Button();
		btn.setText("X");
		btn.setStyle("-fx-background-radius : 40");
		btn.setMaxHeight(4);
		btn.setMaxWidth(4);
		
		
		btn.setOnMouseClicked(new EventHandler<MouseEvent>(){	
			public void handle(MouseEvent e) {
				EventHandlers.deleteDelivery(delivery);		
				}
		});
		
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

	public static ZoomablePane createZoomablePane(Node node) {
		ZoomablePane zp = new ZoomablePane(node);
		zp.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				double wheelDelta = event.getDeltaY();
				zp.onScroll(wheelDelta, new Point2D(event.getX(), event.getY()));
			}
		});
		
		zp.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			double deltaX = 0;
			double deltaY = 0;

			@Override
			public void handle(MouseEvent e) {
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				Scene scene = WindowManager.getScene();
				//Group g = (Group) scene.lookup("#mapGroup");
				System.out.println(e.getX() + "" + e.getY());
				zp.setTranslateX(e.getX());
				zp.setTranslateY(e.getY());
			}
		});
		return zp;
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
				deltaX = e.getSceneX();
				deltaY = e.getSceneY();
			}
		});
		
		g.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				try {
					TimeUnit.MICROSECONDS.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				deltaX = e.getX();
				deltaY = e.getY();
				
				System.out.println("DELTA X = " + deltaX);
				System.out.println("DELTA Y = " + deltaY);
				
				g.setTranslateX(deltaX);
				g.setTranslateY(deltaY);
			}
		});

		return g;
	}
}
