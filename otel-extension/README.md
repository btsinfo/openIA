# OpenTelemetry Extension for webMethods Integration Server

This module provides a sample Java Servlet `Filter` that creates OpenTelemetry
traces for incoming HTTP requests handled by Software AG webMethods Integration
Server.

## Building

This project uses Maven. To build the JAR run:

```sh
mvn clean package
```

## Usage

1. Ensure the OpenTelemetry SDK is configured via the `opentelemetry-javaagent`
   or by setting up the SDK in your environment.
2. Drop the resulting JAR in your Integration Server's `lib/jars` directory
   and register the filter in the `web.xml` of your custom package.

This basic example demonstrates how to start and finish spans around
HTTP requests. You can extend it to capture additional attributes
or integrate with other parts of the ESB.
