package org.jesteban.clockomatic.helpers;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a list of subscriptor and inovke callback
 */

public class ObservableDispatcher<T> {
    private static final Logger LOGGER = Logger.getLogger(ObservableDispatcher.class.getName());
    private List<T> observers = new ArrayList<>();
    public void add(T observer){
        observers.add(observer);
    }

    Class<?> [] getClassForParams(Object...params){
        Class<?> [] list = new Class<?>[params.length];
        int idx = 0;
        for (Object obj : params){
            LOGGER.info(" obj " + obj.getClass());
            list[idx] = obj.getClass();
            idx++;
        }
        return list;
    }

    public void notify(String methodName,Object...params) throws RuntimeException {
        for (T observer : observers){

            try {

                Class<?> [] list = getClassForParams(params);
                Method method = observer.getClass().getMethod(methodName,list);

                LOGGER.info("calling method: " + method.getName() + " / " + observer.getClass().getName());
                LOGGER.info("param len  " + Integer.toString(params.length));
                method.invoke(observer, params);

            }catch (IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Exception IllegalAccessException invoking " + e);
                throw(new RuntimeException(e.toString()));
            } catch(IllegalArgumentException e){
                LOGGER.log(Level.SEVERE, "Exception IllegalArgumentException invoking " + e);
                throw(new RuntimeException(e.toString()));
            }catch (InvocationTargetException e) {
                LOGGER.log(Level.SEVERE, "Exception InvocationTargetException invoking ["+ observer.getClass().getName() + "." + methodName + "] exception:" + e);
                throw(new RuntimeException(e.toString()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, "Exception NoSuchMethodException invoking " + e);
                throw(new RuntimeException(e.toString()));
            }
        }
    }

}
