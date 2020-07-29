package com.eviware.soapui.model.tree;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.plugins.interfaces.controls.ExternalTreeFilter;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class SoapUIFilteredTreeModel extends SoapUITreeModel {
    private List<ExternalTreeFilter> filters = new ArrayList<>();
    boolean isFilterable = false;

    public SoapUIFilteredTreeModel(Workspace workspace) {
        super(workspace);
    }

    protected boolean shouldSkipNode(ModelItem modelItem) {
        for (ExternalTreeFilter filter : filters) {
            if (filter.shouldSkip(modelItem)) {
                return true;
            }
        }
        return false;
    }

    protected boolean shouldDeleteNode(ModelItem modelItem) {
        for (ExternalTreeFilter filter : filters) {
            if (filter.shouldRemove(modelItem)) {
                return true;
            }
        }
        return false;
    }

    private List<SoapUITreeNode> getFilteredChildren(SoapUITreeNode parent) {
        List<SoapUITreeNode> filteredChildren = new ArrayList<>();
        if (parent == null) {
            return filteredChildren;
        }
        int childrenAmount = parent.getChildCount();
        for (int i = 0; i < childrenAmount; i++) {
            SoapUITreeNode child = parent.getChildNode(i);
            ModelItem modelItem = child.getModelItem();
            if (shouldDeleteNode(modelItem)) {
                continue;
            }
            if (!shouldSkipNode(modelItem)) {
                filteredChildren.add(child);
            } else {
                filteredChildren.addAll(getChildren(child));
            }
        }
        return filteredChildren;
    }

    private List<SoapUITreeNode> getChildren(Object parent) {
        return getFilteredChildren((SoapUITreeNode) parent);
    }

    @Override
    public Object getChild(Object parent, int index) {
        List<SoapUITreeNode> filteredChildren = getChildren(parent);
        if (filteredChildren.isEmpty() || index >= filteredChildren.size()) {
            return null;
        }
        return filteredChildren.get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        int result = getChildren(parent).size();
        return result;
    }

    @Override
    public boolean isLeaf(Object node) {
        boolean result = getChildCount(node) == 0;
        return result;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return child instanceof SoapUITreeNode ? getChildren(parent).indexOf(child) : -1;
    }

    public void addFilter(ExternalTreeFilter treeFilter) {
        filters.add(treeFilter);
        isFilterable = true;
    }

}
