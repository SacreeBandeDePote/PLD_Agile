package lsbdp.agile.controller;

import java.io.File;
import java.text.ParseException;
import java.util.List;

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
	private CommandList cmdList;
	private static StreetMap map;
	private static GloutonTSP algo; 
	private static DeliverySchedule schedule;
	private static DeliveriesRequest deliveries;
	
	// Mettre ses méthodes en static
	public Controller() {
		this.cmdList = new CommandList();
		this.algo = new GloutonTSP();
	}
	
	public StreetMap addMap(File XML) throws ParseException {
		map = SerializeXML.serializeMapXML(XML);
		return map;
	}
	public static StreetMap getMap() {
		return map;
	}
	
	public static DeliveriesRequest addDeliveriesRequest(File XML) throws ParseException {
		//return serializer.serializeDeliveryXML(XML);
		return null;
	}
	
	/*
	public static Route calculateRoute(Delivery start, Delivery end) {
		// Mettre en static et modifier la méthode
		//Dijkstra dj = new Dijkstra(map);
		//return dj.performDijkstra(start.getLocation(), end.getLocation());
		return null;
	}
	*/
	public static DeliverySchedule loadDeliveryRequest(File XML) throws ParseException {
		deliveries = SerializeXML.serializeDeliveryXML(XML, map);
		findDeliverySchedule();
		return schedule;
	}
	
	public static void findDeliverySchedule() {
		 algo.findSolution(schedule, map, deliveries.getWarehouse(), deliveries.getDeliveryList());
	}
	
	
	public static void DrawMap(){
		
	}
	
	public void undo() {
		cmdList.undo();
	}
	public void redo() {
		cmdList.redo();
	}
	
}
