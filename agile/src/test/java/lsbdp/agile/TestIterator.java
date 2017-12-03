package lsbdp.agile;

import java.util.ArrayList;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;


import lsbdp.agile.algorithm.LessCostIterator;

public class TestIterator {
	static int crtNode;
	static ArrayList<Integer> nonView;
	static float[][] timeCost;

	@BeforeClass
	public static void setUp() {
		crtNode =0;
		nonView = new ArrayList<>();
		nonView.add(1);
		nonView.add(3);
		nonView.add(4);
		nonView.add(2);
		timeCost = new float[5][5];
		for (int i = 0; i < timeCost.length; i++) {
			float[] line = {0.f, 4.f, 2.f,3.f, 1.f};
			timeCost[i] = line;
		}
	}

	@Test
	public void testIterator(){
		LessCostIterator b = new LessCostIterator(crtNode, nonView, timeCost);
		assertEquals(new Integer(4) , b.next());
		assertEquals(new Integer(2) , b.next());
		assertEquals(new Integer(3) , b.next());
		assertEquals(new Integer(1) , b.next());
	}
	
}
