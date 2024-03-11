package com.yeremeyev.apiservant.teststeps.common.ui.content.views.json;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.support.UISupport;
import com.yeremeyev.java.core.interfaces.common.Releasable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class SaveJsonTextAreaAction extends AbstractAction implements Releasable {
    private final Logger log = LogManager.getLogger(SaveJsonTextAreaAction.class);
    private final static String DIALOG_TITLE = "Save";
    private final static String ACTION_NAME = "Save as...";
    private final static String DIALOG_EXTENSION = ".json";
    private final static String DIALOG_FILE_TYPE = "JSON Files (*.json)";
    private final static String SAVE_FILE_ERROR_PREFIX = "Error saving json to file: ";
    private final static String OPERATION_COMPLETE_MESSAGE = "JSON written to [%s]";

    private RSyntaxTextArea textArea;

    public SaveJsonTextAreaAction(RSyntaxTextArea editArea) {
        super(ACTION_NAME);

        this.textArea = editArea;
        if (UISupport.isMac()) {
            putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke("menu S"));
        } else {
            putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke("ctrl S"));
        }
    }

    private void closeWriter(FileWriter writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (IOException subException) {
            SoapUI.logError(subException);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        File file = UISupport.getFileDialogs().saveAs(this, DIALOG_TITLE, DIALOG_EXTENSION, DIALOG_FILE_TYPE, null);
        if (file == null) {
            return;
        }

        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
            writer.write(textArea.getText());
            writer.close();

            log.info(String.format(OPERATION_COMPLETE_MESSAGE, file.getAbsolutePath()));
        } catch (IOException exception) {
            UISupport.showErrorMessage(SAVE_FILE_ERROR_PREFIX + exception.getMessage());
        } finally {
            closeWriter(writer);
        }
    }

    @Override
    public void release() {
        textArea = null;
    }
}