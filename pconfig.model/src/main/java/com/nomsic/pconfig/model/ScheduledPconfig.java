package com.nomsic.pconfig.model;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "spconfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledPconfig implements Serializable {

	private static final long serialVersionUID = 3739675542605140760L;

	public static final String PROP_END_DATE = "endDate";
	public static final String PROP_START_DATE = "startDate";

	@XmlElement
	private String id;

	@XmlElement
	private RepeatInterval repeatInterval;

	@XmlElement
	private int intervalCount;

	@XmlElement
	private Date startDate;

	@XmlElement
	private Date endDate;

	@XmlElement
	private String scheduledFor;

	@XmlElement
	private Pconfig pconfig;

	public ScheduledPconfig() {
	}

	public ScheduledPconfig(Pconfig pconfig) {
		super();
		this.pconfig = pconfig;
	}

	public String getId() {
		return id;
	}

	/**
	 * A unique ID for the scheduled pconfig
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public RepeatInterval getRepeatInterval() {
		return repeatInterval;
	}

	/**
	 * They type of interval to repeat over.
	 * 
	 * @param repeatInterval
	 */
	public void setRepeatInterval(RepeatInterval repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public int getIntervalCount() {
		return intervalCount;
	}

	/**
	 * The number of repeat intervals between schedules.
	 * 
	 * e.g. every 1 week
	 * 
	 * @param intervalCount
	 */
	public void setIntervalCount(int intervalCount) {
		this.intervalCount = intervalCount;
	}

	public Date getStartDate() {
		return startDate;
	}

	/**
	 * The date that the schedule starts
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	/**
	 * The date that the schedule ends
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getScheduledFor() {
		return scheduledFor;
	}

	/**
	 * A generic field to store a reference of who the schedule was created for.
	 * 
	 * e.g. email address
	 * 
	 * @param scheduledFor
	 */
	public void setScheduledFor(String scheduledFor) {
		this.scheduledFor = scheduledFor;
	}

	public Pconfig getPconfig() {
		return pconfig;
	}

	/**
	 * The pconfig that this schedule applies to.
	 * 
	 * @param pconfig
	 */
	public void setPconfig(Pconfig pconfig) {
		this.pconfig = pconfig;
	}
	
	public String getProperty(String key) {
		if (pconfig == null) {
			return null;
		}
		return pconfig.getProperty(key);
	}

	public boolean hasProperty(String propertyName) {
		if (pconfig == null)
			return false;

		return pconfig.hasProperty(propertyName);
	}

}
