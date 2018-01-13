package org.jesteban.clockomatic;

import org.jesteban.clockomatic.providers.Provider;
import org.jesteban.clockomatic.helpers.DependencyInjectorBinding;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class DependencyInjectorBindingTest {
    public static class  ConsumerExampleBinding{
        public ExampleProvider example;

        public void setExampleProvider(ExampleProvider smi){
            example = smi;
        }

    }

    public static class  ConsumerExampleBindingExtendedWithAnother extends ConsumerExampleBinding{
        public AnotherProvider another;
        public void setAnotherProvider(AnotherProvider smi) { another = smi;};
    }

    public interface ExampleProvider extends org.jesteban.clockomatic.providers.Provider {
        public static final String KEY_PROVIDER ="example";

        public boolean setExample(String txt);
        public String getExample();

        void subscribe(Listener listner);

        public interface Listener {
            void onChangeExample(ExampleProvider owner);
        }
    }

    public interface AnotherProvider extends org.jesteban.clockomatic.providers.Provider {


    }

    public static class AnotherProviderImpl implements AnotherProvider {

        @Override
        public String getName() {
            return null;
        }
    }

    public static class ExampleProviderImpl implements ExampleProvider {

        @Override
        public boolean setExample(String txt) {
            return false;
        }

        @Override
        public String getExample() {
            return null;
        }

        @Override
        public void subscribe(Listener listner) {

        }


        @Override
        public String getName() {
            return null;
        }
    }

    public static class Provider2 implements ExampleProvider {

        @Override
        public boolean setExample(String txt) {
            return false;
        }

        @Override
        public String getExample() {
            return null;
        }

        @Override
        public void subscribe(Listener listner) {

        }

        @Override
        public String getName() {
            return null;
        }
    }
    @Test
    public void happy_path(){
        DependencyInjectorBinding dib =new DependencyInjectorBinding();
        ConsumerExampleBinding consumer = new ConsumerExampleBinding();
        ExampleProviderImpl provider = new ExampleProviderImpl();
        Provider2 provider2 = new Provider2();

        dib.inject(consumer,provider );
        assertTrue(consumer.example == provider);

    }
    @Test
    public void inject_multiples_providers(){
        DependencyInjectorBinding dib =new DependencyInjectorBinding();
        ConsumerExampleBindingExtendedWithAnother consumer = new ConsumerExampleBindingExtendedWithAnother();
        ExampleProviderImpl provider = new ExampleProviderImpl();
        AnotherProviderImpl provider2 = new AnotherProviderImpl();
        List<Provider> providers = new ArrayList<>();
        providers.add(provider);
        providers.add(provider2);
        dib.inject(consumer,providers );
        assertTrue(consumer.example == provider);
        assertTrue(consumer.another == provider2);
    }
}
