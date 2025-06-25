package com.example.webmethods.instrumentation;

import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

import java.util.Collections;
import java.util.List;

/**
 * Instrumentation module registering the ServiceManager invoke advice.
 */
public class ServiceInvokeInstrumentationModule extends InstrumentationModule {

    public ServiceInvokeInstrumentationModule() {
        super("webmethods-service-invoke");
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        return Collections.singletonList(new ServiceInvokeInstrumentation());
    }
}
