package lsbdp.agile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import lsbdp.agile.algorithm.NNHTimeLessCostTSP;
import lsbdp.agile.algorithm.NNHTimeTSP;
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
	static File deliveryFileExpected;
	static File deliveryFileTest;
	static File roadMapFileExpected;
	static File roadMapFileTest;
	static TSP tsp;
	static DeliverySchedule deliverySchedule;
	static DeliveriesRequest deliveriesRequest;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapFile = new File("./Data/fichiersXML/planLyonPetit.xml");		
		deliveryFile = new File("./Data/fichiersXML/DLpetit5.xml");
		deliveryFileExpected = new File("./Data/fichiersXML/tests/testDL.xml");
		deliveryFileTest = new File("./Data/fichiersXML/tests/test.xml");
		deliveryFileTest.deleteOnExit();
		roadMapFileExpected = new File("./Data/fichiersXML/tests/testRoadMap.txt");
		roadMapFileTest = new File("./Data/fichiersXML/tests/test.txt");
		roadMapFileTest.deleteOnExit();
		
		map = SerializerXML.deserializeMapXML(mapFile);
		tsp = new NNHTimeTSP();
		deliverySchedule = new DeliverySchedule();
		deliveriesRequest = SerializerXML.deserializeDeliveryXML(deliveryFile,map);
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
	public void testSerializeDeliveryXML () throws IOException{
		tsp.findSolution(deliverySchedule, map, deliveriesRequest);
		SerializerXML.serializeDeliveryXML(deliverySchedule, deliveryFileTest);
		assertTrue(deliveryFileTest.exists());
		assertTrue("The files differ!", FileUtils.contentEquals(deliveryFileTest,deliveryFileExpected));
	}
	
	@Test
	public void testGenerateRoadMap() throws IOException{
		tsp.findSolution(deliverySchedule, map, deliveriesRequest);
		SerializerXML.generateRoadMap(roadMapFileTest, deliverySchedule);
		assertTrue(deliveryFileTest.exists());
		assertTrue("The files differ!", FileUtils.contentEquals(deliveryFileTest, deliveryFileExpected));
	}
}
