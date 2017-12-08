package org.jesteban.clockomatic.helpers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a generic class for inject dependencies in objects
 */

public class DependencyInjector<INJECTABLE_OBJECT> {
    private static final Logger LOGGER = Logger.getLogger(DependencyInjector.class.getName());
    public interface Injectable<INJECTABLE_OBJECT>{
        public boolean setDependency(INJECTABLE_OBJECT dependency);
    }

    public boolean inject(INJECTABLE_OBJECT dependency, Object object){
        try {
            Injectable<INJECTABLE_OBJECT> iny = (Injectable<INJECTABLE_OBJECT>) object;
            LOGGER.log(Level.INFO, String.format("DependencyInjector.inject injecnting %s to %s", dependency, object));
            return iny.setDependency(dependency);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, String.format("Object [%s] is not Inyectable ", object));
        }
        return false;
    }

    public boolean injectList(INJECTABLE_OBJECT dependency, List<? extends Object> objectsList){
        LOGGER.log(Level.INFO, String.format("inject list objects (%s)", objectsList));
        if (objectsList==null) return false;
        boolean result = false;
        for (Object obj : objectsList) {
            result |= inject(dependency, obj);
        }
        return result;
    }

}
