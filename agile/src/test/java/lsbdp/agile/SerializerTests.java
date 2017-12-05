package lsbdp.agile;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;

import org.junit.BeforeClass;
import org.junit.Test;

import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.StreetMap;

public class SerializerTests {
	static StreetMap map;
	static File mapFile; 
	static File deliveryFile; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapFile = new File("./Data/fichiersXML/planLyonPetit.xml");		
		deliveryFile = new File("./Data/fichiersXML/DLPetit5.xml");
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
		DeliveriesRequest delivery = SerializerXML.deserializeDeliveryXML(deliveryFile,map);
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
		assertEquals(88, delivery.getWarehouse().getId());
		assertEquals("08:00:00", formater.format(delivery.getStartingTime()));
	}	

}
