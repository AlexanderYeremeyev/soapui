package com.yeremeyev.apiservant.ui.components.windows;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class NoImplementationComponent extends JPanel {
    private static final String NO_IMPLEMENTATION_MESSAGE = "No implementation";

    private void buildUI() {
        JLabel description = new JLabel(NO_IMPLEMENTATION_MESSAGE, 0);
        add(description);
    }

    public NoImplementationComponent() {
        setLayout(new BorderLayout());

        buildUI();
    }
}
