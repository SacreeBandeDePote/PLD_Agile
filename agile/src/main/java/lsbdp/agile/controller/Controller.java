package lsbdp.agile.controller;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javafx.util.Pair;
import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.algorithm.GloutonTSP;
import lsbdp.agile.data.SerializeXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

public class Controller {
	private static CommandList cmdList;
	private static StreetMap map;
	private static GloutonTSP algo; 
	private static DeliverySchedule schedule;
	private static DeliveriesRequest deliveries;
	
	public Controller() {
		Controller.cmdList = new CommandList();
		Controller.algo = new GloutonTSP();
	}
	
	//Gérer Map
	public void loadMap(File XML) throws ParseException {
		map = SerializeXML.serializeMapXML(XML);
		WindowManager.drawMap(map);
	}
	public static void drawMap(){
		WindowManager.drawMap(map);
	}
	public static StreetMap getMap() {
		return map;
	}
	
	//Gérer Schedule
	public static DeliverySchedule loadDeliveryRequest(File XML) throws ParseException {
		deliveries = SerializeXML.serializeDeliveryXML(XML, map);
		algo.findSolution(schedule, map, deliveries.getWarehouse(), deliveries.getDeliveryList());
		return schedule;
	}
	public static DeliverySchedule getSchedule() {
		return schedule;
	}
	
	//Interaction avec Commandes
	public static void cmdDelete(Delivery element) {
		Command c = new CommandDelete(element);
		cmdList.addCommand(c);
	}
	public static void cmdAdd() {
		/*Command c = new CommandAdd();
		cmdList.addCommand(c);*/
	}
	public static void cmdModify(Delivery element, Date startTime, Date endTime) {
		Command c = new CommandModify(element, startTime, endTime);
		cmdList.addCommand(c);
	}
	public static void undo() {
		cmdList.undo();
	}
	public void redo() {
		cmdList.redo();
	}
	
}
