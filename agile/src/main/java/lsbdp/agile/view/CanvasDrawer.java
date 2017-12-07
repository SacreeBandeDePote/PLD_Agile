package lsbdp.agile.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
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

		StackPane sPane = (StackPane) scene.lookup("#mainStackPane");
		Double dimension      = Double.min(sPane.getHeight(), sPane.getWidth());
		canvas             = new Canvas(dimension-30, dimension-30);
		canvas.setOpacity(1d);
		Pane overlay       = new Pane();
		
 		Double canvasWidth = canvas.getWidth();
		GraphicsContext gc = canvas.getGraphicsContext2D();		
		Set<Long> keys     = map.keySet();
		Iterator iterator  = keys.iterator();

		gc.setFill(new Color(0.957, 0.957, 0.957, 1));
		gc.fillRect(0, 0, canvasWidth, canvasWidth);
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
		
		Group drawGroup = WidgetBuilder.createDrawGroup(canvas,overlay);

		sPane.getChildren().clear();

		HBox hbox = new HBox();
		hbox.setId("timeDoughnutHBox");
		hbox.setStyle("-fx-background-color: derive(#ececec,26.4%);");
		hbox.setAlignment(Pos.CENTER);
		sPane.getChildren().add(hbox);
		sPane.getChildren().add(drawGroup);
	}

	public static void fillTimeDoughnut(Pane overlay, DeliverySchedule schedule, Scene scene) {
		Date endOfLastDelivery = null;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			endOfLastDelivery = sdf.parse("8:0:0");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (Pair<Route, Delivery> p : schedule) {
			if(p.getKey() != null && p.getValue() != null) {
				Delivery delivery = p.getValue();
				drawDeliveryArc(overlay, delivery);
				drawTravelArc(overlay, delivery, p.getKey());
				drawFreeTimeArc(overlay, endOfLastDelivery, delivery, p.getKey());
				endOfLastDelivery = getDateAfterDuration(delivery.getDeliveryTime(), delivery.getDuration());
			}
		}
		drawEndofDayArc(overlay, endOfLastDelivery);

		overlay.getChildren().add(WidgetBuilder.createFakeHole());
		overlay.getChildren().add(WidgetBuilder.createLegend(overlay));
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
		
	    line.setStrokeWidth(2);
	    line.setStroke(color);
	    
	    Circle travelerCircle = new Circle(2d); 
	    travelerCircle.setFill(Color.GREEN);
	    
	    Timeline timeline = new Timeline();

		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(travelerCircle.centerXProperty(), startX)),
				new KeyFrame(new Duration(500), new KeyValue(travelerCircle.centerXProperty(), endX)),
				new KeyFrame(Duration.ZERO, new KeyValue(travelerCircle.centerYProperty(), startY)),
				new KeyFrame(new Duration(500), new KeyValue(travelerCircle.centerYProperty(), endY))
				);
		timeline.setAutoReverse(false);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	    
	    overlay.getChildren().add(line);
	    overlay.getChildren().add(travelerCircle);
	}
	
	public static void drawEndofDayArc(Pane overlay, Date endOfLastDelivery) {
		try {
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Date maxDate = sdf.parse("18:0:0");
			double angle = normalize(endOfLastDelivery);
			double duration = maxDate.getTime() - endOfLastDelivery.getTime();
			duration = TimeUnit.MILLISECONDS.toSeconds((long) duration);
			duration = duration/(10*60*60);
			duration *= -360;
			Arc arc = WidgetBuilder.createArcFreeTime(angle, duration);
			overlay.getChildren().add(arc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void drawTravelArc(Pane overlay, Delivery delivery, Route route) {
		try {
			double angle = normalize(delivery.getDeliveryTime());
			double duration = route.getRouteDuration()/(10*60*60);
			duration *= 360;
			Arc arc = WidgetBuilder.createArcTravel(angle, duration);
			overlay.getChildren().add(arc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void drawFreeTimeArc(Pane overlay, Date startingTime, Delivery delivery, Route route) {
		Date deliveryTime = delivery.getDeliveryTime();
		Date freeTimeEnd = getDateBeforeDuration(deliveryTime, route.getRouteDuration());
		try {
			double angle = normalize(startingTime);
			double duration = (freeTimeEnd.getTime()) - startingTime.getTime();
			duration = TimeUnit.MILLISECONDS.toSeconds((long) duration);
			duration = duration/(10*60*60);
			duration *= -360;
			if(duration != 0) {
				Arc arc = WidgetBuilder.createArcFreeTime(angle, duration);
				overlay.getChildren().add(arc);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void drawDeliveryArc(Pane overlay, Delivery delivery) {
		try {
			double angle = normalize(delivery.getDeliveryTime());
			double duration = ((double)delivery.getDuration())/(10*60*60);
			duration *= 360;
			Arc arc = WidgetBuilder.createArcDelivery(overlay, delivery, angle, -1*duration);
			overlay.getChildren().add(arc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static double normalize(Date time) throws ParseException {
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date minDate = sdf.parse("8:0:0");
		Date maxDate = sdf.parse("18:0:0");

		double d1 = time.getTime()-minDate.getTime();
		double d2 = maxDate.getTime()-minDate.getTime();
		double angle = d1/d2;
		angle *= -1;
		angle += 1;
		angle *= 360;
		angle += 90;
		return angle;
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
	
	public static Date getDateAfterDuration( Date start, double duration) {
		Date end = null;
		duration = TimeUnit.SECONDS.toMillis((long) duration);
		long newDate = (long) (start.getTime() + duration);
		end = new Date(newDate);
		return end;
	}

	public static Date getDateBeforeDuration( Date start, double duration) {
		Date end = null;
		duration = TimeUnit.SECONDS.toMillis((long) duration);
		long newDate = (long) (start.getTime() - duration);
		end = new Date(newDate);
		return end;
	}
}
