/*
 * SoapUI, Copyright (C) 2004-2022 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence"); 
 * You may not use this work except in compliance with the Licence. 
 * You may obtain a copy of the Licence at: 
 * 
 * http://ec.europa.eu/idabc/eupl 
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is 
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

package com.eviware.soapui.ui.navigator;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.Releasable;
import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.tree.SoapUITreeNode;
import com.eviware.soapui.model.tree.SoapUITreeNodeRenderer;
import com.eviware.soapui.model.tree.nodes.ProjectTreeNode;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.plugins.factories.navigator.NavigatroNodeExpandStateProviderFactory;
import com.eviware.soapui.settings.UISettings;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.swing.ActionList;
import com.eviware.soapui.support.action.swing.ActionListBuilder;
import com.eviware.soapui.support.action.swing.ActionSupport;
import com.eviware.soapui.support.components.JXToolBar;
import com.eviware.soapui.support.swing.MenuBuilderHelper;
import com.eviware.soapui.ui.navigator.state.NavigatorNodesExpandStateEngine;
import com.yeremeyev.apiservant.model.tree.ApiServantFilteredTreeModel;
import com.yeremeyev.apiservant.plugins.interfaces.controls.common.tree.TreeFilter;
import com.yeremeyev.apiservant.plugins.interfaces.controls.common.tree.TreeType;
import com.yeremeyev.apiservant.plugins.interfaces.factories.TreeFilterFactory;
import com.yeremeyev.apiservant.plugins.interfaces.factories.WindowMarginsFactory;
import com.yeremeyev.apiservant.plugins.interfaces.margins.WindowMargin;
import com.yeremeyev.apiservant.plugins.interfaces.margins.WindowType;
import com.yeremeyev.apiservant.plugins.tools.MarginsTools;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The SoapUI navigator tree
 *
 * @author Ole.Matzura
 */

public class Navigator extends JPanel implements Releasable {
    public static final String NAVIGATOR = "navigator";
    private Workspace workspace;
    private NavigatorTree mainTree;
    private ApiServantFilteredTreeModel treeModel;
    private Set<NavigatorListener> listeners = new HashSet<NavigatorListener>();
    private NavigatorNodesExpandStateEngine navigatorNodesExpandStateEngine;
    private NavigatorTreeMouseListener navigatorTreeMouseListener;
    private NavigatorTreeKeyListener navigatorTreeKeyListener;

    public Navigator(Workspace workspace) {
        super(new BorderLayout());
        setName(NAVIGATOR);
        this.workspace = workspace;

        buildUI();
    }

    private void initializeTreeModel() {
        treeModel = new ApiServantFilteredTreeModel(workspace);
        List<TreeFilterFactory> treeFilterFactoryList = SoapUI.getFactoryRegistry().getFactories(TreeFilterFactory.class);
        for (TreeFilterFactory treeFilterFactory : treeFilterFactoryList) {
            if (treeFilterFactory.isAvailable(TreeType.MAIN_NAVIGATOR)) {
                treeModel.addFilter(treeFilterFactory.create(TreeType.MAIN_NAVIGATOR));
                break;
            }
        }
    }

    private List<WindowMargin> getNavigatorMarginsList() {
        List<WindowMargin> result = new ArrayList<>();
        List<WindowMarginsFactory> windowMarginFactoryList = SoapUI.getFactoryRegistry().getFactories(WindowMarginsFactory.class);
        for (WindowMarginsFactory windowMarginFactory : windowMarginFactoryList) {
            if (windowMarginFactory.isAvailable(WindowType.MAIN_NAVIGATOR)) {
                result.add(windowMarginFactory.create(WindowType.MAIN_NAVIGATOR));
                break;
            }
        }
        return result;
    }

