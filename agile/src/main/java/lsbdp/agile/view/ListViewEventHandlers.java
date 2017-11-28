package lsbdp.agile.view;

import javafx.scene.paint.Color;
import lsbdp.agile.model.Intersection;

public class ListViewEventHandlers {

	public static void highlightIntersection(Intersection intersection) {
		WindowManager.canvasDrawer.drawIntersection(intersection, Color.BLUE, (double)10);
	}
	
	public static void unhighlightIntersection(Intersection intersection) {
		WindowManager.canvasDrawer.drawIntersection(intersection, Color.RED, (double)10);
	}
}
