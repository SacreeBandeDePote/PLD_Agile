package lsbdp.agile;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;

import org.junit.BeforeClass;
import org.junit.Test;

import lsbdp.agile.algorithm.NNHTimeLessCostTSP;
import lsbdp.agile.algorithm.TSP;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.StreetMap;

public class TestSerializer {

	static StreetMap map;
	static File mapFile; 
	static File deliveryFile; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapFile = new File("./Data/fichiersXML/planLyonPetit.xml");		
		deliveryFile = new File("./Data/fichiersXML/DLpetit5.xml");
		map = SerializerXML.deserializeMapXML(mapFile);
	}
	
	@Test
	public void testDeserializeMapXML (){
		map = SerializerXML.deserializeMapXML(mapFile);
		Intersection intersection = map.get((long)0);
		assertEquals(0, intersection.getId());
		assertEquals(27866, intersection.getX());
		assertEquals(42785, intersection.getY());	
	}	
	
	@Test
	public void testDeserializeDeliveryXML (){
		DeliveriesRequest deliveries = SerializerXML.deserializeDeliveryXML(deliveryFile,map);
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
		assertEquals(88, deliveries.getWarehouse().getId());
		assertEquals("08:00:00", formater.format(deliveries.getStartingTime()));
	}
	
	@Test
	public void testSerializeDeliveryXML (){
		TSP test = new NNHTimeLessCostTSP();
		DeliverySchedule s = new DeliverySchedule();
		DeliveriesRequest deliveries = SerializerXML.deserializeDeliveryXML(deliveryFile,map);
		File file = new File("./Data/testSerialize.xml");
		file.deleteOnExit();
		test.findSolution(s, map, deliveries);
		SerializerXML.serializeDeliveryXML(s, file);
		assertTrue(file.exists());
	}
	
}
