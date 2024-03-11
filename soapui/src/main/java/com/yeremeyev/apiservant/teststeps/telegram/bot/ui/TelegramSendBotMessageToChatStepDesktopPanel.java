package com.yeremeyev.apiservant.teststeps.telegram.bot.ui;

import com.eviware.soapui.support.ListDataChangeListener;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.components.JXToolBar;
import com.eviware.soapui.support.log.JLogList;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.yeremeyev.apiservant.teststeps.common.messages.RequestMessage;
import com.yeremeyev.apiservant.teststeps.common.messages.ResponseMessage;
import com.yeremeyev.apiservant.teststeps.common.messages.interfaces.UpdateSimpleMessageListener;
import com.yeremeyev.apiservant.teststeps.common.ui.content.RequestResponseContentPanel;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.PossibleViews;
import com.yeremeyev.apiservant.teststeps.telegram.bot.TelegramSendBotMessageToChatTestStep;
import com.yeremeyev.apiservant.ui.components.factories.TabbedPaneFactory;
import com.yeremeyev.java.common.windows.common.Colors;
import com.yeremeyev.java.common.windows.swing.layouts.tools.GridBagConstraintsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

public class TelegramSendBotMessageToChatStepDesktopPanel
        extends ModelItemDesktopPanel<TelegramSendBotMessageToChatTestStep>
        implements PropertyChangeListener, UpdateSimpleMessageListener {
    private final static String UNEXPECTED_MISTAKE = "Unexpected mistake";
    private final static String SETTINGS_TITLE = "Settings";
    private final static String ENTER_API_TOKEN_BOT_MESSAGE = "Enter api token bot";
    private final static String ENTER_CHANNEL_NAME_MESSAGE = "Enter channel name/id";
    private final static String LOGGER_NAME_TEMPLATE = "%s#%d";
    private final static String DEFAULT_LOGGER_TAB_NAME = "Request Log";
    private final static String LOGGER_TAB_NAME_TEMPLATE = "Request Log (%s)";
    private final static String SETTINGS_TOOLTIP_MESSAGE = "<html>setting must have valid values</html>";
    private final static int DEFAULT_TOKEN_COLUMNS = 30;

    private RunAction runAction;
    private JTextField apiTokenBotTextField;
    private JTextField channelNameTextField;
    private RequestResponseContentPanel requestResponseContentPanel;

    private String loggerUniqueName;
    private JLogList logArea;
    private Logger logger;

    private RequestMessage requestMessage;
    private ResponseMessage responseMessage;

    private JComponent buildToolbar() {
        JXToolBar toolBar = UISupport.createToolbar();
        JButton runButton = UISupport.createToolbarButton(runAction);
        toolBar.add(runButton);
        toolBar.add(Box.createHorizontalGlue());
        JLabel label = new JLabel(SETTINGS_TOOLTIP_MESSAGE);
        label.setToolTipText(label.getText());
        label.setMaximumSize(label.getPreferredSize());

        toolBar.add(label);

        return toolBar;
    }

    private JPanel buildSettings() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Colors.SMOKY_WHITE);

        JLabel settingsTitleLabel = new JLabel(SETTINGS_TITLE);

        JLabel apiTokenBotLabel = new JLabel(ENTER_API_TOKEN_BOT_MESSAGE);
        apiTokenBotTextField = new JTextField(getModelItem().getApiTokenBot(), DEFAULT_TOKEN_COLUMNS);
        apiTokenBotTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                getModelItem().setApiTokenBot(apiTokenBotTextField.getText());
            }
        });

        JLabel channelNameLabel = new JLabel(ENTER_CHANNEL_NAME_MESSAGE);
        channelNameTextField = new JTextField(getModelItem().getChannelName(), DEFAULT_TOKEN_COLUMNS);
        channelNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                getModelItem().setChannelName(channelNameTextField.getText());
            }
        });

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
        requestResponseContentPanel = new RequestResponseContentPanel(
                requestMessage,
                responseMessage,
                Set.of(PossibleViews.TEXT),
                Set.of(PossibleViews.RAW, PossibleViews.JSON)
        );

        return requestResponseContentPanel;
    }

    private JComponent buildResponsePanel() {
        JTabbedPane tabbedPane = TabbedPaneFactory.createDefaultTabbedPane(SwingConstants.BOTTOM);

        logArea = new JLogList(DEFAULT_LOGGER_TAB_NAME);
        logArea.addLogger(loggerUniqueName, true);
        logArea.getLogList().getModel().addListDataListener(new ListDataChangeListener() {
            public void dataChanged(ListModel model) {
                tabbedPane.setTitleAt(0, String.format(LOGGER_TAB_NAME_TEMPLATE, model.getSize()));
            }
        });

        tabbedPane.add(DEFAULT_LOGGER_TAB_NAME, logArea);

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

        requestMessage = new RequestMessage();
        requestMessage.setMessage(getModelItem().getSendMessage());
        responseMessage = new ResponseMessage();
        requestMessage.addListener(this);

        loggerUniqueName = String.format(LOGGER_NAME_TEMPLATE, getModelItem().getName(), hashCode());
        logger = LogManager.getLogger(loggerUniqueName);
        runAction = new RunAction(getModelItem(), logger);

        buildUI();
        setPreferredSize(new Dimension(600, 440));

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                requestResponseContentPanel.requestFocusInWindow();
            }
        });
    }

    @Override
    public void onMessageSimpleUpdate(String message) {
        // request editor update
        getModelItem().setSendMessage(message);
    }

    public boolean onClose(boolean canCancel) {
        logArea.removeLogger(logger.getName());
        logger = null;

        requestResponseContentPanel.release();

        requestMessage.removeListener(this);
        requestMessage.release();
        responseMessage.release();

        return super.release();
    }

    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName.equals(TelegramSendBotMessageToChatTestStep.RESPONSE_MESSAGE_EXPAND_PROPERTY_NAME)) {
            String message = propertyChangeEvent.getNewValue() == null ? UNEXPECTED_MISTAKE : propertyChangeEvent.getNewValue().toString();
            responseMessage.setMessage(message);
        }

        super.propertyChange(propertyChangeEvent);
    }
}