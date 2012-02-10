package com.nomsic.pconfig.gxt;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;

public interface EntityStoreProvider {

	public abstract ListStore<ModelData> getEntityStore(
			final String entityName, final String searchField);

}