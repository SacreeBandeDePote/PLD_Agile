package lsbdp.agile.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lsbdp.agile.model.Intersection;

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
	public CanvasDrawer(int maxX, int minX, int maxY, int minY, Canvas canvas) {
		this.maxX   = maxX;
		this.minX   = minX;
		this.maxY   = maxY;
		this.minY   = minY;
		this.canvas = canvas;
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
