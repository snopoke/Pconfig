package com.nomsic.pconfig.model;

import java.io.Serializable;

public interface Parameter<T> extends Serializable{
	
	public void setValue(T value);
	
	public T getValue();

	boolean isHidden();

	boolean isOptional();

	String getLabel();

	String getName();

	T getDefaultValue();

	void setDefaultValue(T valueIfNull);

	String getTooltip();

}
