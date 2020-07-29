package com.eviware.soapui.ui.navigator;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.tree.SoapUITreeModel;
import com.eviware.soapui.model.tree.SoapUITreeNode;
import com.eviware.soapui.ui.UIManager;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

class NavigatorNodesExpandStateManager {
    private JTree jTree;
    private NavigatorNodesExpandStateProvider navigatorNodesExpandStateProvider;
    private NavigatorTreeExpansionListener treeExpansionListener;

    void expandLowerTreeNode(SoapUITreeNode currentNode, SoapUITreeModel soapUITreeModel) {
        ModelItem modelItem = currentNode.getModelItem();
        boolean isExpanded = navigatorNodesExpandStateProvider.isExpanded(modelItem);
        if (!isExpanded) {
            return;
        }
        TreePath treePath = soapUITreeModel.getPath(currentNode);
        jTree.expandPath(treePath);

        int childCount = currentNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            expandLowerTreeNode(currentNode.getChildNode(i), soapUITreeModel);
        }
    }

    void expandLowerTreeNode(SoapUITreeNode currentNode) {
        SoapUITreeModel soapUITreeModel = (SoapUITreeModel) jTree.getModel();
        expandLowerTreeNode(currentNode, soapUITreeModel);
    }

    private void restoreNodeExpansion(JTree jTree) {
        TreeModel treeModel = jTree.getModel();
        // second condition needed for internal function.
        if (treeModel == null || !(treeModel instanceof SoapUITreeModel)) {
            return;
        }
        SoapUITreeModel soapUITreeModel = (SoapUITreeModel) treeModel;
        Object rootObject = treeModel.getRoot();
        if (rootObject == null || !(rootObject instanceof SoapUITreeNode)) {
            return;
        }
        SoapUITreeNode rootNode = (SoapUITreeNode) rootObject;
        int childNodesAmount = rootNode.getChildCount();
        for (int i = 0; i < childNodesAmount; i++) {
            expandLowerTreeNode(rootNode.getChildNode(i), soapUITreeModel);
        }
    }

    public NavigatorNodesExpandStateManager() {
    }

    public void initialize(JTree jTree) {
        navigatorNodesExpandStateProvider = UIManager.getNavigatorNodesExpandStateProvider();
        if (navigatorNodesExpandStateProvider == null) {
            return;
        }
        this.jTree = jTree;
        restoreNodeExpansion(jTree);
        treeExpansionListener = new NavigatorTreeExpansionListener(this);
        jTree.addTreeExpansionListener(treeExpansionListener);
    }

    void setExpandedState(ModelItem modelItem, boolean expand) {
        navigatorNodesExpandStateProvider.setExpandedState(modelItem, expand);
    }
}
