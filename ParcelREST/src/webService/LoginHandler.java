package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.DbHandler;

/**
 * Methode GET: reçoit les infos de login et renvoie 1 ou 0 en fonction de la validite de l'utilisateur
 */
@Path("/login/{matricule}/{password}")
public class LoginHandler {	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam("matricule") int matricule, @PathParam("password") String password) {
		//TODO chiffrement
		return new DbHandler().isRealUser(matricule, password);
	}
}
