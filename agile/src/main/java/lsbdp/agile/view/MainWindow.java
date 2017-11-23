package lsbdp.agile.view;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import lsbdp.agile.data.SerializeXML;
import lsbdp.agile.model.StreetMap;

public class MainWindow extends Application {

	private static Stage stage;
	private static Scene mainScene;
	/**
	 * 
	 * @author Matthieu
	 * @param Stage primaryStage
	 * 
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
				
		Parent mainNode = FXMLLoader.load(MainWindow.class.getResource("MainWindow.fxml"));
		
		File f = new File("MainWindow.fxml");
		mainScene = new Scene(mainNode);
		primaryStage.setScene(mainScene);
		
		
		
		stage = primaryStage;
		primaryStage.setMaximized(true);
		primaryStage.setTitle("AGILE Project - Sprint 1");
		primaryStage.setOnShown(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				MainWindowController.initializer(mainScene);
			}
		});
		primaryStage.show();
	}
	
	public static File openFileChooserMap(FileChooser fileChooser) {
		File file = fileChooser.showOpenDialog(stage);
		
		SerializeXML serializer = new SerializeXML();
		StreetMap m;
		try {
			m = serializer.serializeMapXML(file);
			MainWindowController.loadMap(m);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
