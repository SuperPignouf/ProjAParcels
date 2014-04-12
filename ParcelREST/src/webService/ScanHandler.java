package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.DbHandler;

//path is "http://localhost:8080/ParcelREST/rest/hello/111111/roger"
//or "http://192.168.1.5:8080/ParcelREST/rest/hello/111111/roger"
//type ipconfig in console to know ip address
@Path("/scan/{format}/{content}")
public class ScanHandler {	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("format") String format, @PathParam("content") String content) {
		//TODO chiffrement ?
		return new DbHandler().scanningProduct(format, content);
	}
}
