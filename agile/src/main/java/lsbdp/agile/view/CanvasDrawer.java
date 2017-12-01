package lsbdp.agile.view;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

public class CanvasDrawer {

	int maxX;
	int minX;
	int maxY;
	int minY;
	Canvas canvas;
	
	/**
	 * 
	 * @param maxX
	 * @param minX
	 * @param maxY
	 * @param minY
	 * @param canvas
	 */
	public CanvasDrawer(int maxX, int minX, int maxY, int minY, Scene scene) {
		this.maxX   = maxX;
		this.minX   = minX;
		this.maxY   = maxY;
		this.minY   = minY;
	}
	
	public void drawMap(StreetMap map, Scene scene) {

		HBox ap            = (HBox) scene.lookup("#canvasHBox");
		Double dimension      = Double.min(ap.getHeight(), ap.getWidth());
		canvas             = new Canvas(dimension-30, dimension-30);
		Pane overlay       = new Pane();
		Double canvasWidth = canvas.getWidth();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Set<Long> keys     = map.keySet();
		Iterator iterator  = keys.iterator();
		
		overlay.setId("overlay");
		while(iterator.hasNext() ) {
			Long key = (Long) iterator.next();
			Intersection intersection = map.get(key);
			this.drawIntersection(intersection, Color.GREY, 1d);
			
			List<Street> neighbors = intersection.getStreets();
			for(Street inter : neighbors) {
				this.drawStreet(intersection, inter.getEnd(), Color.GREY);
			}
		}
		gc.strokeLine(0, 0, canvasWidth, 0);
		gc.strokeLine(0, 0, 0, canvasWidth);
		gc.strokeLine(canvasWidth, canvasWidth, canvasWidth, 0);
		gc.strokeLine(canvasWidth, canvasWidth, 0, canvasWidth);
		
		Group mapGroup = new Group(canvas, overlay);
		
		ap.getChildren().clear();
		ap.getChildren().add(mapGroup);
	}

	/**
	 * 
	 * @param intersection
	 * @param color
	 * @param radius
	 */
	public void drawIntersection(Intersection intersection, Color color, Double radius) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Double x           = normalizeX((double)intersection.getX(), canvas.getWidth());
		Double y           = normalizeY((double)intersection.getY(), canvas.getHeight());
		Double delta       = radius/2;
		
		gc.setFill(color);
		gc.setStroke(color);
		gc.fillOval(x-delta, y-delta, radius, radius);
		gc.strokeOval(x-delta, y-delta, radius, radius);
	}
	
	public void drawDelivery(Pane overlay, Delivery delivery, Color color, Double radius) {
		Intersection intersection = delivery.getLocation();
		Double x                  = normalizeX((double)intersection.getX(), canvas.getWidth());
		Double y                  = normalizeY((double)intersection.getY(), canvas.getHeight());
		Circle circle             = WidgetBuilder.createDeliveryCircle(delivery, color, radius);
        
		circle.relocate(x-radius, y-radius);
        overlay.getChildren().add(circle);
	}
	
	public void drawWarehouse(Pane overlay, Intersection warehouse, Color color, Double radius) {
		Double x                  = normalizeX((double)warehouse.getX(), canvas.getWidth());
		Double y                  = normalizeY((double)warehouse.getY(), canvas.getHeight());
		Circle circle             = WidgetBuilder.createWarehouseCircle(warehouse, color, radius);
        
		circle.relocate(x-radius, y-radius);
        overlay.getChildren().add(circle);
	}
	
	public void drawTemporaryIntersection(Pane overlay, Intersection intersection, Color color, Double radius) {
		Double x                  = normalizeX((double)intersection.getX(), canvas.getWidth());
		Double y                  = normalizeY((double)intersection.getY(), canvas.getHeight());
		Circle circle             = WidgetBuilder.createTemporaryIntersectionCircle(intersection, color, radius);
        
		circle.relocate(x-radius, y-radius);
        overlay.getChildren().add(circle);
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param color
	 */
	public void drawStreet(Intersection start, Intersection end, Color color) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Double startX      = normalizeX((double)start.getX(), canvas.getWidth());
		Double startY      = normalizeY((double)start.getY(), canvas.getHeight());
		Double endX        = normalizeX((double)end.getX(), canvas.getWidth());
		Double endY        = normalizeY((double)end.getY(), canvas.getHeight());
		
		gc.setLineWidth(1);
		gc.setFill(color);
		gc.strokeLine(startX, startY, endX, endY);
	}
	
	
	public void drawStreetOverlay(Pane overlay, Intersection start, Intersection end, Color color) {
		Double startX      = normalizeX((double)start.getX(), canvas.getWidth());
		Double startY      = normalizeY((double)start.getY(), canvas.getHeight());
		Double endX        = normalizeX((double)end.getX(), canvas.getWidth());
		Double endY        = normalizeY((double)end.getY(), canvas.getHeight());
		Line line          = new Line(startX, startY, endX, endY);
		
	    line.setStrokeWidth(1);
	    line.setStroke(color);
	    
	    overlay.getChildren().add(line);
	}
	/**
	 * 
	 * @param x
	 * @param width
	 * @return
	 */
	public Double normalizeX(Double x, Double width) {
		Double newX = (x-minX)/(maxX-minX);
		newX *= width;
		return newX;
	}
	
	/**
	 * 
	 * @param y
	 * @param height
	 * @return
	 */
	public Double normalizeY(Double y, Double height) {
		Double newY = (y-minY)/(maxY-minY);
		newY *= height;
		return newY;
	}
}
