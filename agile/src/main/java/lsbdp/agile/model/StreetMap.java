package lsbdp.agile.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StreetMap extends HashMap<Long,Intersection> {
    public StreetMap() {
        super();
    }
    
    public int getMaxY() {
		int maxX = 0;
		Set<Long> keys = this.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = this.get(key);
			if(maxX < intersection.getY()) {
				maxX = intersection.getY();
			}
		}
		return maxX+5;
	}

	public int getMinY() {
		int minX = 100000000;
		Set<Long> keys = this.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = this.get(key);
			if(minX > intersection.getY()) {
				minX = intersection.getY();
			}
		}
		return minX+5;
	}

	public int getMaxX() {
		int maxX = 0;
		Set<Long> keys = this.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = this.get(key);
			if(maxX < intersection.getX()) {
				maxX = intersection.getX();
			}
		}
		return maxX+5;
	}

	public int getMinX() {
		int minX = 100000000;
		Set<Long> keys = this.keySet();
		Iterator<Long> iterator = keys.iterator();
		while(iterator.hasNext()) {
			Long key = (Long) iterator.next();
			Intersection intersection = this.get(key);
			if(minX > intersection.getX()) {
				minX = intersection.getX();
			}
		}
		return minX-5;
	}
}
