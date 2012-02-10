package com.nomsic.pconfig.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.nomsic.pconfig.util.MapAdapter;

/**
 * Pconfig class represents the configuration descriptor of a parameterised
 * object.
 * 
 * e.g. a report with input parameters may be represented as a Pconfig
 * 
 * @author Simon Kelly
 * 
 */
@XmlRootElement(name = "pconfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pconfig implements Serializable {

	private static final long serialVersionUID = -4554124717963059832L;

	public static final String PROP_LABEL = "label";

	@XmlElement
	private String id;

	@XmlElement
	private String label;

	@XmlElement
	private String resource;

	@XmlElements({ @XmlElement(name = "date", type = DateParameter.class),
			@XmlElement(name = "integer", type = IntegerParameter.class),
			@XmlElement(name = "text", type = StringParameter.class),
			@XmlElement(name = "entity", type = EntityParameter.class),
			@XmlElement(name = "boolean", type = BooleanParameter.class),
			@XmlElement(name = "label", type = LabelParameter.class) })
	@XmlElementWrapper(name = "parameters")
	private List<Parameter<?>> parameters;

	@XmlElement
	@XmlJavaTypeAdapter(MapAdapter.class)
	private Map<String, String> properties;

	public Pconfig() {
	}

	public Pconfig(String id, String label) {
		this.id = id;
		this.label = label;
	}

	/**
	 * The label shown to the user.
	 */
	public String getLabel() {
		return label;
	}

	public void setLabel(String name) {
		this.label = name;
	}

	/**
	 * The path / location to the resource associated with this pconfig
	 * 
	 * e.g. path to a report file
	 */
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * A list of parameters to prompt the user for
	 */
	public List<Parameter<?>> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter<?>> parameters) {
		this.parameters = parameters;
	}

	/**
	 * A set of user defined properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void addProperty(String key, String value) {
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(key, value);
	}

	/**
	 * The internal unique id.
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProperty(String key) {
		if (properties == null) {
			return null;
		}
		return properties.get(key);
	}

	public boolean hasProperty(String propertyName) {
		if (properties == null)
			return false;

		return properties.containsKey(propertyName);
	}

	@Override
	public String toString() {
		return id;
	}

	public void addParameter(Parameter<?> param) {
		if (parameters == null) {
			parameters = new ArrayList<Parameter<?>>();
		}
		parameters.add(param);
	}

	public Parameter<?> getParameter(String name) {
		if (parameters == null) {
			return null;
		}
		for (Parameter<?> p : parameters) {
			if (p.getName().equals(name))
				return p;
		}

		return null;
	}
}
