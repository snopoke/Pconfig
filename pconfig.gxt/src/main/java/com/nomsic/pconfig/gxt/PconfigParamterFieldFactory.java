package com.nomsic.pconfig.gxt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HtmlEditor;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.nomsic.pconfig.model.BaseParameter;
import com.nomsic.pconfig.model.BooleanParameter;
import com.nomsic.pconfig.model.DateParameter;
import com.nomsic.pconfig.model.EntityParameter;
import com.nomsic.pconfig.model.IntegerParameter;
import com.nomsic.pconfig.model.LabelParameter;
import com.nomsic.pconfig.model.Parameter;
import com.nomsic.pconfig.model.Pconfig;
import com.nomsic.pconfig.model.StringParameter;

public class PconfigParamterFieldFactory {
	
//	private static final String DISPLAY_HTML = "html";

	public static void createFieldsOnForm(Pconfig pconfig, FormPanel formPanel,
			EntityStoreProvider storeProvider, boolean viewOnly) {
		List<? extends Parameter<?>> parameters = pconfig.getParameters();
		if (parameters == null || parameters.isEmpty()) {
			formPanel.add(new LabelField("No parameters required"));
		} else {
			for (final Parameter<?> param : parameters) {
				Field<?> field = null;
				if (!param.isHidden()) {
					field = PconfigParamterFieldFactory.getField(param,
							storeProvider, viewOnly);
					formPanel.add(field,new FormData("-20"));
				}
			}
		}
	}

	public static Field<?> getField(final Parameter<?> param, EntityStoreProvider storeProvider, boolean viewOnly) {
		if (viewOnly){
			Object value = null;
			if (param instanceof StringParameter){
				final StringParameter stringParam = (StringParameter) param;
				value = stringParam.getValue();
			} else if (param instanceof IntegerParameter){
				final IntegerParameter integerParam = (IntegerParameter) param;
				value = integerParam.getValue();
			} else if (param instanceof DateParameter){
				final DateParameter dateParam = (DateParameter) param;
				Date date = dateParam.getValue();
				value = date == null ? "" : DateTimeFormat.getFormat("dd-MM-yyyy").format(date);
			} else if (param instanceof BooleanParameter){
				final BooleanParameter boolParam = (BooleanParameter) param;
				value = boolParam.getValue();
			} else if (param instanceof EntityParameter){
				final EntityParameter entityParam = (EntityParameter) param;
				value = entityParam.getValueLabel();
			}
			
			LabelField field = new LabelField(value == null ? "" : value.toString());
			field.setFieldLabel(param.getLabel());
			return field;
		} else {
		
			if (param instanceof StringParameter){
				final StringParameter stringParam = (StringParameter) param;
				return getStringField(stringParam);
			} else if (param instanceof LabelParameter){
				final LabelParameter labelParam = (LabelParameter) param;
				return getLabelField(labelParam);
			} else if (param instanceof IntegerParameter){
				final IntegerParameter integerParam = (IntegerParameter) param;
				return getIntegerField(integerParam);
			} else if (param instanceof DateParameter){
				final DateParameter dateParam = (DateParameter) param;
				return getDateField(dateParam);
			} else if (param instanceof BooleanParameter){
				final BooleanParameter boolParam = (BooleanParameter) param;
				return getBoolField(boolParam);
			} else if (param instanceof EntityParameter){
				EntityParameter entityParam = (EntityParameter) param;
				return getEntityField(entityParam, storeProvider);
			}
			
			return new LabelField("Unknown parameter type");
		}
	}

	private static Field<?> getLabelField(LabelParameter param) {
		LabelField field = new LabelField(param.getValue() == null ? "" : param.getValue().toString());
		field.setFieldLabel(param.getLabel() == null ? "" : param.getLabel());
		if (param.getTooltip() != null && !param.getTooltip().isEmpty())
			field.setToolTip(param.getTooltip());
		return field;
	}

