package lsbdp.agile.algorithm;

import lsbdp.agile.model.*;

public interface TSP {
    void findSolution(DeliverySchedule schedule, StreetMap map, DeliveriesRequest req);
}
