package lsbdp.agile.model;

import java.sql.Time;

public class Delivery {
	private int duration;
	private Time timespanStart;
	private Time timespanEnd;
	private Intersection location;
	
	public Delivery(int duration, Time timespanStart, Time timespanEnd, Intersection location) {
		this.duration = duration;
		this.timespanStart = timespanStart;
		this.timespanEnd = timespanEnd;
		this.location = location;
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

	public Time getTimespanStart() {
		return timespanStart;
	}

	public void setTimespanStart(Time timespanStart) {
		this.timespanStart = timespanStart;
	}

	public Time getTimespanEnd() {
		return timespanEnd;
	}

	public void setTimespanEnd(Time timespanEnd) {
		this.timespanEnd = timespanEnd;
	}
}
