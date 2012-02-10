package com.nomsic.pconfig.gxt;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.SimpleComboBoxFieldBinding;
import com.extjs.gxt.ui.client.binding.TimeFieldBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TimeField;

public abstract class FormDialog extends Window {

	private Button saveButton;
	private Button cancelButton;

	private Listener<ButtonEvent> saveListener;
	private FormPanel formPanel;
	private FormBinding formBindings;
	
	private boolean hideOnSubmit = true;
	
	protected void buildDialog() {
		// Setup the window
		setWidth(450);
		setPlain(true);
		setModal(true);
		setBlinkModal(true);
		
		getHeader().setId("DialogHeader");

		saveButton = new Button();
		saveButton.setId("saveButton");
		saveButton.setText(Messages.INSTANCE.done());
		
		cancelButton = new Button();
		cancelButton.setId("cancelButton");
		cancelButton.setText(Messages.INSTANCE.cancel());
		
		formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setBodyBorder(false);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		formPanel.setLabelSeparator("");
		formPanel.setLabelWidth(100);

		createFormContents(formPanel);
		
		addButtons();

		createFormBinding();
		createFormButtonBinding();

		addListeners();

		add(formPanel);
	}

	protected void addButtons() {
		formPanel.addButton(saveButton);
		formPanel.addButton(cancelButton);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void createFormBinding() {
		formBindings = new FormBinding(formPanel, false);
		for (Field<?> f : formPanel.getFields()) {
			if (formBindings.getBinding(f) == null) {
				String name = f.getName();
				if (name != null && name.length() > 0) {
					FieldBinding b;
					if (f instanceof TimeField){
						b = new TimeFieldBinding((TimeField) f, name);
					} else if (f instanceof SimpleComboBox){
						b = new SimpleComboBoxFieldBinding((SimpleComboBox) f, name);
					} else if (f instanceof ComboBox){
						b = new ComboBoxFieldBinding((ComboBox) f, name);
					} else {
						b = new FieldBinding(f, name);
					}
					b.setUpdateOriginalValue(true);
					formBindings.addFieldBinding(b);
				}
			}
		}
	}

	protected void createFormButtonBinding() {
		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(saveButton);
	}
	
	protected abstract void createFormContents(FormPanel formPanel);

	private void addListeners() {
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});

		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (hideOnSubmit)
					hide();
			}
		});
	}

	public void bind(BeanModel beanModel){
		setModel(beanModel);
		formBindings.bind(beanModel);
	}

	public void setSaveListener(Listener<ButtonEvent> listener){
		if (this.saveListener != null){
			saveButton.removeListener(Events.Select, this.saveListener);
		}
		this.saveListener = listener;
		saveButton.addListener(Events.Select, listener);
	}
	
	public FormPanel getFormPanel() {
		return formPanel;
	}
	
	public FormBinding getFormBindings() {
		return formBindings;
	}
	
	public Button getSaveButton() {
		return saveButton;
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}
	
	/**
	 * Defaults to true.
	 * 
	 * @param hideOnSubmit
	 */
	public void setHideOnSubmit(boolean hideOnSubmit) {
		this.hideOnSubmit = hideOnSubmit;
	}
}
