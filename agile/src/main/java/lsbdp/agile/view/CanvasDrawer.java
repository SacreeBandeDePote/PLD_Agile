package lsbdp.agile.view;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
		canvas = new Canvas(750,750);
		Double canvasWidth = canvas.getWidth();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Map<Long, Intersection> intersections = map;
		Set keys = intersections.keySet();
		Iterator iterator = keys.iterator();
		while(iterator.hasNext() ) {
			Long key = (Long) iterator.next();
			Intersection intersection = intersections.get(key);
			this.drawIntersection(intersection, Color.GREY,(double)1);
			
			List<Street> neighbors = intersection.getStreets();
			for(Street inter : neighbors) {
				this.drawStreet(intersection, inter.getEnd(), Color.GREY);
			}
		}
		gc.strokeLine(0, 0, canvasWidth, 0);
		gc.strokeLine(0, 0, 0, canvasWidth);
		gc.strokeLine(canvasWidth, canvasWidth, canvasWidth, 0);
		gc.strokeLine(canvasWidth, canvasWidth, 0, canvasWidth);
		HBox ap = (HBox) scene.lookup("#canvasHBox");
		ap.getChildren().clear();
		ap.getChildren().add(canvas);
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
