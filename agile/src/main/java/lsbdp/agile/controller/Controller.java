package lsbdp.agile.controller;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

import lsbdp.agile.algorithm.*;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.StreetMap;
import lsbdp.agile.view.WindowManager;

public class Controller {
	private static CommandList cmdList;
	private static StreetMap map;
	private static TSP algo;
	private static DeliverySchedule schedule;
	private static DeliveriesRequest deliveries;
	
	public Controller() {
		Controller.schedule = new DeliverySchedule();
		Controller.cmdList = new CommandList();
		Controller.algo = new ShortestLessCostTSP();
	}
	
	//Gérer Map
	public static void loadMap(File XML) throws ParseException {
		map = SerializerXML.deserializeMapXML(XML);
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
		deliveries = SerializerXML.deserializeDeliveryXML(XML, map);
		algo.findSolution(schedule, map, deliveries);
		WindowManager.colorDeliverySchedule(schedule);
		WindowManager.loadListView(schedule);
		return schedule;
	}
	
	public static DeliverySchedule getSchedule() {
		return schedule;
	}
	
	//Interaction avec Commandes
	public static void cmdDelete(Delivery element) {
		Command c = new CommandDelete(element);
		cmdList.addCommand(c);
		refreshIHM();
	}
	public static void cmdAdd() {
		WindowManager.highlightAll(map, schedule);
	}
	public static void cmdAdd2(Delivery element) {
		Command c = new CommandAdd(element);
		cmdList.addCommand(c);
		refreshIHM();
	}
	
	public static void cmdModify(Delivery element, Date startTime, Date endTime, int duration) {
		Command c = new CommandModify(element, startTime, endTime, duration);
		cmdList.addCommand(c);
	}
	
	public static void undo() {
		cmdList.undo();
		refreshIHM();
	}
	public static void redo() {
		cmdList.redo();
		refreshIHM();
	}
	
	public static void refreshIHM() {
		WindowManager.loadListView(schedule);
		WindowManager.colorDeliverySchedule(schedule);
	}
	
}
