package com.nomsic.pconfig.gxt;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Timer;

public class DisableDoubleClick implements ComponentPlugin {

	private final int delayMillis;
	private boolean disableRequested = false;

	public DisableDoubleClick() {
		delayMillis = 500;
	}

	public DisableDoubleClick(int delayMillis) {
		this.delayMillis = delayMillis;
	}

	@Override
	public void init(Component component) {
		final Button button = (Button) component;
		button.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				button.disable();
				final Timer t = new Timer() {
					@Override
					public void run() {
						if (!disableRequested) {
							button.enable();
						}
						disableRequested = false;
					}
				};
				disableRequested = false;
				t.schedule(delayMillis);
			}
		});

		button.addListener(Events.Disable,  new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				disableRequested = true;
			}
		});
	}
}
