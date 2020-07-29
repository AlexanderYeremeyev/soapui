package com.eviware.soapui.ui.navigator;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.tree.SoapUITreeNode;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

class NavigatorTreeExpansionListener implements TreeExpansionListener {
    private NavigatorNodesExpandStateManager navigatorNodesExpandStateManager;

    public NavigatorTreeExpansionListener(NavigatorNodesExpandStateManager navigatorNodesExpandStateManager) {
        this.navigatorNodesExpandStateManager = navigatorNodesExpandStateManager;
    }

    void switchNodeState(TreeExpansionEvent event, boolean expand) {
        TreePath path = event.getPath();
        Object currentNode = path.getLastPathComponent();
        if (currentNode instanceof SoapUITreeNode) {
            SoapUITreeNode soapUITreeNode = (SoapUITreeNode) currentNode;
            ModelItem modelItem = soapUITreeNode.getModelItem();
            navigatorNodesExpandStateManager.setExpandedState(modelItem, expand);
            if (expand) {
                navigatorNodesExpandStateManager.expandLowerTreeNode(soapUITreeNode);
            }
        }
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        switchNodeState(event, true);
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
        switchNodeState(event, false);
    }
}
