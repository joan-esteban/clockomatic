package org.jesteban.clockomatic.helpers;

import java.util.ArrayList;

/**
 * This class let reuse widgets
 * - setAllWidgetAsUnused()
 * - getWidget() as many as you need (always in order! starting at 0)
 * - removeWidgetUnused()
 */

public class DynamicWidgets<T> {


    public interface OnMyActions<T> {
        public T createWidget(int idx);

        public void removeWidgetFromView(T widget);
    }

    private class WidgetStatus {
        T widget;
        Boolean inUse;
    }

    private ArrayList<WidgetStatus> widgets = new ArrayList<>();
    private OnMyActions<T> actions = null;

    public DynamicWidgets(OnMyActions<T> pActions) {
        actions = pActions;
    }

    public void setAllWidgetAsUnused() {
        for (WidgetStatus st : widgets) st.inUse = false;
    }

    public T getWidget(int idx) {
        if (widgets == null) {
            widgets = new ArrayList<>();
        }
        if (widgets.size() > idx) {
            WidgetStatus wStatus = widgets.get(idx);
            wStatus.inUse = true;
            return wStatus.widget;
        }
        WidgetStatus wStatus = new WidgetStatus();
        wStatus.inUse = true;
        wStatus.widget = actions.createWidget(idx);
        widgets.add(idx, wStatus);
        return wStatus.widget;
    }

    public void removeWidgetUnused() {
        for (WidgetStatus st : widgets) {
            if (!st.inUse) {
                actions.removeWidgetFromView(st.widget);
            }
        }
    }
}
