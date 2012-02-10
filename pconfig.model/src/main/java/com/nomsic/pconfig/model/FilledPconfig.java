package com.nomsic.pconfig.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fpconfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilledPconfig implements Serializable {

	private static final long serialVersionUID = -5094568678277857703L;
	
	public static final String PROP_PCONFIG = "pconfig";
	public static final String PROP_DATE_FILLED = "dateFilled";

	@XmlElement
	private String id;
	
	@XmlElement
	private String resourceType;
	
	@XmlElement
	private String resourcePath;
	
	@XmlElement
	private Date dateFilled;
	
	@XmlElement
	private Pconfig pconfig;
	
	public FilledPconfig() {
	}

	public FilledPconfig(String id, String resourceType, Date dateFilled,
			Pconfig pconfig) {
		super();
		this.id = id;
		this.resourceType = resourceType;
		this.dateFilled = dateFilled;
		this.pconfig = pconfig;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Date getDateFilled() {
		return dateFilled;
	}

	public void setDateFilled(Date dateFilled) {
		this.dateFilled = dateFilled;
	}

	public Pconfig getPconfig() {
		return pconfig;
	}

	public void setPconfig(Pconfig pconfig) {
		this.pconfig = pconfig;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getResourcePath() {
		return resourcePath;
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
