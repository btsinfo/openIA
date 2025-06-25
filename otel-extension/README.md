# OpenTelemetry Extension for webMethods Integration Server

This module provides a sample Java Servlet `Filter` that creates OpenTelemetry
traces for incoming HTTP requests handled by Software AG webMethods Integration
Server.

It also contains a Byte Buddy instrumentation module that creates spans around
calls to `com.wm.app.b2b.server.ServiceManager.invoke` when loaded by the
OpenTelemetry Java agent.

## Building

This project uses Maven. To build the JAR run:

```sh
mvn clean package
```

The build relies on the `javax.servlet-api` dependency which is declared in the
`pom.xml`. Most servlet containers already provide this library at runtime, so
the dependency is marked with scope `provided`.

## Usage

1. Ensure the OpenTelemetry SDK is configured via the `opentelemetry-javaagent`
   or by setting up the SDK in your environment.
2. Drop the resulting JAR in your Integration Server's `lib/jars` directory
   and register the filter in the `web.xml` of your custom package. Add entries
   similar to:

   ```xml
   <filter>
       <filter-name>otelFilter</filter-name>
       <filter-class>com.example.webmethods.OtelFilter</filter-class>
   </filter>
   <filter-mapping>
       <filter-name>otelFilter</filter-name>
       <url-pattern>/*</url-pattern>
   </filter-mapping>
   ```

This basic example demonstrates how to start and finish spans around
HTTP requests. You can extend it to capture additional attributes
or integrate with other parts of the ESB.

When used together with the `opentelemetry-javaagent`, the included
instrumentation module will automatically trace calls to
`ServiceManager.invoke` without further configuration.