    private void buildUI() {
        initializeTreeModel();
        mainTree = new NavigatorTree(treeModel);
        navigatorNodesExpandStateEngine = new NavigatorNodesExpandStateEngine();
        navigatorNodesExpandStateEngine.initialize(mainTree);
        mainTree.setRootVisible(true);
        mainTree.setExpandsSelectedPaths(true);
        mainTree.setScrollsOnExpand(true);
        mainTree.setToggleClickCount(0);
        navigatorTreeMouseListener = new NavigatorTreeMouseListener(mainTree);
        mainTree.addMouseListener(navigatorTreeMouseListener);
        mainTree.addTreeSelectionListener(new InternalTreeSelectionListener());
        mainTree.setCellRenderer(new SoapUITreeNodeRenderer());
        mainTree.setBorder(null);
        mainTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        navigatorTreeKeyListener = new NavigatorTreeKeyListener(mainTree);
        mainTree.addKeyListener(navigatorTreeKeyListener);
        JScrollPane sp = new JScrollPane(mainTree);
        sp.setBorder(BorderFactory.createEmptyBorder());
        List<WindowMargin> extendersList = getNavigatorMarginsList();
        JPanel pluginsPanel = new JPanel(new BorderLayout());
        pluginsPanel.add(sp, BorderLayout.CENTER);
        pluginsPanel.add(buildToolbar(), BorderLayout.NORTH);
        pluginsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pluginsPanel = MarginsTools.createExtendedMarginsComponent(pluginsPanel, extendersList);
        add(pluginsPanel);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private Component buildToolbar() {
        JXToolBar toolbar = UISupport.createSmallToolbar();

        JToggleButton toggleButton = new JToggleButton(new TogglePropertiesAction());
        toggleButton.setToolTipText("Toggles displaying of Test Properties in tree");
        toggleButton.setSize(10, 12);
        toolbar.addFixed(toggleButton);
        toolbar.addGlue();

        return toolbar;
    }

    public Project getCurrentProject() {
        TreePath path = mainTree.getSelectionPath();
        if (path == null) {
            return null;
        }

        Object node = path.getLastPathComponent();
        while (node != null && !(node instanceof ProjectTreeNode)) {
            path = path.getParentPath();
            node = (path == null ? null : path.getLastPathComponent());
        }

        if (node == null) {
            return null;
        }

        return ((ProjectTreeNode) node).getProject();
    }

    public void addNavigatorListener(NavigatorListener listener) {
        listeners.add(listener);
    }

    public void removeNavigatorListener(NavigatorListener listener) {
        listeners.remove(listener);
    }

    public void selectModelItem(ModelItem modelItem) {
        TreePath path = treeModel.getPath(modelItem);
        mainTree.setSelectionPath(path);
        mainTree.expandPath(path);
        mainTree.scrollPathToVisible(path);
    }

    public TreePath getTreePath(ModelItem modelItem) {
        return treeModel.getPath(modelItem);
    }

    public JTree getMainTree() {
        return mainTree;
    }

    public ModelItem getSelectedItem() {
        TreePath path = mainTree.getSelectionPath();
        if (path == null) {
            return null;
        }

        return ((SoapUITreeNode) path.getLastPathComponent()).getModelItem();
    }

    public void restoreNodeExpansion() {
        navigatorNodesExpandStateEngine.restoreNodeExpansion();
    }

    @Override
    public void release() {
        if (navigatorTreeMouseListener != null) {
            navigatorTreeMouseListener.release();
            navigatorTreeMouseListener = null;
        }
        if (navigatorTreeKeyListener != null) {
            navigatorTreeKeyListener.release();
            navigatorTreeKeyListener = null;
        }
    }

    public class InternalTreeSelectionListener implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            Object obj = e.getPath().getLastPathComponent();
            if (obj instanceof SoapUITreeNode) {
                SoapUITreeNode treeNode = (SoapUITreeNode) obj;
                MenuBuilderHelper.buildTreeNodeMenu(treeNode);
                if (!listeners.isEmpty()) {
                    TreePath newPath = e.getNewLeadSelectionPath();
                    NavigatorListener[] array = listeners.toArray(new NavigatorListener[listeners.size()]);
                    for (NavigatorListener listener : array) {
                        listener.nodeSelected(newPath == null ? null : treeNode);

                    }
                }
            }
        }
    }

    public boolean isVisible(TreePath path) {
        return mainTree.isVisible(path);
    }

    public boolean isExpanded(TreePath path) {
        return mainTree.isExpanded(path);
    }

    private class TogglePropertiesAction extends AbstractAction {
        public TogglePropertiesAction() {
            putValue(SMALL_ICON, UISupport.createImageIcon("/properties_step.png"));
        }

        public void actionPerformed(ActionEvent e) {
            Enumeration<TreePath> expandedDescendants = mainTree.getExpandedDescendants(getTreePath(workspace));
            TreePath selectionPath = mainTree.getSelectionPath();

            treeModel.setShowProperties(!treeModel.isShowProperties());

            while (expandedDescendants != null && expandedDescendants.hasMoreElements()) {
                mainTree.expandPath(expandedDescendants.nextElement());
            }

            if (selectionPath != null) {
                mainTree.setSelectionPath(selectionPath);
            }
        }
    }
}
