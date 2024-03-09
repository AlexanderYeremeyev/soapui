package com.yeremeyev.apiservant.teststeps.common.ui.content.views.json;

import com.eviware.soapui.support.UISupport;
import com.yeremeyev.java.core.interfaces.common.Releasable;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

class EnableLineNumbersAction extends AbstractAction implements Releasable {
    private final static String DIALOG_TITLE = "Toggle Line Numbers";
    private RTextScrollPane editorScrollPane;

    public EnableLineNumbersAction(RTextScrollPane editorScrollPane) {
        super(DIALOG_TITLE);
        this.editorScrollPane = editorScrollPane;
        if (UISupport.isMac()) {
            putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke("ctrl L"));
        } else {
            putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke("alt L"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editorScrollPane.setLineNumbersEnabled(!editorScrollPane.getLineNumbersEnabled());
    }

    @Override
    public void release() {
        editorScrollPane = null;
    }
}
