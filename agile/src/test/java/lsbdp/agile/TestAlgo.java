package lsbdp.agile;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Street;

public class TestAlgo {
	static Intersection start;
	static Intersection end;
	static Street link;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		start = new Intersection(0, 0, 0);
		end = new Intersection(1, 5, 5);
		link = new Street(50, "link", end);
		
		start.addStreet(link);
		
	}

	@Test
	public void testGetNeighboors() {
		assertEquals(1, start.getNeighbors().size());
		assertEquals(0, end.getNeighbors().size());
	}

}
