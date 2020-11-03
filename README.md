# Print Server for APEX based on Apache FOP

This is the implementation of a simple print server to generate PDF from APEX reports.
It is based on the following open source projects:

- Apache FOP
- Jetty

It needs Apache Maven 3 and JDK 1.8+ to compile and run.

Compile and package with:

> mvn clean package

Run with:

> java -jar printserver-1.0-SNAPSHOT-jar-with-dependencies.jar

Running printserver without any other parameter starts a print server on port 9999.

The listening port is configurable with (in order of precedence):

1) System property by adding the -Dfop.server.port=port_number

    > java -Dfop.server.port=port_number -jar printserver-1.0-SNAPSHOT-jar-with-dependencies.jar
2) Environment variable FOP_SERVER_PORT=port_number

    > FOP_SERVER_PORT=port_number java -jar printserver-1.0-SNAPSHOT-jar-with-dependencies.jar
3) Command line parameter

    > java -jar printserver-1.0-SNAPSHOT-jar-with-dependencies.jar port_number
