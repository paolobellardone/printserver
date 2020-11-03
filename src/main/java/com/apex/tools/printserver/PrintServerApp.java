package com.apex.tools.printserver;

/**
 *  Copyright 2020 PaoloB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * Servlet to generate a PDF <br>
 * Servlet parameters are:
 * <ul>
 * <li>xml: the path to an XML file to render</li>
 * <li>template: the path to an XSLT file that can transform the above XML to
 * XSL-FO</li>
 * </ul>
 * <br>
 * The servlet supports only POST on path / and it runs by default on port 9999
 * <br>
 * To change the listening port please do one of the following (in order of
 * priority):
 * <ul>
 * <li>Use a system variable when invoking the jar file, i.e. java
 * -Dfop.server.port=9999 -jar printserver.jar</li>
 * <li>Use an environment variable, i.e. export FOP_SERVER_PORT=9999</li>
 * <li>Use a parameter when invoking the jar file, i.e. java -jar
 * printserver.jar 9999</li>
 * </ul>
 * <br>
 */
public class PrintServerApp {

    private static final Logger LOG = Log.getLogger(FopServlet.class);

    protected static int fopServerPort = 0;

    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new FopServlet()), "/*");

        if (System.getProperty("fop.server.port") != null) {
            fopServerPort = Integer.parseInt(System.getProperty("fop.server.port"));
            LOG.info("System property fop.server.port found. FopServlet is listening on port: {}", fopServerPort);
        } else if (System.getenv("FOP_SERVER_PORT") != null) {
            fopServerPort = Integer.parseInt(System.getenv("FOP_SERVER_PORT"));
            LOG.info("Enviroment variable FOP_SERVER_PORT found. FopServlet is listening on port: {}", fopServerPort);
        } else if (args.length > 0) {
            fopServerPort = Integer.parseInt(args[0]);
            LOG.info("Port parameter found. FopServlet is listening on port: {}", fopServerPort);
        } else {
            fopServerPort = 9999;
            LOG.info("No custom port specification found. FopServlet is listening on default port: {}", fopServerPort);
        }

        Server server = new Server(fopServerPort);
        server.setHandler(context);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                LOG.info("Stopping print server...");
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        server.start();
        server.join();


    }

}
