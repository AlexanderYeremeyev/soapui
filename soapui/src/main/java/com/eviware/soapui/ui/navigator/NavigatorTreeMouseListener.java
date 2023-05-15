package com.eviware.soapui.ui.navigator;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.Releasable;
import com.eviware.soapui.model.tree.SoapUITreeNode;
import com.eviware.soapui.settings.UISettings;
import com.eviware.soapui.support.action.swing.ActionList;
import com.eviware.soapui.support.action.swing.ActionListBuilder;
import com.eviware.soapui.support.action.swing.ActionSupport;

/**
 * @author alexander.yeremeyev
 */
class NavigatorTreeMouseListener extends MouseAdapter implements Releasable {
    private NavigatorTree navigatorTree;

    NavigatorTreeMouseListener(NavigatorTree navigatorTree) {
        this.navigatorTree = navigatorTree;
    }

    @Override
    public void release() {
        navigatorTree = null;
    }

    private final class CollapseRowAction extends AbstractAction {
        private final int row;

        public CollapseRowAction(int row) {
            this.row = row;
        }

        public void actionPerformed(ActionEvent e) {
            collapseAll(navigatorTree.getPathForRow(row));
            navigatorTree.collapseRow(row);
        }

        private void collapseAll(TreePath tp) {
            if (tp == null) {
                return;
            }

            Object node = tp.getLastPathComponent();
            TreeModel model = navigatorTree.getModel();
            if (!model.isLeaf(node)) {
                navigatorTree.collapsePath(tp);
                for (int i = 0; i < model.getChildCount(node); i++) {
                    // for (int i = node.childCount()-4;i>=0;i--){
                    collapseAll(tp.pathByAddingChild(model.getChild(node, i)));
                }
                navigatorTree.collapsePath(tp);
            }
        }
    }

    private final class ExpandRowAction extends AbstractAction {
        private final int row;

        public ExpandRowAction(int row) {
            this.row = row;
        }

        public void actionPerformed(ActionEvent e) {
            navigatorTree.expandRow(row);
            expandAll(navigatorTree.getPathForRow(row));
        }

        private void expandAll(TreePath tp) {
            if (tp == null) {
                return;
            }

            Object node = tp.getLastPathComponent();
            TreeModel model = navigatorTree.getModel();
            if (!model.isLeaf(node)) {
                navigatorTree.expandPath(tp);
                for (int i = 0; i < model.getChildCount(node); i++) {
                    expandAll(tp.pathByAddingChild(model.getChild(node, i)));
                }
            }
        }

    }

    private ActionList actions;

    private boolean isSingleDesktopMode() {
        String desktopType = SoapUI.getSoapUICore().getSettings().getString(UISettings.DESKTOP_TYPE, SoapUI.DEFAULT_DESKTOP);
        return desktopType.equals(SoapUI.SINGLE_DESKTOP);
    }

    private void callDefaultAction(MouseEvent mouseEvent) {
        if (navigatorTree.getSelectionCount() == 1) {
            int row = navigatorTree.getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
            TreePath path = navigatorTree.getSelectionPath();
            if (path == null && row == -1) {
                return;
            }

            if (path == null || navigatorTree.getRowForPath(path) != row) {
                navigatorTree.setSelectionRow(row);
            }

            SoapUITreeNode node = (SoapUITreeNode) path.getLastPathComponent();
            actions = node.getActions();
            if (actions != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (actions != null) {
                            actions.performDefaultAction(new ActionEvent(navigatorTree, 0, null));
                            actions = null;
                        }
                    }
                });
            }
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            showPopup(mouseEvent);
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            showPopup(mouseEvent);
        }
        if (mouseEvent.getClickCount() < 2 && !isSingleDesktopMode()) {
            return;
        }
        callDefaultAction(mouseEvent);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopup(e);
        }
    }

    private void showToolTipLessPopupMenu(JPopupMenu pm, int x, int y) {
        pm.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                ToolTipManager.sharedInstance().setEnabled(true);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                ToolTipManager.sharedInstance().setEnabled(true);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                ToolTipManager.sharedInstance().setEnabled(false);
            }
        });
        pm.show(navigatorTree, x, y);
    }

    private void showPopup(MouseEvent e) {
        if (navigatorTree.getSelectionCount() < 2) {
            TreePath path = navigatorTree.getPathForLocation((int) e.getPoint().getX(), (int) e.getPoint().getY());
            if (path == null) {
                int row = (int) e.getPoint().getY() / navigatorTree.getRowHeight();
                if (row != -1) {
                    JPopupMenu collapsePopup = new JPopupMenu();
                    collapsePopup.add("Collapse").addActionListener(new CollapseRowAction(row));
                    collapsePopup.add("Expand").addActionListener(new ExpandRowAction(row));
                    showToolTipLessPopupMenu(collapsePopup, e.getX(), e.getY());
                }

                return;
            }
            SoapUITreeNode node = (SoapUITreeNode) path.getLastPathComponent();

            JPopupMenu popupMenu = node.getPopup();
            if (popupMenu == null) {
                return;
            }

            navigatorTree.setSelectionPath(path);

            showToolTipLessPopupMenu(popupMenu, e.getX(), e.getY());
        } else {
            TreePath[] selectionPaths = navigatorTree.getSelectionPaths();
            List<ModelItem> targets = new ArrayList<ModelItem>();
            for (TreePath treePath : selectionPaths) {
                SoapUITreeNode node = (SoapUITreeNode) treePath.getLastPathComponent();
                targets.add(node.getModelItem());
            }

            if (targets.size() > 0) {
                ActionList actions = ActionListBuilder
                        .buildMultiActions(targets.toArray(new ModelItem[targets.size()]));
                if (actions.getActionCount() > 0) {
                    JPopupMenu popup = new JPopupMenu();
                    ActionSupport.addActions(actions, popup);
                    showToolTipLessPopupMenu(popup, e.getX(), e.getY());
                }
            }
        }
    }
}