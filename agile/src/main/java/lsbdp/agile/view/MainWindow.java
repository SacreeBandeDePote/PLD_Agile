package lsbdp.agile.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainWindow extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		//Scene scene = FXMLLoader.load("MainWindow.fxml");
		
		Scene scene = new Scene(new Label("Main Window"));
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
