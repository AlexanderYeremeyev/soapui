package com.eviware.soapui.ui.desktop.single;

import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.ui.desktop.DesktopFactory;
import com.eviware.soapui.ui.desktop.SoapUIDesktop;

public class SingleDesctopFactory implements DesktopFactory {
    public SoapUIDesktop createDesktop(Workspace workspace) {
        return new SingleDesktop(workspace);
    }
}
