	package lsbdp.agile.view;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lsbdp.agile.model.Intersection;

public class MainWindow extends Application {

	private static Stage stage;
	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent mainNode = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
		
		mainScene = new Scene(mainNode);
		primaryStage.setScene(mainScene);
		
		stage = primaryStage;
		primaryStage.setMaximized(true);
		primaryStage.setTitle("AGILE Project - Sprint 1");
		primaryStage.setOnShown(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				WindowManager.initializer(mainScene);
			}
		});
		primaryStage.show();
	}
	
	public static File openFileChooser(FileChooser fileChooser) throws InterruptedException {
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}
	
	public static File openFileChooserDeliveries(FileChooser fileChooser) throws InterruptedException, ParseException {
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}
	
	public static void openMessagePopup(String message) {
		Popup pop = PopupWindowManager.createMessagePopup(message);
		pop.show(stage);
	}
	
	public static void openAddPopUp(Intersection intersection) {
		Popup pop = PopupWindowManager.createAddPopup(intersection);
		pop.show(stage);
	}

	public static void openModifyPopUp(Popup pop) {
		pop.show(stage);
	}
	
	public static void openAddPopUp(Date start, Date end) {
		Popup pop = PopupWindowManager.createAddPopup(start, end);
		pop.show(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static File openFileChooserRoadmap(FileChooser fileChooser) {
		File file = fileChooser.showSaveDialog(stage);
		return file;
	}
}
