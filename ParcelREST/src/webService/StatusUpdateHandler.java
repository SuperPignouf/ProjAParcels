package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.DbHandler;

@Path("/update/{format}/{content}")
public class StatusUpdateHandler {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String updateStatus(@PathParam("format") String format, @PathParam("content") String content) {
		//TODO chiffrement ?
		return new DbHandler().updateStatus(format, content);
	}

}
