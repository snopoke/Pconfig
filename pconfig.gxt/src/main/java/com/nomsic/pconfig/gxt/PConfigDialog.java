package com.nomsic.pconfig.gxt;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.nomsic.pconfig.model.Pconfig;

public class PConfigDialog extends FormDialog {
	
	private final Pconfig pconfig;
	private final EntityStoreProvider storeProvider;
	private final boolean viewOnly;

	public PConfigDialog(EntityStoreProvider storeProvider, Pconfig pconfig, boolean viewOnly) {
		this.storeProvider = storeProvider;
		this.pconfig = pconfig;
		this.viewOnly = viewOnly;
		
		buildDialog();
	}
	
	@Override
	protected void createFormContents(FormPanel formPanel) {
		setWidth(500);
		
		formPanel.setFrame(false);
		formPanel.setBorders(false);
		formPanel.setAutoWidth(true);
		
		setHeading("Parameters for: " + pconfig.getLabel());

		PconfigParamterFieldFactory.createFieldsOnForm(pconfig, formPanel,
				storeProvider, viewOnly);
		
		getSaveButton().setText(Messages.INSTANCE.done());
		
		addListener(Events.Resize, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				layout(true);
			}
		});
	}

	public Pconfig getPconfig() {
		return pconfig;
	}
}
