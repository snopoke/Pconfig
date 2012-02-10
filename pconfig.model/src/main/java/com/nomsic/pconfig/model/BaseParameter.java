package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The base class for all Pconfig parameters.
 * 
 * @author Simon Kelly
 *
 * @param <T>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseParameter<T> implements Parameter<T> {

	private static final long serialVersionUID = 786600413519431974L;

	public static final String PROP_VALUE = "value";

	@XmlAttribute
	private String name = "";

	@XmlAttribute
	private String label;
	
	@XmlAttribute
	private String tooltip;
	
	@XmlAttribute
	private boolean optional = false;

	@XmlAttribute
	private boolean hidden;

	@XmlTransient
	protected T defaultValue;

	@XmlTransient
	protected T value;
	
	public BaseParameter() {}

	public BaseParameter(String name, String label) {
		this.name = name;
		this.label = label;
	}

	/**
	 * The name of the parameter.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <date name="start_date" label="Start Date"/>
	 * }
	 * </pre>
	 */
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The label for the parameter to display when prompting the user.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <date name="start_date" label="Start Date"/>
	 * }
	 * </pre>
	 */
	@Override
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * The tooltip for the parameter.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <date name="start_date" label="Start Date" tooltip="The start of the reporting period"/>
	 * }
	 * </pre>
	 */
	@Override
	public String getTooltip() {
		return tooltip;
	}
	
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Is the parameter optional?
	 * <p>
	 * Defaults to false.
	 *
	 * <pre>
	 * e.g.
	 * {@code
	 * <boolean name="include_others" label="Include others?" optional="true"/>
	 * }
	 * </pre>
	 */
	@Override
	public boolean isOptional() {
		return optional;
	}
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	
	/**
	 * Should the parameter be hidden when prompting the user. This can be used when
	 * you want to pass static values to a report via a parameter.
	 * <p>
	 * Defaults to false.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <string name="system_prop" defatulValue="%" hidden="true"/>
	 * }
	 * </pre>
	 */
	@Override
	public boolean isHidden() {
		return hidden;
	}
	
	/**
	 * The default value for the parameter.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <integer name="min_age" label="Minimum age" defaultValue="18"/>
	 * }
	 * </pre>
	 */
	@Override
	public abstract T getDefaultValue();
	
	@Override
	public abstract void setDefaultValue(T valueIfNull);
	
	/**
	 * The value for the parameter supplied by the user.
	 */
	@Override
	public abstract T getValue();
	
	@Override
	public abstract void setValue(T value);
	
	@Override
	public String toString() {
		return name + "[" + value + "]";
	}
	
}
