package com.eviware.soapui.ui.desktop.single;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.PanelBuilder;
import com.eviware.soapui.model.util.PanelBuilderRegistry;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.ui.desktop.AbstractSoapUIDesktop;
import com.eviware.soapui.ui.desktop.DesktopPanel;
import com.eviware.soapui.ui.desktop.SoapUIDesktop;
import com.yeremeyev.apiservant.ui.components.windows.NoImplementationComponent;

import javax.swing.JComponent;
import java.awt.Toolkit;

public class SingleDesktop extends AbstractSoapUIDesktop {
    private SingleMainPanel mainPanel;
    private DesktopsCache desktopsCache;
    private final NoImplementationComponent noImplementationComponent;

    public SingleDesktop(Workspace workspace) {
        super(workspace);

        noImplementationComponent = new NoImplementationComponent();
        desktopsCache = new DesktopsCache();
        mainPanel = new SingleMainPanel();
        mainPanel.add(noImplementationComponent);
    }

    @Override
    public boolean closeDesktopPanel(DesktopPanel desktopPanel) {
        return false;
    }

    @Override
    public boolean hasDesktopPanel(ModelItem modelItem) {
        return false;
    }

    private void clearMainPanel() {
        UISupport.invokeAndWaitIfNotInEDT(() -> {
            mainPanel.removeAll();
            mainPanel.invalidate();
            mainPanel.repaint();
        });
    }

    private void addToMainPanel(JComponent component) {
        UISupport.invokeAndWaitIfNotInEDT(() -> {
            mainPanel.add(component);

            mainPanel.invalidate();
            mainPanel.repaint();
            component.invalidate();
            component.repaint();
        });
    }

    @Override
    public DesktopPanel showDesktopPanel(ModelItem modelItem) {
        clearMainPanel();

        PanelBuilder<ModelItem> panelBuilder = PanelBuilderRegistry.getPanelBuilder(modelItem);
        DesktopPanel currentPanel = desktopsCache.find(modelItem);
        if (currentPanel != null) {
            addToMainPanel(currentPanel.getComponent());
        } else if (panelBuilder != null && panelBuilder.hasDesktopPanel()) {
            currentPanel = panelBuilder.buildDesktopPanel(modelItem);
            if (currentPanel == null) {
                addToMainPanel(noImplementationComponent);
                return null;
            }
            addToMainPanel(currentPanel.getComponent());

            desktopsCache.put(modelItem, currentPanel);

            fireDesktopPanelCreated(currentPanel);

            currentPanel.getComponent().requestFocusInWindow();

        } else {
            addToMainPanel(noImplementationComponent);
            Toolkit.getDefaultToolkit().beep();
        }

        //TODO:
        /*final DesktopPanel currentPanelFinal = currentPanel;
        UISupport.invokeAndWaitIfNotInEDT(new Runnable() {
                                              @Override
                                              public void run() {
                                                  mainPanel.invalidate();
                                                  mainPanel.repaint();
                                                  mainPanel.updateUI();
                                              }
                                          });
*/

        //enableWindowActions();

        return currentPanel;
    }

    @Override
    public boolean closeDesktopPanel(ModelItem modelItem) {
        return false;
    }

    @Override
    public DesktopPanel[] getDesktopPanels() {
        return new DesktopPanel[0];
    }

    @Override
    public DesktopPanel getDesktopPanel(ModelItem modelItem) {
        return null;
    }

    @Override
    public DesktopPanel showDesktopPanel(DesktopPanel desktopPanel) {
        return null;
    }

    @Override
    public JComponent getDesktopComponent() {
        return mainPanel;
    }

    @Override
    public void transferTo(SoapUIDesktop newDesktop) {

    }

    @Override
    public boolean closeAll() {
        return false;
    }

    @Override
    public void minimize(DesktopPanel desktopPanel) {

    }

    @Override
    public void maximize(DesktopPanel dp) {

    }
}
