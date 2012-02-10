package com.nomsic.pconfig.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Class representing a Date parameter.
 * 
 * @author Simon Kelly
 */
@XmlType(name = "date")
@XmlAccessorType(XmlAccessType.FIELD)
public class DateParameter extends BaseParameter<Date> {

	private static final long serialVersionUID = 5063147282465811918L;

	@XmlAttribute
	private boolean allowPast = true;

	@XmlAttribute
	private boolean allowFuture = true;

	/**
	 * Boolean value to indicate if the user is allowed to select dates in the past.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <date name="start_date" label="Start Date" allowPast="true"/>
	 * }
	 * </pre>
	 */
	public boolean isAllowPast() {
		return allowPast;
	}
	
	public void setAllowPast(boolean allowPast) {
		this.allowPast = allowPast;
	}
	
	/**
	 * Boolean value to indicate if the user is allowed to select dates in the future.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <date name="end_date" label="End Date" allowFuture="true"/>
	 * }
	 * </pre>
	 */
	public boolean isAllowFuture() {
		return allowFuture;
	}
	
	public void setAllowFuture(boolean allowFuture) {
		this.allowFuture = allowFuture;
	}
	
	@Override
	@XmlAttribute
	public Date getValue() {
		return value;
	}
	
	@Override
	public void setValue(Date value) {
		this.value = value;
	}
	
	@Override
	@XmlAttribute
	public Date getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public void setDefaultValue(Date valueIfNull) {
		this.defaultValue = valueIfNull;
	}
}
