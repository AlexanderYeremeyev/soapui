package com.yeremeyev.apiservant.teststeps.common.ui.content.views.json;

import com.eviware.soapui.support.JsonUtil;
import com.eviware.soapui.support.UISupport;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

class FormatJsonAction extends AbstractAction {
    private final static Logger log = LogManager.getLogger(FormatJsonAction.class);

    private final static String ACTION_NAME = "Format JSON";
    private final static String TOOLTIP = "Pretty-prints JSON";

    private RSyntaxTextArea textArea;

    public FormatJsonAction(RSyntaxTextArea textArea) {
        super(ACTION_NAME);

        this.textArea = textArea;
        putValue(Action.SMALL_ICON, UISupport.createImageIcon("/format_request.gif"));
        putValue(Action.SHORT_DESCRIPTION, TOOLTIP);
        if (UISupport.isMac()) {
            String keyStroke = "shift meta F";
            putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke(keyStroke));
            textArea.getInputMap().put(KeyStroke.getKeyStroke(keyStroke), this);
        } else {
            String keyStroke = "alt F";
            putValue(Action.ACCELERATOR_KEY, UISupport.getKeyStroke(keyStroke));
            textArea.getInputMap().put(KeyStroke.getKeyStroke(keyStroke), this);
        }
    }

    protected boolean isValidJson(String jsonText) {
        return JsonUtil.isValidJson(jsonText);
    }

    public void actionPerformed(ActionEvent event) {
        try {
            Rectangle visibleRect = textArea.getVisibleRect();
            String message = textArea.getText();
            if (!isValidJson(message)) {
                return;
            }
            JsonNode json = JsonUtil.parseTrimmedTextToJsonNode(message);
            textArea.setText(JsonUtil.format(json));
            textArea.setCaretPosition(0);
            SwingUtilities.invokeLater(() -> {
                textArea.scrollRectToVisible(visibleRect);
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
