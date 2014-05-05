package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.DbHandler;

/**
 * Methode GET, recoit le format du code et le contenu du code d'un colis et renvoie les informations de ce colis.
 */
@Path("/scan/{format}/{content}")
public class ScanHandler {	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String scan(@PathParam("format") String format, @PathParam("content") String content) {
		return new DbHandler().scanningProduct(format, content);
	}
}
