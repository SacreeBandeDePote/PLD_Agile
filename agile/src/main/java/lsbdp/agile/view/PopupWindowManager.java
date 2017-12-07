package lsbdp.agile.view;

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

	public static Popup createAddPopup(Intersection intersection) {
		Popup pop = new Popup();
		Label l = new Label("Create Delivery on intersection " + intersection.getId());
		
		Label durationLabel = new Label("Duration* : ");
		TextField durationField = new TextField();
		durationField.setText("in seconds");
		
		HBox durationBox =  new HBox(durationLabel, durationField);
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
		validButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(!isInteger(durationField.getText())) {
					MainWindow.openMessagePopup("Please enter a correct duration");
				}
				else {
					EventHandlers.addDelivery(intersection,durationField.getText(),startField.getText(),endField.getText());
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
		vbox.setStyle("-fx-background-color : FFFFFF;"
					+ "-fx-background-radius : 5;"
				    + "-fx-border-color : C0C0C0;"
				    + "-fx-border-width : 3;"
				    + "-fx-border-radius : 5");
		
		pop.getContent().add(vbox);
		
		return pop;
	}
	
	public static VBox createVBoxDelvieryInformation(Pane overlay, Delivery delivery) {
		HBox hbox = (HBox) WindowManager.getScene().lookup("#timeDoughnutHBox");
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
	
	public static Popup createAddPopup() {
		Popup pop = new Popup();
		Label l = new Label("Create Delivery ");
		
		Label intersectionLabel = new Label("Intersection : ");
		TextField intersectionField = new TextField();
		
		HBox intersectionBox = new HBox(intersectionLabel, intersectionField);
		intersectionBox.setAlignment(Pos.CENTER_LEFT);
		
		Label durationLabel = new Label("Duration* : ");
		TextField durationField = new TextField();
		durationField.setText("in seconds");
		
		HBox durationBox =  new HBox(durationLabel, durationField);
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
		validButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(isInteger(durationField.getText())) {
					long id = Long.parseLong(intersectionField.getText());
					Intersection intersection = Controller.getMap().get(id);
					System.out.println(intersectionField.getText());
					if(intersection != null) {
						EventHandlers.addDelivery(intersection,durationField.getText(),startField.getText(),endField.getText());
					} else {
						MainWindow.openMessagePopup("Incorrect Intersection");	
					}
				}else {
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
		vbox.setStyle("-fx-background-color : FFFFFF;"
					+ "-fx-background-radius : 5;"
				    + "-fx-border-color : C0C0C0;"
				    + "-fx-border-width : 3;"
				    + "-fx-border-radius : 5");
		
		pop.getContent().add(vbox);
		
		return pop;
	}
	
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
		vbox.setStyle("-fx-background-color : FFFFFF;"
					+ "-fx-background-radius : 5;"
				    + "-fx-border-color : C0C0C0;"
				    + "-fx-border-width : 3;"
				    + "-fx-border-radius : 5");
		
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
		vbox.setStyle("-fx-background-color : FFFFFF;"
					+ "-fx-background-radius : 5;"
				    + "-fx-border-color : C0C0C0;"
				    + "-fx-border-width : 3;"
				    + "-fx-border-radius : 5");
		
		pop.getContent().add(vbox);
		return pop;
	}
}
