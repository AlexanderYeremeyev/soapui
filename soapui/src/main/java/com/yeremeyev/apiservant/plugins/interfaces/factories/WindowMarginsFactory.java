package com.yeremeyev.apiservant.plugins.interfaces.factories;

import com.yeremeyev.apiservant.plugins.interfaces.margins.WindowMargin;
import com.yeremeyev.apiservant.plugins.interfaces.margins.WindowType;

import java.util.List;

/**
 * factory to create WindowMargin
 */
public interface WindowMarginsFactory {

    /**
     * @return list of supported margins
     */
    List<WindowType> getSupportTypesList();

    /**
     * @param windowType
     * @return true is windowType is supported
     */
    boolean isAvailable(WindowType windowType);

    /**
     *
     * @param windowType
     * @return new created windowType margin
     */
    WindowMargin create(WindowType windowType);
}
