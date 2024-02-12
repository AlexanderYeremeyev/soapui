package com.yeremeyev.apiservant.teststeps.telegram.bot.ui;

import com.eviware.soapui.support.ListDataChangeListener;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.components.JXToolBar;
import com.eviware.soapui.support.log.JLogList;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.yeremeyev.apiservant.teststeps.telegram.bot.TelegramSendBotMessageToChatTestStep;
import com.yeremeyev.apiservant.ui.components.factories.TabbedPaneFactory;
import com.yeremeyev.java.common.windows.common.Colors;
import com.yeremeyev.java.common.windows.swing.layouts.tools.GridBagConstraintsConstructor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TelegramSendBotMessageToChatStepDesktopPanel
        extends ModelItemDesktopPanel<TelegramSendBotMessageToChatTestStep>
        implements PropertyChangeListener
{
    private RunAction runAction;
    private JTextField apiTokenBotTextField;
    private JTextField channelNameTextField;

    private RSyntaxTextArea editArea;
    private JLabel responseTextLabel;
    private JLogList logArea;

    private JComponent buildToolbar() {
        JXToolBar toolBar = UISupport.createToolbar();
        JButton runButton = UISupport.createToolbarButton(runAction);
        toolBar.add(runButton);
        toolBar.add(Box.createHorizontalGlue());
        JLabel label = new JLabel("<html>setting must have valid values</html>");
        label.setToolTipText(label.getText());
        label.setMaximumSize(label.getPreferredSize());

        toolBar.add(label);

        return toolBar;
    }

    private JPanel buildSettings() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Colors.SMOKY_WHITE);

        JLabel settingsTitleLabel = new JLabel("Settings");

        JLabel apiTokenBotLabel = new JLabel("Enter api token bot");
        apiTokenBotTextField = new JTextField("", 30);

        JLabel channelNameLabel = new JLabel("Enter channel name/id");
        channelNameTextField = new JTextField("", 30);

        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(Colors.TRANSPARENT_COLOR);

        GridBagConstraintsConstructor constraintsConstructor = new GridBagConstraintsConstructor()
                .setAnchor(GridBagConstraints.LINE_START)
                .setInsets(new Insets(4, 10, 4, 8));

        settingsPanel.add(apiTokenBotLabel, constraintsConstructor);
        settingsPanel.add(apiTokenBotTextField, constraintsConstructor.nextCell());

        settingsPanel.add(channelNameLabel, constraintsConstructor.nextRow().fillNone());
        settingsPanel.add(channelNameTextField, constraintsConstructor.nextCell());

        JPanel settingsContainer = new JPanel(new BorderLayout());
        settingsContainer.setBackground(Colors.TRANSPARENT_COLOR);
        settingsContainer.add(settingsPanel, BorderLayout.WEST);

        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.SHADOW);
        resultPanel.setBorder(border);

        resultPanel.add(settingsTitleLabel, BorderLayout.NORTH);
        resultPanel.add(settingsContainer);

        return resultPanel;
    }

    private JPanel buildTopPanels() {
        JPanel resultPanel = new JPanel(new BorderLayout());

        resultPanel.add(buildToolbar(), BorderLayout.NORTH);
        resultPanel.add(buildSettings());

        return resultPanel;
    }

    private JPanel buildContentPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());

        editArea = new RSyntaxTextArea(20, 60);

        editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        editArea.setFont(UISupport.getEditorFont());
        editArea.setCodeFoldingEnabled(true);
        editArea.setAntiAliasingEnabled(true);
        editArea.setMinimumSize(new Dimension(50, 50));
        editArea.setCaretPosition(0);
        editArea.setEnabled(true);
        editArea.setEditable(true);
        editArea.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.WHITE));

        resultPanel.add(editArea);

        return resultPanel;
    }

    private JComponent buildResponsePanel() {
        JTabbedPane tabbedPane = TabbedPaneFactory.createDefaultTabbedPane(SwingConstants.BOTTOM);

        responseTextLabel = new JLabel();

        JPanel responsePanel = new JPanel(new BorderLayout());
        responsePanel.setMinimumSize(new Dimension(200, 150));
        responsePanel.setPreferredSize(new Dimension(200, 150));
        responsePanel.setBackground(Colors.GAINSBORO);
        responsePanel.add(responseTextLabel);

        logArea = new JLogList("Request Log");
        logArea.getLogList().getModel().addListDataListener(new ListDataChangeListener() {

            public void dataChanged(ListModel model) {
                tabbedPane.setTitleAt(1, "Request Log (" + model.getSize() + ")");
            }
        });

        tabbedPane.add("Response", responsePanel);
        tabbedPane.add("Rquest Log", logArea);

        return tabbedPane;
    }


    private void buildUI() {
        JPanel rootPanel = new JPanel(new BorderLayout());

        rootPanel.add(buildTopPanels(), BorderLayout.NORTH);
        rootPanel.add(buildResponsePanel(), BorderLayout.SOUTH);
        rootPanel.add(buildContentPanel());

        add(rootPanel);
    }

    public TelegramSendBotMessageToChatStepDesktopPanel(TelegramSendBotMessageToChatTestStep telegramSendMessageTestStep) {
        super(telegramSendMessageTestStep);

        runAction = new RunAction(getModelItem());

        buildUI();
        setPreferredSize(new Dimension(600, 440));

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                editArea.requestFocusInWindow();
            }
        });
    }

    public boolean onClose(boolean canCancel) {
        return super.release();
    }

    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName.equals(TelegramSendBotMessageToChatTestStep.RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME)) {
            responseTextLabel.setText(propertyChangeEvent.getNewValue().toString());
        }

        super.propertyChange(propertyChangeEvent);
    }
}