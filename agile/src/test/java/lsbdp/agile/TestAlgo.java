package lsbdp.agile;

import static org.junit.Assert.*;

import lsbdp.agile.algorithm.Dijkstra;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.StreetMap;
import org.junit.BeforeClass;
import org.junit.Test;

import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Street;

import java.util.List;

public class TestAlgo {
	static StreetMap map;

	static Intersection a;
	static Intersection b;
	static Intersection c;
	static Intersection d;
	static Street aToB;
	static Street aToC;
	static Street bToD;
	static Street cToD;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		a = new Intersection(0, 0, 0);
		b = new Intersection(1, 5, 0);
		c = new Intersection(2, 0, 5);
		d = new Intersection(3, 5, 5);
		aToB = new Street(50.f, "aToB", b);
		aToC = new Street(25.f, "aToC", c);
		bToD = new Street(10.f, "BToD", d);
		cToD = new Street(10.f, "DToD", d);
		
		a.addStreet(aToB);
		a.addStreet(aToC);
		b.addStreet(bToD);
		c.addStreet(cToD);

		map = new StreetMap();

		map.put(0l, a);
		map.put(1l, b);
		map.put(2l, c);
		map.put(3l, d);
	}

	@Test
	public void testGetNeighboors() {
		assertEquals(2, a.getNeighbors().size());
		assertEquals(1, b.getNeighbors().size());
	}

	@Test
	public void testDistTo() {
		assertEquals(50.f, a.distTo(b), 0);
	}

	@Test
	public void testGetStreetTo() {
		assertEquals(aToB, a.getStreetTo(b));
		assertEquals(null, b.getStreetTo(a));
	}

	@Test
	public void testDijkstra() {
		Dijkstra dijkstra = new Dijkstra(map);
		Route r = dijkstra.performDijkstra(a, d);

		List<Street> streets = r.getStreets();

		assertEquals(2, streets.size());
		assertEquals(aToC, streets.get(0));
		assertEquals(cToD, streets.get(1));
	}

}
