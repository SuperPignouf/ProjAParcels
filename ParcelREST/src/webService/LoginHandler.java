package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.DbHandler;

// path is "http://localhost:8080/ParcelREST/rest/hello/111111/roger"
@Path("/hello/{matricule}/{password}")
public class LoginHandler {	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("matricule") int matricule, @PathParam("password") String password) {
		//TODO chiffrement ?
		return new DbHandler().isRealUser(matricule, password);
	}
}
