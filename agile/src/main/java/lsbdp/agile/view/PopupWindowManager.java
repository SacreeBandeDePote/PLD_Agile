package lsbdp.agile.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import lsbdp.agile.controller.Controller;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.Intersection;

public class PopupWindowManager {

	/**
	 * Create the popup window that allows the user to add a delivery for a given intersection
	 * 
	 * @param intersection
	 * @return
	 * 
	 * @see openMessagePopup
	 */
	public static Popup createAddPopup(Intersection intersection) {
		Popup pop = new Popup();
		Label l = new Label("Create Delivery on intersection " + intersection.getId());

		Label durationLabel = new Label("Duration* : ");
		TextField durationField = new TextField();
		durationField.setText("in seconds");

		HBox durationBox = new HBox(durationLabel, durationField);
		durationBox.setAlignment(Pos.CENTER_LEFT);

		Label startLabel = new Label("Starting time : ");
		TextField startField = new TextField();
		startField.setText("HH:MM:SS");

		HBox startBox = new HBox(startLabel, startField);
		startBox.setAlignment(Pos.CENTER_LEFT);

		Label endLabel = new Label("End time : ");
		TextField endField = new TextField();
		endField.setText("HH:MM:SS");

		HBox endBox = new HBox(endLabel, endField);
		endBox.setAlignment(Pos.CENTER_LEFT);

		Label mandatoryLabel = new Label("Items with * are mandatory");

		Button validButton = new Button("Confirm");
		validButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!isInteger(durationField.getText())) {
					MainWindow.openMessagePopup("Please enter a correct duration");
				} else {
					EventHandlers.addDelivery(intersection, durationField.getText(), startField.getText(),
							endField.getText());
				}
				pop.hide();
			}
		});

		Button returnButton = new Button("Return");
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pop.hide();
			}
		});

		HBox buttonBox = new HBox(validButton, returnButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(15);

		VBox vbox = new VBox(l, durationBox, startBox, endBox, mandatoryLabel, buttonBox);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
				+ "-fx-border-width : 3;" + "-fx-border-radius : 5");

		pop.getContent().add(vbox);

		return pop;
	}

	/**
	 * Create the popup window that allows the user to modify a delivery
	 * 
	 * @param delivery
	 * @param duration
	 * @param start
	 * @param end
	 * @return
	 */
	public static Popup createModifyPopup(Delivery delivery, String duration, String start, String end) {
		Popup pop = new Popup();
		Label l = new Label("Modify Delivery on intersection " + delivery.getLocation().getId());

		Label durationLabel = new Label("Duration* : ");
		TextField durationField = new TextField();
		durationField.setText(duration);

		HBox durationBox = new HBox(durationLabel, durationField);
		durationBox.setAlignment(Pos.CENTER_LEFT);

		Label startLabel = new Label("Starting time : ");
		TextField startField = new TextField();
		startField.setText(start);

		HBox startBox = new HBox(startLabel, startField);
		startBox.setAlignment(Pos.CENTER_LEFT);

		Label endLabel = new Label("End time : ");
		TextField endField = new TextField();
		endField.setText(end);

		HBox endBox = new HBox(endLabel, endField);
		endBox.setAlignment(Pos.CENTER_LEFT);

		Label mandatoryLabel = new Label("Items with * are mandatory");

		Button validButton = new Button("Confirm");
		validButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!isInteger(durationField.getText())) {
					MainWindow.openMessagePopup("Please enter a correct duration");
				} else {
					try {
						EventHandlers.modifyDelivery(delivery, durationField.getText(), startField.getText(),
								endField.getText());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				pop.hide();
			}
		});

		Button returnButton = new Button("Return");
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pop.hide();
			}
		});

		HBox buttonBox = new HBox(validButton, returnButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(15);

		VBox vbox = new VBox(l, durationBox, startBox, endBox, mandatoryLabel, buttonBox);

		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
				+ "-fx-border-width : 3;" + "-fx-border-radius : 5");

		pop.getContent().add(vbox);

		return pop;
	}

	/**
	 * Create the popup window that allows the user to add a delivery without a intersection
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Popup createAddPopup(Date start, Date end) {
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Popup pop = new Popup();
		Label l = new Label("Create Delivery ");

		Label intersectionLabel = new Label("Intersection : ");
		TextField intersectionField = new TextField();

		HBox intersectionBox = new HBox(intersectionLabel, intersectionField);
		intersectionBox.setAlignment(Pos.CENTER_LEFT);

		Label durationLabel = new Label("Duration* : ");
		TextField durationField = new TextField();
		durationField.setText("in seconds");

		HBox durationBox = new HBox(durationLabel, durationField);
		durationBox.setAlignment(Pos.CENTER_LEFT);

		Label startLabel = new Label("Starting time : ");
		TextField startField = new TextField();
		startField.setText(sdf.format(start));

		HBox startBox = new HBox(startLabel, startField);
		startBox.setAlignment(Pos.CENTER_LEFT);

		Label endLabel = new Label("End time : ");
		TextField endField = new TextField();
		endField.setText(sdf.format(end));

		HBox endBox = new HBox(endLabel, endField);
		endBox.setAlignment(Pos.CENTER_LEFT);

		Label mandatoryLabel = new Label("Items with * are mandatory");

		Button validButton = new Button("Confirm");
		validButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (isInteger(durationField.getText())) {
					long id = Long.parseLong(intersectionField.getText());
					Intersection intersection = Controller.getMap().get(id);
					if (intersection != null) {
						EventHandlers.addDelivery(intersection, durationField.getText(), startField.getText(),
								endField.getText());
					} else {
						MainWindow.openMessagePopup("Incorrect Intersection");
					}
				} else {
					MainWindow.openMessagePopup("Please enter a correct duration");
				}
				pop.hide();
			}
		});

		Button returnButton = new Button("Return");
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pop.hide();
			}
		});

		HBox buttonBox = new HBox(validButton, returnButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(15);

		VBox vbox = new VBox(l, intersectionBox, durationBox, startBox, endBox, mandatoryLabel, buttonBox);

		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
				+ "-fx-border-width : 3;" + "-fx-border-radius : 5");

		pop.getContent().add(vbox);

		return pop;
	}

	/**
	 * Checks if a String is an integer
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static Popup createMessagePopup(String message) {
		Popup pop = new Popup();
		Label mess = new Label(message);
		mess.setPadding(new Insets(15));
		Button returnButton = new Button("Return");

		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				pop.hide();
			}
		});

		VBox vbox = new VBox(mess, returnButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
				+ "-fx-border-width : 3;" + "-fx-border-radius : 5");

		pop.getContent().add(vbox);

		return pop;
	}

	public static Popup createWaitingPopup() {
		Popup pop = new Popup();
		Label mess = new Label("Please wait");
		mess.setPadding(new Insets(15));

		VBox vbox = new VBox(mess);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color : FFFFFF;" + "-fx-background-radius : 5;" + "-fx-border-color : C0C0C0;"
				+ "-fx-border-width : 3;" + "-fx-border-radius : 5");

		pop.getContent().add(vbox);
		return pop;
	}
}
