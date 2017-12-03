package lsbdp.agile;

import java.util.ArrayList;
import org.junit.Test;

import javafx.util.Pair;
import lsbdp.agile.algorithm.LessCostIterator;
import lsbdp.agile.algorithm.Heuristique;

public class TestIterator {
	@Test
	public void testIterator(){
		int crtNode =0;
		ArrayList<Integer> nonView = new ArrayList<Integer>();
		nonView.add(1);
		nonView.add(3); 
		nonView.add(4); 
		nonView.add(2);
	    float[][] timeCost = {{0.F, 4.F, 5.F,3.F, 0.F},{0.F, 4.F, 5.F,3.F, 0.F},{0.F, 4.F, 5.F,3.F, 0.F},{0.F, 4.F, 5.F,3.F, 0.F},{0.F, 4.F, 5.F,3.F, 0.F}}  ;
		LessCostIterator b = new LessCostIterator(crtNode, nonView, timeCost);
		/*	
	 	System.out.println(b.next());
		System.out.println(b.next());
		System.out.println(b.next());
		System.out.println(b.next());
		
		*/
	}
	@Test
	public void testHeuristique(){
		int crtNode =3;
		ArrayList<Integer> nonView = new ArrayList<Integer>();
		nonView.add(1);
		nonView.add(4); 
		nonView.add(2); 
		nonView.add(0);
		
	    float[][] timeCost = {{0.F, 1.F, 15.F,17.F, 10.F},
	    					  {1.F, 0.F, 5.F,7.F, 12.F},
	    					  {4.F, 4.F, 0.F,3.F, 13.F},
	    					  {9.F, 4.F, 5.F,0.F, 22.F},
	    					  {33.F, 4.F, 5.F,31.F, 0.F}}  ;
	    Heuristique h = new Heuristique (crtNode, nonView, timeCost);
	    System.out.println(h.Estimation());
	    Pair p1 = new Pair (0.F,4.F);

	    Pair p2 = new Pair (5.F,6.F);
	    Pair<Float, Float>[] timeWindows = new Pair [5];
	
	    System.out.println( h.checkTimePossible(crtNode, nonView, timeWindows));

	}
	
}
