package com.eviware.soapui.plugins.interfaces.controls;

import com.eviware.soapui.model.ModelItem;

public interface ExternalTreeFilter {

    /**
     * @return type of tree for which filter will be implemented.
     */
    TreeType getTreeType();

    /**
     * @param modelItem tree node item
     * @return boolean value which show skip or show current node.
     * if "node" is skipped then all his children will be inserted instead of current node.
     */
    boolean shouldSkip(ModelItem modelItem);

    /**
     * @param modelItem tree node item
     * @return boolean value which show remove or show current node.
     * if "node" is removed then all his children also will be removed.
     */
    boolean shouldRemove(ModelItem modelItem);
}
