package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;

import java.util.List;

public class StupidTSP implements TSP{

    @Override
    public void findSolution(DeliverySchedule schedule, Route[][] graphTSP, List<Delivery> list) {
        Route r = graphTSP[graphTSP.length-1][0];
        Delivery d = getDel(list, r.getEnd());

        schedule.add(new Pair<>(r, d));

        for (int i = 0; i < graphTSP.length-1 ; i++) {
            Route r1 = graphTSP[i][i + 1];
            Delivery d1 = getDel(list, r.getEnd());

            schedule.add(new Pair<>(r1, d1));
        }
    }

    private Delivery getDel(List<Delivery> list, Intersection target) {
        for(Delivery del : list) {
            if (del.getLocation().equals(target))
                return del;
        }
        return null;
    }
}
