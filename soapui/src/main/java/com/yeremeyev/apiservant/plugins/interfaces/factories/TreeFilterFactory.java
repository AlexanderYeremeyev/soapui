package com.yeremeyev.apiservant.plugins.interfaces.factories;

import com.yeremeyev.apiservant.plugins.interfaces.controls.common.tree.TreeFilter;
import com.yeremeyev.apiservant.plugins.interfaces.controls.common.tree.TreeType;

import java.util.List;

/**
 * factory to create TreeFilter
 */
public interface TreeFilterFactory {

    /**
     * @return list of supported margins
     */
    List<TreeType> getSupportTypesList();

    /**
     * @param treeType
     * @return true is treeType is supported
     */
    boolean isAvailable(TreeType treeType);

    /**
     * @param treeType
     * @return new created treeType filter
     */
    TreeFilter create(TreeType treeType);
}
