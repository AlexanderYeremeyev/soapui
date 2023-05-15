package com.eviware.soapui.ui.navigator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.Releasable;
import com.eviware.soapui.model.tree.SoapUITreeNode;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.swing.ActionList;
import com.eviware.soapui.support.action.swing.ActionListBuilder;

/**
 * @author alexander.yeremeyev
 */
class NavigatorTreeKeyListener extends KeyAdapter implements Releasable {
    private NavigatorTree mainTree;

    NavigatorTreeKeyListener(NavigatorTree mainTree) {
        this.mainTree = mainTree;
    }

    @Override
    public void release() {
        mainTree = null;
    }

    public void keyPressed(KeyEvent e) {
        TreePath selectionPath = mainTree.getSelectionPath();
        if (selectionPath == null || mainTree.getSelectionCount() == 0) {
            return;
        }

        if (mainTree.getSelectionCount() == 1) {
            SoapUITreeNode lastPathComponent = (SoapUITreeNode) selectionPath.getLastPathComponent();
            ActionList actions = lastPathComponent.getActions();
            if (actions != null) {
                actions.dispatchKeyEvent(e);
            }

            if (!e.isConsumed()) {
                KeyStroke ks = KeyStroke.getKeyStrokeForEvent(e);
                if (ks.equals(UISupport.getKeyStroke("alt C"))) {
                    mainTree.collapsePath(selectionPath);
                    e.consume();
                } else if (ks.equals(UISupport.getKeyStroke("alt E"))) {
                    mainTree.collapsePath(selectionPath);
                    int row = mainTree.getSelectionRows()[0];
                    TreePath nextPath = mainTree.getPathForRow(row + 1);

                    TreePath path = mainTree.getPathForRow(row);
                    while (path != null && !path.equals(nextPath)) {
                        mainTree.expandRow(row);
                        path = mainTree.getPathForRow(++row);
                    }

                    e.consume();
                }
            }
        } else {
            TreePath[] selectionPaths = mainTree.getSelectionPaths();
            List<ModelItem> targets = new ArrayList<ModelItem>();
            for (TreePath treePath : selectionPaths) {
                SoapUITreeNode node = (SoapUITreeNode) treePath.getLastPathComponent();
                targets.add(node.getModelItem());
            }

            if (targets.size() > 0) {
                ActionList actions = ActionListBuilder
                        .buildMultiActions(targets.toArray(new ModelItem[targets.size()]));
                if (actions.getActionCount() > 0) {
                    actions.dispatchKeyEvent(e);
                }
            }
        }
    }
}