	private static Field<?> getEntityField(final EntityParameter param, EntityStoreProvider presenter) {
		final String displayProperty = param.getDisplayProperty();
		final String valueProperty = param.getValueProperty();
		String searchFields = param.getSearchFields();
		searchFields = searchFields == null ? displayProperty : searchFields;
		
		final ComboBox<ModelData> field = new ComboBox<ModelData>(); 
		field.setEmptyText("Search by " + searchFields);
		field.setFieldLabel(param.getLabel());
		
		if (param.getTooltip() != null && !param.getTooltip().isEmpty())
			field.setToolTip(param.getTooltip());
		
		field.setDisplayField(displayProperty);  
		field.setAllowBlank(param.isOptional());
		field.setStore(presenter.getEntityStore(param.getEntityClass(), searchFields));  
//	    field.setHideTrigger(true);  
	    field.setForceSelection(true);
	    field.setPageSize(10);
	    field.setMinChars(2);
	    
	    applyDefaultValue(field, param);
	    
	    field.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
					List<ModelData> selOrgList = field.getSelection();
					ModelData bm = selOrgList.get(0);
					
					param.setValue(bm.get(valueProperty).toString());
					Object valueLabel = bm.get(displayProperty);
					param.setValueLabel(valueLabel.toString());
			}
		});
		
		return field;
	}

	private static void applyDefaultValue(ComboBox<ModelData> field, final EntityParameter param) {
		String value = param.getValue();
		String defaultValue = param.getDefaultValue();
		
		List<ModelData> selection = new ArrayList<ModelData>();
		
		if (value != null) {
			ModelData model = getBeanModel(param, value);
			selection.add(model);
			field.setSelection(selection);
		} else if (defaultValue != null){
			param.setValue(defaultValue);
		} else {
			field.clear();
		}
		
	}

	private static ModelData getBeanModel(EntityParameter param, String value) {
		ModelData model = new BaseModelData();
		model.set(param.getDisplayProperty(), param.getValueLabel());
		model.set(param.getValueProperty(), value);
		return model;
	}

	/**
	 * @param param
	 * @return
	 */
	private static Field<?> getBoolField(final BooleanParameter param) {
		final CheckBox field = new CheckBox();
		field.setFieldLabel(param.getLabel());

		if (param.getTooltip() != null && !param.getTooltip().isEmpty())
			field.setToolTip(param.getTooltip());
		
		field.setName(BaseParameter.PROP_VALUE);
		
		field.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				param.setValue(field.getValue());
			}
		});
		
		applyDefaultValue(param, field);
		return field;
	}

	/**
	 * @param param
	 * @return
	 */
	private static Field<?> getDateField(final DateParameter param) {
		final DateField field = new DateField();
		field.setFieldLabel(param.getLabel());
		field.setName(BaseParameter.PROP_VALUE);
		field.setAllowBlank(param.isOptional());

		if (param.getTooltip() != null && !param.getTooltip().isEmpty())
			field.setToolTip(param.getTooltip());
		
		if (param.isAllowFuture() && !param.isAllowPast()){
			field.setMinValue(new Date());
		} else if (!param.isAllowFuture() && param.isAllowPast()){
			field.setMaxValue(new Date());
		}
		
		applyDefaultValue(param, field);
		
		field.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				param.setValue(field.getValue());
			}
		});
		
		return field;
	}

	/**
	 * @param param
	 * @param field
	 */
	private static <T> void applyDefaultValue(final Parameter<T> param,
			final Field<T> field) {
		T value = param.getValue();
		T defaultValue = param.getDefaultValue();
		if (value != null) {
			field.setValue(value);
		} else if (defaultValue != null){
			field.setValue(defaultValue);
			param.setValue(defaultValue);
		} else {
			field.clear();
		}
	}

	/**
	 * @param param
	 * @return
	 */
	private static Field<?> getIntegerField(final IntegerParameter param) {
		final NumberField field = new NumberField();
		field.setFieldLabel(param.getLabel());
		field.setName(BaseParameter.PROP_VALUE);
		field.setAllowBlank(param.isOptional());
		field.setPropertyEditorType(Integer.class);
		
		if (param.getTooltip() != null && !param.getTooltip().isEmpty())
			field.setToolTip(param.getTooltip());
		
		if (param.getMax() != null){
			field.setMaxValue(param.getMax());
		}
		if (param.getMin() != null){
			field.setMinValue(param.getMin());
		}
		
		Integer value = param.getValue();
		Integer defaultValue = param.getDefaultValue();
		if (value != null){
			field.setValue(value);
		} else if (defaultValue != null){
			field.setValue(defaultValue);
			param.setValue(defaultValue);
		}
		
		field.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				param.setValue(field.getValue().intValue());
			}
		});
		
		return field;
	}

	/**
	 * @param param
	 * @param param
	 * @return
	 */
	private static Field<?> getStringField(final StringParameter param) {
		String displayType = param.getDisplayType();
		if (displayType != null){
			// currently disabled since binding is tricky
			/*if (displayType.equals(DISPLAY_HTML)){
				return getHtmlField(param);
			}*/
		}
			
		
		final TextField<String> field = new TextField<String>();
		field.setFieldLabel(param.getLabel());
		field.setName(BaseParameter.PROP_VALUE);
		field.setAllowBlank(param.isOptional());

		if (param.getTooltip() != null && !param.getTooltip().isEmpty()) {
			field.setToolTip(param.getTooltip());
		}
		
		if (param.getValidator() != null) {
			// TODO: implement custom validatros
		} else if (param.getRegex() != null && !param.getRegex().isEmpty()) {
			field.setRegex(param.getRegex());
			TextField<String>.TextFieldMessages messages = field.getMessages();
			messages.setRegexText(param.getErrorMessage());
		}
		
		if (displayType != null){
			if (displayType.equals(StringParameter.TYPE_MEDIUM)){
				field.setHeight(100);
			} else if (displayType.equals(StringParameter.TYPE_LARGE)){
				field.setHeight(200);
			}
		}
		
		applyDefaultValue(param, field);
		
		field.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				param.setValue(field.getValue());
			}
		});
		return field;
	}

	/**
	 * FIXME: unable to add listener to HtmlEditor to do binding.
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Field<?> getHtmlField(final StringParameter param) {
		final HtmlEditor field = new HtmlEditor();
		field.setFieldLabel(param.getLabel());
		
		applyDefaultValue(param, field);
		
		field.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				param.setValue(field.getRawValue());
			}
		});
		
		return field;
	}
}
