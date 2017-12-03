package lsbdp.agile;

import org.junit.BeforeClass;
import org.junit.Test;

import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

public class SerializerTests {
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
		bToD = new Street(10.f, "bToD", d);
		cToD = new Street(10.f, "cToD", d);
		
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
	
}
