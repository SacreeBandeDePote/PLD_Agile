package lsbdp.agile.model;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Delivery {
	private int duration;
	private Date timespanStart;
	private Date timespanEnd;
	private Intersection location;
	private Date deliveryTime;
	
	public Delivery(int duration, Date timespanStart, Date timespanEnd, Intersection location, Date deliveryT) {
		this.duration = duration; //temps en secondes
		this.timespanStart = timespanStart;
		this.timespanEnd = timespanEnd;
		this.location = location;
		this.deliveryTime = deliveryT;
	}

	public Intersection getLocation() {
		return location;
	}

	public void setLocation(Intersection location) {
		this.location = location;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getTimespanStart() {
		return timespanStart;
	}

	public Date getDateAfterDuration() {
		Date end = null;
        long duration = TimeUnit.SECONDS.toMillis((long)this.getDuration());
        long newDate = (long)(this.getDeliveryTime().getTime() + duration);
        end = new Date(newDate);
        return end;
	}
	
	public void setTimespanStart(Date timespanStart) {
		this.timespanStart = timespanStart;
	}

	public Date getTimespanEnd() {
		return timespanEnd;
	}

	public void setTimespanEnd(Date timespanEnd) {
		this.timespanEnd = timespanEnd;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
}
