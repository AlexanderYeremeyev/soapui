package com.eviware.soapui.ui.desktop.single;

import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.Releasable;
import com.eviware.soapui.ui.desktop.DesktopPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesktopsCache implements Releasable {
    private static final int DEFAULT_CACHE_SIZE = 10;
    private Map<ModelItem, DesktopPanel> modelItemsMap;
    private List<ModelItem> modelItemsList;
    private int cacheSize;

    public DesktopsCache() {
        modelItemsMap = new HashMap<>();
        modelItemsList = new ArrayList<>();
        cacheSize = DEFAULT_CACHE_SIZE;
    }

    @Override
    public void release() {
        for (DesktopPanel panel : modelItemsMap.values()) {
            panel.onClose(true);
        }
        modelItemsMap.clear();
        modelItemsList.clear();
    }

    private boolean containEmptyItems() {
        return modelItemsMap.size() + 1 < cacheSize;
    }

    private boolean removeOldItem() {
        if (modelItemsList.isEmpty()) {
            return false;
        }
        if (containEmptyItems()) {
            return false;
        }

        ModelItem modelItem = modelItemsList.get(modelItemsList.size() - 1);
        if (modelItemsMap.containsKey(modelItem)) {
            modelItemsMap.get(modelItem).onClose(true);
            modelItemsMap.remove(modelItem);
        }
        modelItemsList.remove(modelItem);
        return true;
    }

    /**
     * add item without any check logic
     *
     * @param modelItem
     * @param desktopPanel
     */
    private void add(ModelItem modelItem, DesktopPanel desktopPanel) {
        modelItemsList.add(0, modelItem);
        modelItemsMap.put(modelItem, desktopPanel);
    }

    public void put(ModelItem modelItem, DesktopPanel desktopPanel) {
        removeOldItem();
        add(modelItem, desktopPanel);
    }

    public DesktopPanel find(ModelItem modelItem) {
        if (modelItemsMap.containsKey(modelItem)) {
            modelItemsList.remove(modelItem);
            modelItemsList.add(0, modelItem);
            return modelItemsMap.get(modelItem);
        }
        return null;
    }
}
