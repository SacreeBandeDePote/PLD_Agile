package lsbdp.agile;

import java.util.ArrayList;
import org.junit.Test;


import lsbdp.agile.algorithm.LessCostIterator;

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
		System.out.println(b.next());
		System.out.println(b.next());
		System.out.println(b.next());
		System.out.println(b.next());
	}
	
}
