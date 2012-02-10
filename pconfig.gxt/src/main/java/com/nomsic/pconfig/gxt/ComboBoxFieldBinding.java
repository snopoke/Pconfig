package com.nomsic.pconfig.gxt;

import java.util.List;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ComboBoxFieldBinding extends FieldBinding {

	private final ComboBox<BeanModel> comboBox;

	public ComboBoxFieldBinding(ComboBox<BeanModel> field, String property) {
		super(field, property);
		comboBox = field;
	}

	@Override
	protected Object onConvertFieldValue(Object value) {
		List<BeanModel> selection = comboBox.getSelection();
		if (selection == null || selection.isEmpty())
			return null;

		return selection.get(0).getBean();
	}

	@Override
	protected Object onConvertModelValue(Object value) {
		return convertToBeanModel(value);
	}

	private BeanModel convertToBeanModel(Object item) {
		if (item == null) {
			return null;
		} else if (item instanceof BeanModel) {
			return (BeanModel) item;
		}
		BeanModelLookup lookup = BeanModelLookup.get();
		BeanModelFactory factory = lookup.getFactory(item.getClass());
		if (factory == null)
			return null;

		return factory.createModel(item);
	}

}
