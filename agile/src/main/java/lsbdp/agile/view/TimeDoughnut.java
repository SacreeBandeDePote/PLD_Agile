package lsbdp.agile.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.Street;
import sun.text.Normalizer;

public class TimeDoughnut extends Application {

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
		Circle circle = new Circle(200);
		circle.setCenterX(centerX);
		circle.setCenterY(centerY);
		Color color = Color.rgb(244, 244, 244);
		circle.setFill(color);


		return circle;
	}

	public static Arc createArcTravel(double start, double duration) {
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
		arc.setFill(Color.LIGHTGREEN);

		arc.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				highlightArc(arc);
			}
		});

		arc.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				unhighlightArc(arc);
			}
		});

		return arc;
	}

	public static void showArcInformations(Pane overlay, Delivery delivery) {
		VBox vbox = createVBoxDelvieryInformation(overlay, delivery);
		overlay.getChildren().add(vbox);
	}
	
	public static void hideArcInformations(Pane overlay) {
		//VBox vbox = (VBox) WindowManager.getScene().lookup("#InformationBox");
		overlay.getChildren().remove(overlay.getChildren().size()-1);
	}
	
	public static void highlightArc(Arc arc) {
		Timeline timeline = new Timeline();

		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusXProperty(), arc.getRadiusX())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusXProperty(), 275)),
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusYProperty(), arc.getRadiusY()	)),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusYProperty(), 275))
				);
		timeline.setAutoReverse(false);
		timeline.play();
	}

	public static void unhighlightArc(Arc arc) {
		Timeline timeline = new Timeline();

		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusXProperty(), arc.getRadiusX())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusXProperty(), 250)),
				new KeyFrame(Duration.ZERO, new KeyValue(arc.radiusYProperty(), arc.getRadiusY())),
				new KeyFrame(new Duration(500), new KeyValue(arc.radiusYProperty(), 250))
				);
		timeline.setAutoReverse(false);
		timeline.play();
	}

	public static Arc createArcFreeTime(double start, double duration) {
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
		arc.setFill(Color.LIGHTBLUE);

		arc.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				highlightArc(arc);
			}

		});
		arc.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				unhighlightArc(arc);
			}

		});
		return arc;
	}

	public static VBox createVBoxDelvieryInformation(Pane overlay, Delivery delivery) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeCheeseHBox");
		double centerX = hbox.getWidth()/2;
		double centerY = hbox.getHeight()/2;
		
		Label idLabel = new Label("Delivery on intersection " + 12457);
		Label durationLabel = new Label("Duration : " + delivery.getDuration());
		VBox vbox = new VBox(idLabel, durationLabel);
		vbox.setId("InformationBox");
		vbox.setAlignment(Pos.CENTER);
		vbox.setPrefHeight(50);
		vbox.setPrefWidth(300);
		vbox.setLayoutX(centerX-150);
		vbox.setLayoutY(centerY-100);
		vbox.setStyle("-fx-background-color : F0F0F0;"
				    + "-fx-border-radius : 15;"
				    + "-fx-border-color : silver;"
				    + "-fx-background-radius : 15;");
		return vbox;
	}

	public static Arc createArcDelivery(Pane overlay, Delivery delivery, double start, double duration) {
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
		arc.setFill(Color.RED);

		arc.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
				showArcInformations(overlay, delivery);
				highlightArc(arc);
			}
		});

		arc.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandlers.highlightDeliveryListView(delivery);
				hideArcInformations(overlay);
				unhighlightArc(arc);
			}
		});

		return arc;
	}

	public static void fillTimeDoughnut(Pane overlay, DeliverySchedule schedule, Scene scene) {
		Date currentTime = null;
		Date endOfLastDelivery = null;
		int odd = 0;
		int seconds = 10*60*60;
		double startT = 0d;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			currentTime = sdf.parse("9:0:0");
			endOfLastDelivery = sdf.parse("8:0:0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int hours = 8;
		for (Pair<Route, Delivery> p : schedule) {
			if(p.getKey() != null && p.getValue() != null) {
				Delivery delivery = p.getValue();
				Date deliveryTime = null;
				try {
					deliveryTime = sdf.parse(hours + ":0:0");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				drawDeliveryArc(overlay, delivery, deliveryTime);
				drawTravelArc(overlay, delivery, p.getKey(), deliveryTime);
				//drawFreeTimeArc(endOfLastDelivery, delivery, p.getKey());

				//endOfLastDelivery = getDateAfterDuration(delivery.getDeliveryTime(), delivery.getDuration());
				System.out.println("-----------------------");
				currentTime = deliveryTime;
				endOfLastDelivery = getDateAfterDuration(deliveryTime, delivery.getDuration());
				hours += 1;
			}
		}
		overlay.getChildren().add(createFakeHole());
	}

	public static void drawDeliveryArc(Pane overlay, Delivery delivery, Date d) {
		try {
			double angle = normalize(d);
			double duration = ((double)delivery.getDuration())/(10*60*60);
			duration *= 360;
			Arc arc = createArcDelivery(overlay, delivery, angle, -1*duration);
			overlay.getChildren().add(arc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static Date getDateAfterDuration( Date start, double duration) {
		Date end = null;
		System.out.println("Time begin : "  + start.toString() + ", Duration " + duration +"s");
		duration = TimeUnit.SECONDS.toMillis((long) duration);
		long newDate = (long) (start.getTime() + duration);
		end = new Date(newDate);
		return end;
	}

	public static Date getDateBeforeDuration( Date start, double duration) {
		Date end = null;
		System.out.println("Time begin : "  + start.toString() + ", Duration " + duration +"s");
		duration = TimeUnit.SECONDS.toMillis((long) duration);
		long newDate = (long) (start.getTime() - duration);
		end = new Date(newDate);
		return end;
	}

	public static void drawTravelArc(Pane overlay, Delivery delivery, Route route, Date d) {
		try {
			double angle = normalize(d);
			double duration = getRouteDuration(route)/(10*60*60);
			System.out.println("Duration in seconds : "+ getRouteDuration(route));
			duration *= 360;
			Arc arc = createArcTravel(angle, duration);
			overlay.getChildren().add(arc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void drawFreeTimeArc(Date startingTime, Delivery delivery, Route route) {
		Date deliveryTime = delivery.getDeliveryTime();

		Date freeTimeEnd = getDateBeforeDuration(deliveryTime, getRouteDuration(route));


	}

	public static double getRouteDuration(Route route) {
		double time = 0;
		double totalLenght = 0;
		List<Street> streets = route.getStreets();
		for(Street street : streets) {
			totalLenght += street.getLength();
		}
		time =  (totalLenght/4.16667);
		return time;
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
}