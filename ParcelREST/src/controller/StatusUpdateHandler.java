package controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.DbHandler;

/**
 * Methode GET: recoit le format et contenu du code associe au colis dont il faut mettre a jour le statut,
 * demande la mise a jour au modele et renvoie le nouveau statut du colis.
 */
@Path("/update/{format}/{content}")
public class StatusUpdateHandler {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String updateStatus(@PathParam("format") String format, @PathParam("content") String content) {
		return new DbHandler().updateStatus(format, content);
	}

}
