package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Class representing a String parameter.
 * 
 * @author Simon Kelly
 */
@XmlType(name = "text")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringParameter extends BaseParameter<String> {

	private static final long serialVersionUID = -4838194392217686037L;

	public static final String TYPE_SMALL = "small";
	public static final String TYPE_MEDIUM = "medium";
	public static final String TYPE_LARGE = "large";

	@XmlAttribute
	private String validator;
	
	@XmlAttribute
	private String regex;

	@XmlAttribute
	private String errorMessage;
	
	@XmlAttribute
	private String displayType;

	public StringParameter() {
	}

	public StringParameter(String name, String label) {
		super(name, label);
	}
	
	public String getValidator() {
		return validator;
	}
	
	/**
	 * The name of a validator to be used. Available validators are
	 * determined by the view implementation.
	 * 
	 * If the validator field is specified the regex and errorMessage fields
	 * will be ignored.
	 * 
	 * @param validator
	 */
	public void setValidator(String validator) {
		this.validator = validator;
	}

	/**
	 * Regular expression that will be used to validate the parameter value.
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <string name="email" label="Email" 
	 * 	regex="^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$" />
	 * }
	 * </pre>
	 */
	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * The error message that should be displayed if the regular expression
	 * validation fails
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <string name="email" label="Email" 
	 * 	regex="^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$" 
	 * 	errorMessage="Please enter a valid email address" />
	 * }
	 * </pre>
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getDisplayType() {
		return displayType;
	}

	/**
	 * The displayType determines how the field is displayed to the user. 
	 * 
	 * Options are:
	 * <li>small (default) : the field will be a normal text field
	 * <li>medium: the field will be a medium sized text box
	 * <li>large: the field will be a large text box
	 * <li>Any other types supported by the display implementation e.g. html
	 * 
	 * <pre>
	 * e.g.
	 * {@code
	 * <string name="template" label="Email template" displayType="html" />
	 * }
	 * </pre>
	 * 
	 * @param displayType
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
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
