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
	private SerializeXML serializer;
	public Controller() {
		this.cmdList = new CommandList();
		this.serializer = new SerializeXML();
	}
	public void addMap(File XML, StreetMap map) throws ParseException {
		map = serializer.serializeMapXML(XML);
	}
	public void addDeliveriesRequest(File XML, DeliveriesRequest dr) throws ParseException {
		dr = serializer.serializeDeliveryXML(XML);
	}
	public void calculateRoute(Delivery start, Delivery end, StreetMap map, Route route) {
		Dijkstra dj = new Dijkstra(map);
		route = dj.performDijkstra(start.getLocation(), end.getLocation());
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
