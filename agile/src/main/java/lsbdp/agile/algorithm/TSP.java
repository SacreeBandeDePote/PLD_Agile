package lsbdp.agile.algorithm;

import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.StreetMap;

/**
 * Describes all the ways to resolve the TSP
 */
public interface TSP {
	/**
	 * @param schedule Must be null and will be created by the method
	 * @param map
	 * @param req
	 */
	void findSolution(DeliverySchedule schedule, StreetMap map, DeliveriesRequest req);
}
