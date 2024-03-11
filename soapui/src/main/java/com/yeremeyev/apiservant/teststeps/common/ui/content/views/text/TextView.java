package com.yeremeyev.apiservant.teststeps.common.ui.content.views.text;

import com.eviware.soapui.support.DocumentListenerAdapter;
import com.eviware.soapui.support.UISupport;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.SimpleMessageStorage;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.common.BaseEditableMessageView;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import java.awt.Color;
import java.awt.Dimension;

public class TextView extends BaseEditableMessageView {
    private static final String DEFAULT_TITLE = "Text";

    private RSyntaxTextArea messageEditArea;
    private JScrollPane scrollPane;

    private void buildUI() {
        messageEditArea = new RSyntaxTextArea(20, 60);

        messageEditArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        messageEditArea.setFont(UISupport.getEditorFont());
        messageEditArea.setCodeFoldingEnabled(true);
        messageEditArea.setAntiAliasingEnabled(true);
        messageEditArea.setMinimumSize(new Dimension(50, 50));
        messageEditArea.setCaretPosition(0);
        messageEditArea.setEnabled(true);
        messageEditArea.setEditable(true);
        messageEditArea.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.WHITE));

        String initialText = getMessageStorage().getMessage();
        messageEditArea.setText(initialText);

        messageEditArea.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                onMessageUpdated();
            }
        });

        scrollPane = new JScrollPane(messageEditArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane);
    }

    public TextView(SimpleMessageStorage messageStorage) {
        super(DEFAULT_TITLE, false, false, messageStorage);

        buildUI();
    }

    @Override
    protected void onMessageUpdated() {
        getMessageStorage().setMessage(messageEditArea.getText());
    }
}
