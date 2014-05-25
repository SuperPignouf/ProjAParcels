package webService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.DbHandler;

/**
 * Methode GET: recoit le format et contenu du code associe au colis dont il faut mettre a jour le statut,
 * ainsi que les coordonnees de scan.
 * Demande la mise a jour au modele et renvoie le nouveau statut du colis.
 */
@Path("/update/{format}/{content}/{latitude}/{longitude}")
public class StatusUpdateHandler {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String updateStatus(@PathParam("format") String format, @PathParam("content") String content, @PathParam("latitude") String latitude, @PathParam("longitude") String longitude) {
		return new DbHandler().updateStatus(format, content, latitude, longitude);
	}

}
