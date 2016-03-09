package com.geniusmart.loveut.sqlite;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.internal.bytecode.InstrumentationConfiguration;

public class DatabaseTestRunner extends RobolectricGradleTestRunner {

    public DatabaseTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public InstrumentationConfiguration createClassLoaderConfig() {
        InstrumentationConfiguration.Builder builder = InstrumentationConfiguration.newBuilder();
        builder.addInstrumentedClass(AccountDBHelper.class.getName());
        return builder.build();
    }
}

