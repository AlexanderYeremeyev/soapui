package com.eviware.soapui.plugins.storage;

import com.eviware.soapui.plugins.interfaces.controls.ExternalTreeFilter;
import com.eviware.soapui.plugins.interfaces.controls.TreeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalFiltersStorage {
    private static Map<TreeType, List<ExternalTreeFilter>> filtersMap = new HashMap<>();

    private static List<ExternalTreeFilter> getTreeFiltersInternal(TreeType treeType) {
        List<ExternalTreeFilter> result = null;
        if (filtersMap.containsKey(treeType)) {
            result = filtersMap.get(treeType);
        } else {
            result = new ArrayList<>();
            filtersMap.put(treeType, result);
        }
        return result;
    }

    public static synchronized void addTreeFilter(ExternalTreeFilter treeFilter) {
        if (treeFilter == null) {
            return;
        }
        List<ExternalTreeFilter> filtersList = getTreeFiltersInternal(treeFilter.getTreeType());
        filtersList.add(treeFilter);
    }

    public static synchronized List<ExternalTreeFilter> getTreeFilters(TreeType treeType) {
        List<ExternalTreeFilter> result = new ArrayList<>();
        if (treeType == null) {
            return result;
        }
        List<ExternalTreeFilter> filtersList = getTreeFiltersInternal(treeType);
        result.addAll(filtersList);
        return result;
    }
}
