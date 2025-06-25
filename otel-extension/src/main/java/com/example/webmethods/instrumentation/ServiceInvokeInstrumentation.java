package com.example.webmethods.instrumentation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArgument;

/**
 * Byte Buddy instrumentation for ServiceManager.invoke.
 */
public class ServiceInvokeInstrumentation implements TypeInstrumentation {

    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return named("com.wm.app.b2b.server.ServiceManager");
    }

    @Override
    public void transform(TypeTransformer transformer) {
        transformer.applyAdviceToMethod(named("invoke").and(takesArgument(0, String.class)),
                ServiceInvokeInstrumentation.class.getName() + "$InvokeAdvice");
    }

    @SuppressWarnings("unused")
    public static class InvokeAdvice {
        private static final Tracer tracer =
                GlobalOpenTelemetry.getTracer("webmethods-otel-extension");

        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.Argument(0) String serviceName,
                                   @Advice.Local("span") Span span,
                                   @Advice.Local("scope") Scope scope) {
            span = tracer.spanBuilder(serviceName)
                    .setSpanKind(SpanKind.INTERNAL)
                    .startSpan();
            scope = span.makeCurrent();
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
        public static void onExit(@Advice.Local("span") Span span,
                                  @Advice.Local("scope") Scope scope,
                                  @Advice.Thrown Throwable thrown) {
            if (scope != null) {
                scope.close();
            }
            if (thrown != null) {
                span.recordException(thrown);
            }
            span.end();
        }
    }
}
