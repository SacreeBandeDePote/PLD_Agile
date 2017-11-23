package lsbdp.agile.controller;

import java.io.File;
import java.text.ParseException;

import lsbdp.agile.data.SerializeXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.StreetMap;

public class Controller {
	private CommandList cmdList;
	private SerializeXML serializer;
	public Controller() {
	}
	public void addMap(File XML, StreetMap map) throws ParseException {
		map = serializer.serializeMapXML(XML);
	}
	public void loadDeliveryRequest(File XML, DeliverySchedule schedule) throws ParseException {
		DeliveriesRequest dr = serializer.serializeDeliveryXML(XML);
		//schedule = algo.createDeliverySchedule(dr);
	}
	public void undo() {
		cmdList.undo();
	}
	public void redo() {
		cmdList.redo();
	}
	
}
