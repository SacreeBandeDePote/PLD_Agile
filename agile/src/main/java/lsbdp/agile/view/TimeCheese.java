package lsbdp.agile.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.Street;

public class TimeCheese extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		 Group g = new Group();
	        Scene scene = new Scene(g, 550, 250);
	        
	        stage.setScene(scene);
	        stage.show();		
	}
	
	public static Circle createFakeHole() {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeCheeseHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;
		Circle circle = new Circle(150);
		circle.setCenterX(centerX);
		circle.setCenterY(centerY);
		Color color = Color.rgb(244, 244, 244);
		circle.setFill(color);
		
		
		return circle;
	}
	
	public static Arc createArc(Delivery delivery, double start, double duration, Color color) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeCheeseHBox");
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
        arc.setFill(color);
        
        arc.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
			}
		});
        
        return arc;
	}
	
	public static void fillTimeCheese(Pane overlay, DeliverySchedule schedule, Scene scene) throws ParseException {
		Date currentTime = null;
		int odd = 0;
		int seconds = 10*60*60;
		Double startT = 360d;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		currentTime = sdf.parse("8:0:0");
		for (Pair<Route, Delivery> p : schedule) {
			System.out.println(p.getKey() +" " + p.getValue());
			if(p.getKey() != null && p.getValue() != null) {
				Delivery del = p.getValue();
				Date start = del.getTimespanStart();
				Date end = del.getTimespanEnd();
				double dur = del.getDuration();
				if(odd == 0) {
					System.err.println(startT);
					dur += getRouteDuration(p.getKey());
					dur = dur/seconds;
					dur = dur*360;
					Arc arc = createArc(del, startT, -1*dur, Color.BLUE);
					startT -= dur;
					overlay.getChildren().add(arc);
					odd++;
				}else{
					System.err.println(startT);
					dur += getRouteDuration(p.getKey());
					dur = dur/seconds;
					dur = dur*360;
					Arc arc = createArc(del, startT, -1*dur, Color.RED);
					startT -= dur;
					overlay.getChildren().add(arc);
					odd--;
				}
			}
		}
		overlay.getChildren().add(createFakeHole());
	}
	
	public static Double getRouteDuration(Route route) {
		Double time = 0d;
		
		List<Street> streets = route.getStreets();
		
		for(Street street : streets) {
			float streetLength = street.getLength();
			time += streetLength/4.16;
		}
		
		return time;
	}
	
	public static void main(String[] args) {
		launch();
	}
}