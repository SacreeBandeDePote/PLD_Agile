package lsbdp.agile.algorithm;

import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Route;

import java.util.List;

public interface TSP {
    public void findSolution(DeliverySchedule schedule, Route[][] graphTSP, List<Delivery> list);
}
