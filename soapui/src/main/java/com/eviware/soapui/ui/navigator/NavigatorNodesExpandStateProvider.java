package com.eviware.soapui.ui.navigator;

import com.eviware.soapui.model.ModelItem;

/**
 * interface to manage navigator nodes state.
 * only single instance is available to work.
 * Do not use this provider to perfect performance.
 * Provider contains needed functions to work.
 * How to use these function and what to do with data depends on implementation.
 */
public interface NavigatorNodesExpandStateProvider {

    void setExpandedState(ModelItem modelItem, boolean expanded);

    boolean isExpanded(ModelItem modelItem);
}
