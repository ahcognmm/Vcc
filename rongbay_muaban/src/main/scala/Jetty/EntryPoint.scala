package Jetty

import handle.Result
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}


@Path("/rongbay")
class EntryPoint {

    @GET
    @Path("/test")
    @Produces(Array(MediaType.TEXT_PLAIN))
    def hello(@QueryParam("idNews") idNews : String): Response={
        val rs = new Result
        Response.ok(rs.getInfor(idNews),MediaType.TEXT_PLAIN).build()
    }

}
