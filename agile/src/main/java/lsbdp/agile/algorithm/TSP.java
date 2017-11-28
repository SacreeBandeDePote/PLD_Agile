package lsbdp.agile.algorithm;

import lsbdp.agile.model.*;

import java.util.List;

public interface TSP {
    public void findSolution(DeliverySchedule schedule, StreetMap map, Intersection warehouse, List<Delivery> deliveries);
}
