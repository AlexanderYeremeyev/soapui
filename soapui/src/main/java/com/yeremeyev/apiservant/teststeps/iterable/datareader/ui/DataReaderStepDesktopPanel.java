package com.yeremeyev.apiservant.teststeps.iterable.datareader.ui;

import com.eviware.soapui.support.ListDataChangeListener;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.components.JXToolBar;
import com.eviware.soapui.support.log.JLogList;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.yeremeyev.apiservant.teststeps.common.actions.RunAction;
import com.yeremeyev.apiservant.teststeps.common.messages.ResponseMessage;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.raw.RawMessageView;
import com.yeremeyev.apiservant.teststeps.iterable.datareader.DataReaderTestStep;
import com.yeremeyev.apiservant.teststeps.readtextfile.ReadTextFileTestStep;
import com.yeremeyev.apiservant.ui.components.factories.TabbedPaneFactory;
import com.yeremeyev.java.common.windows.common.Colors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DataReaderStepDesktopPanel
        extends ModelItemDesktopPanel<DataReaderTestStep>
        implements PropertyChangeListener {
    private final static String UNEXPECTED_MISTAKE = "Unexpected mistake";
    private final static String SETTINGS_TITLE = "Settings";
    private final static String ENTER_FILE_PATH_MESSAGE = "Enter file path";
    private final static String LOGGER_NAME_TEMPLATE = "%s#%d";
    private final static String DEFAULT_LOGGER_TAB_NAME = "Request Log";
    private final static String LOGGER_TAB_NAME_TEMPLATE = "Request Log (%s)";
    private final static int DEFAULT_FILE_PATH_COLUMNS = 30;

    private RunAction runAction;
    private JTextField filePathTextField;
    private JButton browseButton;
    private RawMessageView rawMessageView;

    private String loggerUniqueName;
    private JLogList logArea;
    private Logger logger;

    private ResponseMessage responseMessage;

    private JComponent buildToolbar() {
        JXToolBar toolBar = UISupport.createToolbar();
        JButton runButton = UISupport.createToolbarButton(runAction);
        toolBar.add(runButton);

        return toolBar;
    }

    private JPanel buildSettings() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Colors.SMOKY_WHITE);

        return resultPanel;
    }

    private JPanel buildTopPanels() {
        JPanel resultPanel = new JPanel(new BorderLayout());

        resultPanel.add(buildToolbar(), BorderLayout.NORTH);
        resultPanel.add(buildSettings());

        return resultPanel;
    }

    private JComponent buildContentPanel() {
        rawMessageView = new RawMessageView(responseMessage);

        return rawMessageView.getComponent();
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

    public DataReaderStepDesktopPanel(DataReaderTestStep dataReaderTestStep) {
        super(dataReaderTestStep);

        responseMessage = new ResponseMessage();

        loggerUniqueName = String.format(LOGGER_NAME_TEMPLATE, getModelItem().getName(), hashCode());
        logger = LogManager.getLogger(loggerUniqueName);
        runAction = new RunAction(getModelItem(), logger);

        buildUI();
        setPreferredSize(new Dimension(600, 440));

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                rawMessageView.getComponent().requestFocusInWindow();
            }
        });
    }

    public boolean onClose(boolean canCancel) {
        logArea.removeLogger(logger.getName());
        logger = null;

        rawMessageView.release();
        responseMessage.release();

        return super.release();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName.equals(ReadTextFileTestStep.FILE_CONTENT_EXPAND_PROPERTY_NAME)) {
            String message = propertyChangeEvent.getNewValue() == null ? UNEXPECTED_MISTAKE : propertyChangeEvent.getNewValue().toString();
            responseMessage.setMessage(message);
        }

        super.propertyChange(propertyChangeEvent);
    }
}