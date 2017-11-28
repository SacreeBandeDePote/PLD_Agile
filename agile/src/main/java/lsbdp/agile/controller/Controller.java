package lsbdp.agile.controller;

import java.io.File;
import java.text.ParseException;

import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;

public class Controller {
	private CommandList cmdList;
	
	// Mettre ses méthodes en static
	private SerializerXML serializer;
	public Controller() {
		this.cmdList = new CommandList();
		this.serializer = new SerializerXML();
	}
	public StreetMap addMap(File XML) throws ParseException {
		return serializer.deserializeMapXML(XML);
	}
	public DeliveriesRequest addDeliveriesRequest(File XML) throws ParseException {
		//return serializer.serializeDeliveryXML(XML);
		return null;
	}
	public Route calculateRoute(Delivery start, Delivery end, StreetMap map) {
		// Mettre en static et modifier la méthode
		//Dijkstra dj = new Dijkstra(map);
		//return dj.performDijkstra(start.getLocation(), end.getLocation());
		return null;
	}
	/* On verra ça plus tard
	 * public void loadDeliveryRequest(File XML, DeliverySchedule schedule) throws ParseException {
		DeliveriesRequest dr = serializer.serializeDeliveryXML(XML);
		//schedule = algo.createDeliverySchedule(dr);
	}*/
	public void undo() {
		cmdList.undo();
	}
	public void redo() {
		cmdList.redo();
	}
	
}
