package lsbdp.agile.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import lsbdp.agile.algorithm.*;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.StreetMap;
import lsbdp.agile.view.MainWindow;
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
		Controller.algo = new NNHTimeTSP();
	}

	/**
	 * Loads a map from a file and draws it
	 * 
	 * @param XML			the file to load
	 */
	public static void loadMap(File xml) throws ParseException {
		map = SerializerXML.deserializeMapXML(xml);
		if(map != null) {
			WindowManager.drawMap(map);
		}else {
			MainWindow.openMessagePopup("Invalid map file");
		}

	}
	
	/**
	 * Saves the route in a file
	 * 
	 * @param XML			the file to save to
	 */
	public static void saveDeliveries(File xml) {
		SerializerXML.serializeDeliveryXML(schedule, xml);
	}

	public static void drawMap(){
		WindowManager.drawMap(map);
	}
	public static StreetMap getMap() {
		return map;
	}

	/**
	 * Loads a route from a file
	 * 
	 * @param XML			the file to load
	 * @return the route
	 */
	public static DeliverySchedule loadDeliveryRequest(File xml) throws ParseException {
		deliveries = SerializerXML.deserializeDeliveryXML(xml, map);
		if(deliveries.getDeliveryList().size() == 0) {
			MainWindow.openMessagePopup("Invalid delivery file");
		}else {
			algo.findSolution(schedule, map, deliveries);
			refreshIHM();
		}
		return schedule;
	}

	/**
	 * Gets the current route
	 * 
	 * @return the current route
	 */
	public static DeliverySchedule getSchedule() {
		return schedule;
	}

	public static void generateRoadmapActionHandler(File xml) throws IOException {
		SerializerXML.generateRoadMap(xml, schedule);
	}

	/**
	 * Deletes a delivery from the route
	 * 
	 * @param element			the delivery to delete
	 */
	public static void cmdDelete(Delivery element) {
		Command c = new CommandDelete(element);
		cmdList.addCommand(c);
		refreshIHM();
	}
	
	public static void cmdAdd() {
		WindowManager.highlightAll(map, schedule);
	}
	/**
	 * Adds a delivery to the route
	 * 
	 * @param element			the delivery to add
	 */
	public static void cmdAdd2(Delivery element) {
		Command c = new CommandAdd(element);
		cmdList.addCommand(c);
		refreshIHM();
	}

	/**
	 * Modifies a delivery
	 * 
	 * @param element			the delivery to modify
	 * @param startTime			starting time of the modified delivery
	 * @param endTime			ending time of the modified delivery
	 * @param duration			duration of the modified delivery
	 */
	public static void cmdModify(Delivery element, Date startTime, Date endTime, int duration) {
		Command c = new CommandModify(element, startTime, endTime, duration);
		cmdList.addCommand(c);
		refreshIHM();
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
		WindowManager.loadTimeDoughnut(schedule);
	}

	public static void openErrorPopUp(String msg) {
		WindowManager.openErrorPopUp(msg);		
	}

}
