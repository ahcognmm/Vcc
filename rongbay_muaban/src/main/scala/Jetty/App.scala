package Jetty

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler

object App {

    def main(args: Array[String]): Unit = {
        val context: ServletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)
        context.setContextPath("/")

        val jettyServer = new Server(1507)
        jettyServer.setHandler(context)
        //
        val jerseyServlet = context.addServlet(classOf[org.glassfish.jersey.servlet.ServletContainer], "/*")

        jerseyServlet.setInitParameter(
            "jersey.config.server.provider.classnames",
            classOf[EntryPoint].getCanonicalName
        )
        try {
            jettyServer.start()
            jettyServer.join()
        } finally {
            jettyServer.destroy()
        }
    }

}
