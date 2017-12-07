package lsbdp.agile.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TimeDoughnut extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Group g = new Group();
		Scene scene = new Scene(g, 550, 250);

		stage.setScene(scene);
		stage.show();		
	}





}