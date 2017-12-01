package lsbdp.agile;

import lsbdp.agile.algorithm.TSP;
import lsbdp.agile.algorithm.TemplateTSP;
import lsbdp.agile.data.SerializerXML;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.StreetMap;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class TestTSP {
	static StreetMap map;
	static DeliveriesRequest req;

	@BeforeClass
	public static void setUp() {
		File f = new File("./Data/fichiersXML/planLyonMoyen.xml");
		map = SerializerXML.deserializeMapXML(f);

		f = new File("./Data/fichiersXML/DLmoyen5.xml");
		req = SerializerXML.deserializeDeliveryXML(f, map);
	}

	@Test
	public void testInit() {

	}

	@Test
	public void tesTSP() {
		TSP test = new TemplateTSP();
		DeliverySchedule s = new DeliverySchedule();

		test.findSolution(s, map, req);
	}
}
