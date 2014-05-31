package dbHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;

/**
 * Cette classe se charge d'enregistrer un itineraire fourni par PDPSolver en base de donnees.
 */
public class RouteStorer {
	
	private Connection connection;
	
	/**
	 * Constructeur: ouvre une connexion avec la base de donnees.
	 * @see solver.PDPSolver#PDPSolver(jsprit.core.problem.VehicleRoutingProblem)
	 */
	public RouteStorer(){
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "parcels";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "";
		try {
			Class.forName(driver).newInstance();
			this.connection = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) {
			System.out.println("Error establishing connection to DB.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode gerant l'enregistrement de l'itineraire contenu dans la solution donnee.
	 * @see solver.PDPSolver#PDPSolver(jsprit.core.problem.VehicleRoutingProblem)
	 * @param solution La solution d'un PDP.
	 * @throws SQLException
	 */
	public void store(VehicleRoutingProblemSolution solution) throws SQLException{
		/*
		 * Une solution decrit un ensemble de routes: chaque route concerne un vehicule.
		 */
		for (VehicleRoute route : solution.getRoutes()){
			/**
			 * Appel de la methode d'enregistrement d'une route en base de donnees
			 * et sauvegarde de l'ID de la route inseree pour utilisation ulterieure
			 * lors de l'insertion des etapes constituant la route.
			 */
			int routeID = this.insertRoute(route.getVehicle().getId());
			int stepNumber = 1;
			/*
			 * Chaque route est constituee d'un certain nombre d'etapes.
			 */
			for (TourActivity step : route.getActivities()){
				double arrivalTime = step.getArrTime();
				double departureTime = step.getEndTime();
				/*
				 * Les coordonnees sont presentees dans la solution sous la forme de string au format [x=lat][y=long].
				 * Un peu de traitement est necessaire pour les obtenir sous forme de double.		 
				 */
				List<String> locs = new ArrayList<String>();
				locs.add(step.getLocationId().substring(step.getLocationId().indexOf("x") + 2, step.getLocationId().indexOf("y") - 2));
				locs.add(step.getLocationId().substring(step.getLocationId().indexOf("y") + 2).replace("]", ""));
				double latitude = Double.parseDouble(locs.get(0))/1000;
				double longitude = Double.parseDouble(locs.get(1))/1000;
				/*
				 * Appel de la methode d'enregistrement d'etape.
				 */
				this.insertStep(routeID, stepNumber, arrivalTime, departureTime, latitude, longitude);
				stepNumber ++;
			}
		}
		this.connection.close();
	}

	/**
	 * Mehode d'enregistrement d'etape: pour chaque etape il faut retenir la localisation (lat-long), 
	 * le moment d'arrivee et le moment de depart theoriques, la route dont l'etape fait partie,
	 * ainsi que l'ordre de visite de l'etape.
	 * @param routeID La route dont l'etape fait partie.
	 * @param stepNumber L'ordre de visite de l'etape.
	 * @param arrivalTime Le moment d'arrivee theorique.
	 * @param departureTime Le moment de depart theorique.
	 * @param latitude La latitude.
	 * @param longitude La longitude.
	 * @throws SQLException
	 * @see {@link RouteStorer#store(VehicleRoutingProblemSolution)}
	 */
	private void insertStep(int routeID, int stepNumber, double arrivalTime, double departureTime, double latitude, double longitude) throws SQLException {
		this.connection.createStatement().executeUpdate("INSERT INTO step (route_id, latitude, longitude, step_number, departure_time, arrival_time) "
				+ "VALUES (" + routeID + ", " + latitude + ", " + longitude + ", " + stepNumber + ", " + departureTime + ", " + arrivalTime + ")");
	}

	/**
	 * Methode d'insertion de route, la seule valeur a specifier est l'identifiant du vehicule conseille 
	 * pour effectuer la route.
	 * @param vehiculeName Un identifiant du vehicule conseille pour effectuer la route.
	 * @return l'ID d'insertion dans la base de donnees de la route, de maniere a pouvoir ulterieurement
	 * lui associer les etapes correspondantes.
	 * @throws SQLException
	 */
	private int insertRoute(String vehiculeName) throws SQLException {
		/*
		 * Insertion de la route en base de donnees: nous considerons qu'une route n'est valable que sur 
		 * une plage de temps de 15 heures a partir de son moment de creation. 
		 * Nous imaginons que les routes sont generees chaque jour tot dans la journee, puis parcourues
		 * le jour lors de leur periode de validite. Il faut generer de nouvelles routes chaque matin.
		 */
		String query = "INSERT INTO route (vehicule_name, to_date) "
				+ "VALUES ('" + vehiculeName + "', DATE_ADD(current_timestamp, INTERVAL 15 HOUR))";
		PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pstmt.executeUpdate();  
		ResultSet keys = pstmt.getGeneratedKeys();    
		keys.next();  
		return keys.getInt(1);
	}
	
}
