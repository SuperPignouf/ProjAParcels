package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import database.DbHandler;

// path is "http://localhost:8080/ParcelREST/rest/login/111111/roger"
// or "http://192.168.1.8:8080/ParcelREST/rest/login/111111/roger"
// type ipconfig in console to know ip address
@Path("/login/{matricule}/{password}")
public class LoginHandler {	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("matricule") int matricule, @PathParam("password") String password) {
		//TODO chiffrement ?
		return new DbHandler().isRealUser(matricule, password);
	}
}
