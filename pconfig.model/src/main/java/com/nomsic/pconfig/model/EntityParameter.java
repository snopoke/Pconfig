package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Class representing an Entity parameter (i.e. a parameter that must be
 * searched for in a database).
 * 
 * @author Simon Kelly
 */
@XmlType(name = "entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityParameter extends BaseParameter<String> {

	private static final long serialVersionUID = -3748196432728787387L;

	@XmlAttribute
	private String entityClass;

	@XmlAttribute
	private String displayProperty;

	@XmlAttribute
	private String searchFields;

	@XmlAttribute
	private String valueProperty;

	@XmlAttribute
	private String valueType;

	@XmlAttribute
	private String valueLabel;

	@XmlAttribute
	private boolean autofill = false;

	public EntityParameter() {
	}

	public EntityParameter(String name, String label) {
		super(name, label);
	}

	/**
	 * The fully qualified Java class name of the entity.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <entity name="org" label="Organisation" entityClass="my.package.Organisation"/>
	 * }
	 * </pre>
	 */
	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * The name of the Java property to display to the user.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <entity name="org" label="Organisation" entityClass="my.package.Organisation"
	 * 	displayProperty="name"/>
	 * }
	 * </pre>
	 */
	public String getDisplayProperty() {
		return displayProperty;
	}

	public void setDisplayProperty(String displayProperty) {
		this.displayProperty = displayProperty;
	}

	/**
	 * A comma-separated list of Java properties to use when searching for the
	 * entity. If left blank the displayProperty will be used.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <entity name="user" label="User" entityClass="my.package.User"
	 * 	searchFields="name,username"/>
	 * }
	 * </pre>
	 */
	public String getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}

	/**
	 * The name of the Java property of the entity that should used to get the
	 * parameter value.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <entity name="user" label="User" entityClass="my.package.User"
	 * 	searchFields="name,username" valueProperty="id" />
	 * }
	 * </pre>
	 */
	public String getValueProperty() {
		return valueProperty;
	}

	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}

	/**
	 * The Java type of the value. If left blank the default type is
	 * <code>String</code>. The value is converted to the type by calling the
	 * <code>valueOf(String)</code> method of the specified type.
	 * 
	 * Supported types are: <li>Integer <li>Long <li>Double <li>Boolean
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <entity name="user" label="User" entityClass="my.package.User"
	 * 	searchFields="name,username" valueProperty="id" valueType="Integer" />
	 * }
	 * </pre>
	 */
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * Used to store the value of the displayProperty for later display to the
	 * user.
	 */
	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
	}

	/**
	 * If this attribute is set it indicates that the system using the api
	 * should automatically fill the parameter.
	 * 
	 * An example case is a parameter that gets filled with the currently logged
	 * in user
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <entity name="user" entityClass="my.package.User"
	 * 	valueProperty="id" valueType="Integer" 
	 * 	autofill="true" hidden="true" />
	 * }
	 * </pre>
	 */
	public boolean isAutofill() {
		return autofill;
	}

	public void setAutofill(boolean autofill) {
		this.autofill = autofill;
	}

	@Override
	@XmlAttribute
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	@XmlAttribute
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String valueIfNull) {
		this.defaultValue = valueIfNull;
	}
}
