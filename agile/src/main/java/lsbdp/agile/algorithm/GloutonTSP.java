package lsbdp.agile.algorithm;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;

public class GloutonTSP implements TSP {

	@Override
	public void findSolution(DeliverySchedule schedule, Route[][] graphTSP, List<Delivery> list) {
		List<Integer> view = new ArrayList<>();
		Route r = findShortest(graphTSP[graphTSP.length-1], graphTSP.length-1, view);
        Delivery d = getDel(list, r.getEnd());
        
        schedule.add(new Pair<>(r, d));
        int lastView = 0;
        for (int i = 0; i < graphTSP.length-1 ; i++) {
        	lastView = view.get(view.size()-1);
            r = findShortest(graphTSP[lastView], lastView, view);
            d = getDel(list, r.getEnd());

            schedule.add(new Pair<>(r, d));
        }
        
        r = graphTSP[lastView][graphTSP.length-1];
        d = getDel(list, r.getEnd());
	}
	
	private Route findShortest(Route[] lign, int visited, List<Integer> view) {
		Route r = null;
		int pick = 0;
		for (int i = 0; i < lign.length-1; i++) {
			if(view.contains(i) || i == visited) {
				continue;
			}
			if (r == null) {
				r = lign[i];
				pick = i;
			}
			else {
				if(r.getTotalLength() > lign[i].getTotalLength()) {
					r = lign[i];
					pick = i;
				}
			}
		}
		
		view.add(pick);
		return r;
	}
	
	private Delivery getDel(List<Delivery> list, Intersection target) {
        for(Delivery del : list) {
            if (del.getLocation().equals(target))
                return del;
        }
        return null;
    }
}
