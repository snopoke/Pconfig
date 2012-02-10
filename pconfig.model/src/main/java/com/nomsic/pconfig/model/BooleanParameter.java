package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "boolean")
@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanParameter extends BaseParameter<Boolean> {

	private static final long serialVersionUID = 4410043350849264900L;

	
	public BooleanParameter() {}

	public BooleanParameter(String name, String label) {
		super(name, label);
	}

	@Override
	@XmlAttribute
	public Boolean getValue() {
		return value;
	}
	
	@Override
	public void setValue(Boolean value) {
		this.value = value;
	}
	
	@Override
	@XmlAttribute
	public Boolean getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public void setDefaultValue(Boolean valueIfNull) {
		this.defaultValue = valueIfNull;
	}
}
