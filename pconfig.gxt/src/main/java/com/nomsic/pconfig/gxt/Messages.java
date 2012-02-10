package com.nomsic.pconfig.gxt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en")
public interface Messages extends com.google.gwt.i18n.client.Messages {

	public static final Messages INSTANCE = GWT.create(Messages.class);

	@Key("label.cancel")
	String cancel();

	@Key("label.done")
	String done();

}