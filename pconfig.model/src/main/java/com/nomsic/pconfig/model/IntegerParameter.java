package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Class representing an Integer parameter.
 * 
 * @author Simon Kelly
 */
@XmlType(name = "number")
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerParameter extends BaseParameter<Integer> {

	private static final long serialVersionUID = 1167318141533475618L;

	@XmlAttribute
	private Integer min;

	@XmlAttribute
	private Integer max;
	
	public IntegerParameter() {
		super();
	}

	public IntegerParameter(String name, String label) {
		super(name, label);
	}

	/**
	 *  Minimum allowed value for the parameter.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <integer name="max_age" label="Maximum age" min="0" />
	 * }
	 * </pre>
	 */
	public Integer getMin() {
		return min;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
	
	/**
	 *  Maximum allowed value for the parameter.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <integer name="max_age" label="Maximum age" max="150" />
	 * }
	 * </pre>
	 */
	public Integer getMax() {
		return max;
	}
	
	public void setMin(Integer min) {
		this.min = min;
	}

	@Override
	@XmlAttribute
	public Integer getValue() {
		return value;
	}
	
	@Override
	public void setValue(Integer value) {
		this.value = value;
	}
	
	@Override
	@XmlAttribute
	public Integer getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public void setDefaultValue(Integer valueIfNull) {
		this.defaultValue = valueIfNull;
	}
	
}
