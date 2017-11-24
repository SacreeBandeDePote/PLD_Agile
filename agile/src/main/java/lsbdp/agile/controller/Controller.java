package lsbdp.agile.controller;

import java.io.File;
import java.text.ParseException;

import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.data.SerializeXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

public class Controller {
	private CommandList cmdList;
	
	// Mettre ses méthodes en static
	private SerializeXML serializer;
	public Controller() {
		this.cmdList = new CommandList();
		this.serializer = new SerializeXML();
	}
	public StreetMap addMap(File XML) throws ParseException {
		return serializer.serializeMapXML(XML);
	}
	public DeliveriesRequest addDeliveriesRequest(File XML, StreetMap map) throws ParseException {
		return serializer.serializeDeliveryXML(XML, map);
	}
	public Route calculateRoute(Delivery start, Delivery end, StreetMap map) {
		// Mettre en static et modifier la méthode
		Dijkstra dj = new Dijkstra(map);
		return dj.performDijkstra(start.getLocation(), end.getLocation());
	}
	public DeliverySchedule loadDeliveryRequest(File XML, StreetMap map) throws ParseException {
		DeliveriesRequest dr = serializer.serializeDeliveryXML(XML, map);
		//schedule = algo.createDeliverySchedule(dr);
		return null;
	}
	public void undo() {
		cmdList.undo();
	}
	public void redo() {
		cmdList.redo();
	}
	
}
