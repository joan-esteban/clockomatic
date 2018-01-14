package org.jesteban.clockomatic.helpers;
import org.jesteban.clockomatic.providers.Provider;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * get list of implementations ending with "Provider"
 * and find members on class starting wint set<name_interface>
 * Example:
 *  interface SelectedMonthProviderContract
 *
 *  On consumer
 *  public void setSelectedMonthProvider(SelectedMonthProviderContract i);
 */

public class DependencyInjectorBinding {
    private static final Logger LOGGER = Logger.getLogger(DependencyInjectorBinding.class.getName());
    private static final String PROVIDER_SIGNATURE="Provider";
    private static final String METHOD_START_WITH="set";
    private boolean isMethodForBinding(Method method){
        return method.getName().endsWith(PROVIDER_SIGNATURE) && method.getName().startsWith(METHOD_START_WITH) ;
    }

    public List<Method> getProviderMethods(Object consumer){
        ArrayList<Method> result = new ArrayList<>();
        Method[] methods = consumer.getClass().getMethods();
        for (Method method : methods){
            LOGGER.info( method.getName() );
            if (isMethodForBinding(method)) result.add(method);
        }
        return result;
    }

    public String getInterfaceClassName(String methodName){
        return methodName.substring(METHOD_START_WITH.length());
    }

    public Class getInterfaceClass(Class cls, String interfaceClassName){
        Class[] classes = cls.getInterfaces();
        for (Class cls2 : classes){
            LOGGER.info("provider has: " + cls2.getName());
            if (cls2.getName().endsWith(interfaceClassName)){
                LOGGER.info("find a suitable source " + cls2.getName());
                return cls2;
            }
            if (cls2.getName().endsWith(interfaceClassName+"Contract")){
                LOGGER.info("find a suitable source " + cls2.getName());
                return cls2;
            }
        }
        return null;
    }

    private void invoke(Object consumer, Method method, Provider provider, Class cls){
        try {
            LOGGER.info("calling method: " + method.getName() + " / " + cls.getName() + " over obj " + consumer.getClass());
            method.invoke(consumer, cls.cast(provider));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception invoking " + e);
            throw new RuntimeException(e.toString());
        }
    }

    private void fullfillMethod(Object consumer, Method method, List<Provider> providers){
        LOGGER.info("looking for a class for call method: " + method.getName());
        String interfaceClassName = getInterfaceClassName(method.getName());
        for (Provider provider : providers){
            Class cls = getInterfaceClass(provider.getClass(), interfaceClassName);
            if (cls != null){
                invoke(consumer, method,provider,cls);
                return;
            }
        }
        throw new RuntimeException("I dont have any Provider for method " + method.getName());
    }

    public void inject(Object consumer, List<Provider> providers){
        List<Method> methodToFill = getProviderMethods(consumer);
        for (Method method : methodToFill) {
            fullfillMethod(consumer,method,providers);
        }
    }

    public void inject(Object consumer, Provider provider){
        List<Provider> providers = new ArrayList<>();
        providers.add(provider);
        inject(consumer, providers);
    }
}
