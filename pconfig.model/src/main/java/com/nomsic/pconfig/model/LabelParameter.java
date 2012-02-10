package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Class representing a Label parameter. Label parameters are used to show
 * informational messages to the user.
 * 
 * @author Simon Kelly
 */
@XmlType(name = "label")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabelParameter extends BaseParameter<String> {

	private static final long serialVersionUID = -4838194392217686037L;

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
