package lsbdp.agile.algorithm;

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;

import java.util.ArrayList;
import java.util.List;

public class GloutonTSP implements TSP {

    @Override
    public void findSolution(DeliverySchedule schedule, Route[][] graphTSP, List<Delivery> list) {
        List<Integer> view = new ArrayList<>();
        Route r = findShortest(graphTSP[graphTSP.length - 1], graphTSP.length - 1, view);
        Delivery d = getDel(list, r.getEnd());
        schedule.add(new Pair<>(r, d));

        int lastView = 0;
        while (view.size() < graphTSP.length - 1) {
            lastView = view.get(view.size() - 1);
            r = findShortest(graphTSP[lastView], lastView, view);
            d = getDel(list, r.getEnd());

            schedule.add(new Pair<>(r, d));
        }

        r = graphTSP[lastView][graphTSP.length - 1];
        d = getDel(list, r.getEnd());
        schedule.add(new Pair<>(r, d));
    }

    private Route findShortest(Route[] line, int current, List<Integer> view) {
        Route r = null;
        int pick = 0;
        for (int i = 0; i < line.length - 1; i++) {
            if (view.contains(i) || i == current) {
                continue;
            }
            if (r == null) {
                r = line[i];
                pick = i;
            } else {
                if (r.getTotalLength() > line[i].getTotalLength()) {
                    r = line[i];
                    pick = i;
                }
            }
        }

        view.add(pick);
        return r;
    }

    private Delivery getDel(List<Delivery> list, Intersection target) {
        for (Delivery del : list) {
            if (del.getLocation().equals(target))
                return del;
        }
        return null;
    }
}
