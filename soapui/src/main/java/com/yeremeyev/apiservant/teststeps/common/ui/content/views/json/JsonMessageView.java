package com.yeremeyev.apiservant.teststeps.common.ui.content.views.json;

import com.eviware.soapui.support.JsonUtil;
import com.eviware.soapui.support.xml.SyntaxEditorUtil;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.SimpleMessageStorage;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.UpdateSimpleMessageListener;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.common.BaseEditableMessageView;
import com.yeremeyev.java.core.tools.strings.StringTools;
import net.sf.json.JSON;
import net.sf.json.JSONException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

public class JsonMessageView
        extends BaseEditableMessageView
        implements UpdateSimpleMessageListener {
    private static final String EMPTY_JSON_MESSAGE = "<Empty JSON content>";
    private static final String NOT_JSON_MESSAGE = "The content you are trying to view cannot be viewed as JSON";

    private static final String DEFAULT_TITLE = "JSON";

    private RSyntaxTextArea contentEditor;
    private RTextScrollPane editorScrollPane;

    private SaveJsonTextAreaAction saveJsonAction;
    private GoToLineAction goToLineAction;
    private EnableLineNumbersAction enableLineNumbersAction;
    private FormatJsonAction formatJsonAction;

    private void buildPopup(JPopupMenu inputPopup, RSyntaxTextArea editArea) {
        formatJsonAction = new FormatJsonAction(editArea);
        enableLineNumbersAction = new EnableLineNumbersAction(editorScrollPane);
        goToLineAction = new GoToLineAction(editArea);
        saveJsonAction = new SaveJsonTextAreaAction(editArea);

        inputPopup.add(saveJsonAction);
        inputPopup.addSeparator();
        inputPopup.add(goToLineAction);
        inputPopup.add(enableLineNumbersAction);
        inputPopup.addSeparator();
        inputPopup.add(formatJsonAction);
    }

    private void buildUI() {
        contentEditor = new RSyntaxTextArea();
        contentEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        contentEditor.setEditable(!isReadOnly());
        SyntaxEditorUtil.decorateSyntaxArea(contentEditor);

        editorScrollPane = new RTextScrollPane(contentEditor);
        buildPopup(contentEditor.getPopupMenu(), contentEditor);

        editorScrollPane.setFoldIndicatorEnabled(true);
        editorScrollPane.setLineNumbersEnabled(true);
        editorScrollPane.setBorder(BorderFactory.createEmptyBorder());


        add(editorScrollPane);
    }

    public JsonMessageView(boolean readOnly, SimpleMessageStorage messageStorage) {
        super(DEFAULT_TITLE, readOnly, false, messageStorage);

        buildUI();

        getMessageStorage().addListener(this);
    }

    @Override
    public void release() {
        getMessageStorage().removeListener(this);
        super.release();
    }

    @Override
    public void onMessageSimpleUpdate(String message) {
        String currentMessage = contentEditor.getText();
        if (currentMessage.equals(message)) {
            return;
        }
        setEditorContent(message);
    }

    @Override
    protected void onMessageUpdated() {
        setEditorContent(getMessageStorage().getMessage());
    }

    protected void setEditorContent(String message) {
        if (message == null) {
            contentEditor.setText(StringTools.EMPTY);
            return;
        }
        String content;
        try {
            JSON json = new JsonUtil().parseTrimmedText(message);
            content = json.isEmpty() ? EMPTY_JSON_MESSAGE : json.toString(3);
        } catch (JSONException e) {
            content = NOT_JSON_MESSAGE;
        }
        contentEditor.setText(content);
    }
}
