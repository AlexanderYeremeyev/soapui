package com.yeremeyev.apiservant.teststeps.common.ui.content.views.json;

import com.eviware.soapui.support.UISupport;
import com.yeremeyev.java.core.interfaces.common.Releasable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

class GoToLineAction extends AbstractAction implements Releasable {
    private final Logger log = LogManager.getLogger(GoToLineAction.class);

    private final static String DIALOG_TITLE = "Go To Line";
    private final static String ACTION_DESCRIPTION = "Moves the caret to the specified line";
    private final static String QUESTION_TEMPLATE = "Enter line-number to (1..%s)";
    private RSyntaxTextArea editArea;

    public GoToLineAction(RSyntaxTextArea editArea) {
        super(DIALOG_TITLE);
        this.editArea = editArea;
        putValue(Action.SHORT_DESCRIPTION, ACTION_DESCRIPTION);
        putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke("control G"));
    }

    public void actionPerformed(ActionEvent e) {
        String question = String.format(QUESTION_TEMPLATE, editArea.getLineCount());
        String value = String.valueOf(editArea.getCaretLineNumber() + 1);
        String line = UISupport.prompt(question, DIALOG_TITLE, value);

        if (line == null) {
            return;
        }
        try {
            int ln = Integer.parseInt(line) - 1;

            if (ln < 0) {
                ln = 0;
            }

            if (ln >= editArea.getLineCount()) {
                ln = editArea.getLineCount() - 1;
            }

            editArea.scrollRectToVisible(editArea.modelToView(editArea.getLineStartOffset(ln)));
            editArea.setCaretPosition(editArea.getLineStartOffset(ln));
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    public void release() {
        editArea = null;
    }
}