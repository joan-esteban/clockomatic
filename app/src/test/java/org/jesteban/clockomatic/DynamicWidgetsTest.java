package org.jesteban.clockomatic;

import org.jesteban.clockomatic.helpers.DynamicWidgets;
import org.junit.Test;

public class DynamicWidgetsTest {
    public DynamicWidgets<String> dw = new DynamicWidgets<>(new DynamicWidgets.OnMyActions<String>() {
        @Override
        public String createWidget(int idx) {
            return new String("hello");
        }

        @Override
        public void removeWidgetFromView(String widget) {
            // nothing
        }
    });
    @Test
    public void happy_path(){
        dw.setAllWidgetAsUnused();
        dw.getWidget(0);
        dw.removeWidgetUnused();
    }

    @Test
    public void must_remove_widgets(){
        dw.setAllWidgetAsUnused();
        dw.getWidget(0);
        dw.getWidget(1);
        dw.setAllWidgetAsUnused();
        dw.removeWidgetUnused();
    }
}
