package com.geniusmart.loveut.myshadow;

import com.geniusmart.loveut.shadow.Person;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.internal.bytecode.InstrumentationConfiguration;

public class CustomShadowTestRunner extends RobolectricGradleTestRunner {

    public CustomShadowTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public InstrumentationConfiguration createClassLoaderConfig() {
        InstrumentationConfiguration.Builder builder = InstrumentationConfiguration.newBuilder();
        /**
         * 添加要进行Shadow的对象
         */
        builder.addInstrumentedPackage(Person.class.getPackage().getName());
        builder.addInstrumentedClass(Person.class.getName());

        return builder.build();
    }
}

