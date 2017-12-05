package lsbdp.agile;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import javafx.util.Pair;
import lsbdp.agile.algorithm.LessCostIterator;

public class TestIterator {
	static int crtNode;
	static ArrayList<Integer> nonView;
	static float[][] timeCost;

	@BeforeClass
	public static void setUp() {
		crtNode = 0;
		nonView = new ArrayList<>();
		nonView.add(1);
		nonView.add(3);
		nonView.add(4);
		nonView.add(2);
		timeCost = new float[5][5];
		for (int i = 0; i < timeCost.length; i++) {
			float[] line = {0.f, 4.f, 2.f, 3.f, 1.f};
			timeCost[i] = line;
		}
	}

	@Test
	public void testIterator() {
		LessCostIterator b = new LessCostIterator(crtNode, nonView, timeCost);
		assertEquals(new Integer(4), b.next());
		assertEquals(new Integer(2), b.next());
		assertEquals(new Integer(3), b.next());
		assertEquals(new Integer(1), b.next());
	}

//	@Test
//	public void testHeuristique() {
//		int crtNode = 3;
//		ArrayList<Integer> nonView = new ArrayList<Integer>();
//		nonView.add(1);
//		nonView.add(4);
//		nonView.add(2);
//		nonView.add(0);
//
//		float[][] timeCost = {{0.F, 1.F, 15.F, 17.F, 10.F},
//				{1.F, 0.F, 5.F, 7.F, 12.F},
//				{4.F, 4.F, 0.F, 3.F, 13.F},
//				{9.F, 4.F, 5.F, 0.F, 22.F},
//				{33.F, 4.F, 5.F, 31.F, 0.F}};
//		Heuristique h = new Heuristique(crtNode, nonView, timeCost);
//		System.out.println(h.Estimation());
//		Pair p1 = new Pair(0.F, 4.F);
//
//		Pair p2 = new Pair(5.F, 6.F);
//		Pair<Float, Float>[] timeWindows = new Pair[5];
//
//		System.out.println(h.checkTimePossible(crtNode, nonView, timeWindows));
//	}

}
