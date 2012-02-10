package com.nomsic.pconfig.gxt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("images/ext-ux-wiz-stepIndicator-off.png")
	public ImageResource wizardStepOff();

	@Source("images/ext-ux-wiz-stepIndicator-on.png")
	public ImageResource wizardStepOn();

}