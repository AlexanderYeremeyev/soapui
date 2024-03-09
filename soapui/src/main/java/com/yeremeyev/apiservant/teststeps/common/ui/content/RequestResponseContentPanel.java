package com.yeremeyev.apiservant.teststeps.common.ui.content;

import com.yeremeyev.apiservant.teststeps.common.messages.RequestMessage;
import com.yeremeyev.apiservant.teststeps.common.messages.ResponseMessage;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.PossibleViews;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.ViewFactory;
import com.yeremeyev.apiservant.teststeps.common.ui.content.views.interfaces.MessageView;
import com.yeremeyev.apiservant.ui.components.factories.TabbedPaneFactory;
import com.yeremeyev.java.core.interfaces.common.Releasable;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestResponseContentPanel extends JPanel implements Releasable {
    private double INITIAL_LEFT_WEIGHT = 0.5d;
    private JSplitPane splitPane;
    private JTabbedPane requestTabbedPane;
    private JTabbedPane responseTabbedPane;
    private List<MessageView> requestViewsList;
    private List<MessageView> responseViewsList;

    private void buildRequestPanel(Set<PossibleViews> requestViews, RequestMessage requestMessage) {
        requestTabbedPane = TabbedPaneFactory.createDefaultTabbedPane();

        requestViewsList = new ArrayList<>();
        for (PossibleViews view : requestViews) {
            requestViewsList.add(ViewFactory.create(view, requestMessage, false));
        }

        requestViewsList.stream().forEach(view -> requestTabbedPane.add(view.getViewTitle(), view.getComponent()));
    }

    private void buildResponsePanel(Set<PossibleViews> responseViews, ResponseMessage responseMessage) {
        responseTabbedPane = TabbedPaneFactory.createDefaultTabbedPane();

        responseViewsList = new ArrayList<>();
        for (PossibleViews view : responseViews) {
            responseViewsList.add(ViewFactory.create(view, responseMessage, true));
        }

        responseViewsList.stream().forEach(view -> responseTabbedPane.add(view.getViewTitle(), view.getComponent()));
    }

    private void buildUI(
            RequestMessage requestMessage,
            ResponseMessage responseMessage,
            Set<PossibleViews> requestViews,
            Set<PossibleViews> responseViews) {
        buildRequestPanel(requestViews, requestMessage);
        buildResponsePanel(responseViews, responseMessage);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestTabbedPane, responseTabbedPane);
        splitPane.setDividerLocation(INITIAL_LEFT_WEIGHT);
        splitPane.setResizeWeight(INITIAL_LEFT_WEIGHT);

        setLayout(new BorderLayout());
        add(splitPane);
    }

    public RequestResponseContentPanel(
            RequestMessage requestMessage,
            ResponseMessage responseMessage,
            Set<PossibleViews> requestViews,
            Set<PossibleViews> responseViews) {
        buildUI(requestMessage, responseMessage, requestViews, responseViews);
    }

    @Override
    public void release() {
        requestViewsList.stream().forEach(MessageView::release);
        responseViewsList.stream().forEach(MessageView::release);
    }
}
