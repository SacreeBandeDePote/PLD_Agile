package lsbdp.agile;

import lsbdp.agile.algorithm.*;
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
		File f = new File("./Data/fichiersXML/planLyonGrand.xml");
		map = SerializerXML.deserializeMapXML(f);

		f = new File("./Data/fichiersXML/DLgrand10TW2.xml");
		req = SerializerXML.deserializeDeliveryXML(f, map);
	}

	@Test
	public void testDijkstra() {
		Dijkstra.performDijkstra(map, map.get(1L), map.get(20L));
	}

	@Test
	public void testDijkstraComplete() {
		Dijkstra.createTSPGraph(map, req.getWarehouse(), req.getDeliveryList());
	}

	@Test
	public void testNaiveTSP() {
		TSP test = new NaiveTSP();
		DeliverySchedule s = new DeliverySchedule();

		long start = System.currentTimeMillis();
		test.findSolution(s, map, req);
		System.out.println(System.currentTimeMillis() - start);
	}

	@Test
	public void testNaiveLessCostTSP() {
		TSP test = new NaiveLessCostTSP();
		DeliverySchedule s = new DeliverySchedule();

		long start = System.currentTimeMillis();
		test.findSolution(s, map, req);
		System.out.println(System.currentTimeMillis() - start);
	}

	@Test
	public void testShortestTSP() {
		TSP test = new ShortestTSP();
		DeliverySchedule s = new DeliverySchedule();

		long start = System.currentTimeMillis();
		test.findSolution(s, map, req);
		System.out.println(System.currentTimeMillis() - start);
	}

	@Test
	public void testShortestLessCostTSP() {
		TSP test = new ShortestLessCostTSP();
		DeliverySchedule s = new DeliverySchedule();

		long start = System.currentTimeMillis();
		test.findSolution(s, map, req);
		System.out.println(System.currentTimeMillis() - start);
	}
	
	@Test
	public void testNNHTimeTSP() {
		TSP test = new NNHTimeTSP();
		DeliverySchedule s = new DeliverySchedule();

		long start = System.currentTimeMillis();
		test.findSolution(s, map, req);
		System.out.println(System.currentTimeMillis() - start);
	}
	
}
