package org.jesteban.clockomatic;

import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ObservableDispatcherTest {
    public interface TestListener{
        void onHello();
        void onBye(String bye);
    }

    public class Observer implements TestListener{
        public boolean isCalled = false;
        public String bye = null;
        @Override
        public void onHello() {
            isCalled = true;
        }
        @Override
        public void onBye(String bye){
            this.bye = bye;
        }
    }

    @Test
    public void test_without_params() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Observer myObserver = new Observer();
        ObservableDispatcher<TestListener> od = new ObservableDispatcher<>();
        od.add(myObserver);
        od.notify("onHello");
        assertTrue(myObserver.isCalled);

    }

    @Test
    public void test_with_1_param() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Observer myObserver = new Observer();
        ObservableDispatcher<TestListener> od = new ObservableDispatcher<>();
        od.add(myObserver);
        od.notify("onBye","bye" );
        assertEquals("bye",myObserver.bye);

    }
}
