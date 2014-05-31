package dbHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.util.Coordinate;
import GMapClient.MapClient;
/**
 * Cette classe se charge de traduire les ordres de commandes valides
 * enregistres en base de donnees en un PDP resolvable.
 */
public class ProblemBuilder {
	
	private Connection connection;
	private List<Double> coordinates;
	private List<Integer> masses;

	/**
	 * Constructeur: ouvre une connexion avec la base de donnees.
	 * @see Main.JspritTest.Main#main(String[])
	 */
	public ProblemBuilder(){
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
	 * Methode privee chargee de recuperer dans la base les donnees necessaires 
	 * a la specification du PDP.
	 * @throws SQLException
	 * @throws IOException
	 * @see ProblemBuilder#build()
	 */
	private void fetch() throws SQLException, IOException {
		List<String> addresses = new ArrayList<String>();
		this.coordinates = new ArrayList<Double>();
		this.masses = new ArrayList<Integer>();
		Statement statement = this.connection.createStatement();
		/*
		 * On recupere les adresses des emetteurs et destinataires, ainsi que la masse totale du colis circulant entre eux,
		 * et ce pour tous les ordres de transport valables.
		 */
		ResultSet result = statement.executeQuery("SELECT C1.address_street, C1.address_number, C1.city, C1.region, C1.state_department, C1.postal_code, "
				+ "C2.address_street, C2.address_number, C2.city, C2.region, C2.state_department, C2.postal_code, P.mass_kg "
				+ "FROM client C1, client C2, orders O, parcel P "
				+ "WHERE O.to_date > CURRENT_TIMESTAMP AND "
				+ "O.sender_id = C1.client_id AND "
				+ "O.receiver_id = C2.client_id AND "
				+ "P.order_id = O.order_id");
		while(result.next()){
			for(int i = 1; i <= 12; i++){
				addresses.add(result.getString(i));
			}
			this.masses.add((int) (result.getDouble(13) + 1));			
		}
		/*
		 * Conversion des adresses telles que enregistrees en base vers des coordonnees.
		 */
		this.coordinates = new MapClient(addresses).getCoordinates();
		this.connection.close();
	}
	
	/**
	 * Methode publique dont le role est de construire le probleme.
	 * @return Le PDP pret a etre resolu.
	 * @throws SQLException
	 * @throws IOException
	 * @see Main.JspritTest.Main#main(String[])
	 */
	public VehicleRoutingProblem build() throws SQLException, IOException{
		/*
		 * Appel a la methode de recherche des donnees en base.
		 */
		this.fetch();
		
		/*
		 * Definition de la flotte: nous considerons trois types de vehicules caracterises par leur capacite et leur cout d'utilisation.
		 * Nous considerons une flotte de 5 vehicules (2 du type 1, 2 du type 2, 1 du type 3).
		 * Chaque vehicule commence sa route au depot et est operationnel lors de la meme plage de temps. 50.827144, 4.366822
		 */
		VehicleTypeImpl vehicleType1 = VehicleTypeImpl.Builder.newInstance("type1").addCapacityDimension(0,120).setCostPerDistance(1.0).build();
		VehicleImpl vehicle1_1 = VehicleImpl.Builder.newInstance("vehicle1_1").setStartLocationCoordinate(Coordinate.newInstance(50.8422767* 1000, 4.2994471* 1000)).setEarliestStart(0).setLatestArrival(500).setType(vehicleType1).build();
		VehicleImpl vehicle1_2 = VehicleImpl.Builder.newInstance("vehicle1_2").setStartLocationCoordinate(Coordinate.newInstance(50.8422767* 1000, 4.2994471* 1000)).setEarliestStart(0).setLatestArrival(500).setType(vehicleType1).build();
		
		VehicleTypeImpl vehicleType2 = VehicleTypeImpl.Builder.newInstance("type2").addCapacityDimension(0,500).setCostPerDistance(3).build();
		VehicleImpl vehicle2_1 = VehicleImpl.Builder.newInstance("vehicle2_1").setStartLocationCoordinate(Coordinate.newInstance(50.8422767* 1000, 4.2994471* 1000)).setEarliestStart(0).setLatestArrival(500).setType(vehicleType2).build();
		VehicleImpl vehicle2_2 = VehicleImpl.Builder.newInstance("vehicle2_2").setStartLocationCoordinate(Coordinate.newInstance(50.8422767* 1000, 4.2994471* 1000)).setEarliestStart(0).setLatestArrival(500).setType(vehicleType2).build();

		VehicleTypeImpl vehicleType3 = VehicleTypeImpl.Builder.newInstance("type3").addCapacityDimension(0,1000).setCostPerDistance(7).build();
		VehicleImpl vehicle3 = VehicleImpl.Builder.newInstance("vehicle3").setStartLocationCoordinate(Coordinate.newInstance(50.8422767* 1000, 4.2994471* 1000)).setEarliestStart(0).setLatestArrival(500).setType(vehicleType3).build();

		/*
		 * Instanciation du constructeur de probleme de routage et ajout des informations concernant la flotte.
		 */
		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addVehicle(vehicle1_1).addVehicle(vehicle1_2).addVehicle(vehicle2_1).addVehicle(vehicle2_2).addVehicle(vehicle3);
		vrpBuilder.addPenaltyVehicles(5.0, 5000);
		vrpBuilder.setFleetSize(FleetSize.FINITE);
		
		/*
		 * Definition des services a delivrer: pour chaque service nous specifions la masse a transporter, les coordonnees
		 * du point de prise en charge et du point de livraison ainsi que la duree de chaque service.
		 */
		int coordinatesCounter = 0;
		for(int massesCounter = 0; massesCounter < this.masses.size(); massesCounter ++){
			System.out.println("Livraison n° " + massesCounter + "Coordonnées de l'expéditeur: " + Coordinate.newInstance(this.coordinates.get(coordinatesCounter), this.coordinates.get(coordinatesCounter + 1)) + "Coordonnées du destinataire: " + Coordinate.newInstance(this.coordinates.get(coordinatesCounter + 2), this.coordinates.get(coordinatesCounter + 3)) + "Poids du colis: " + this.masses.get(massesCounter));
			vrpBuilder.addJob(Shipment.Builder.newInstance(Integer.toString(massesCounter)).addSizeDimension(0, this.masses.get(massesCounter)).setPickupCoord(Coordinate.newInstance(this.coordinates.get(coordinatesCounter) * 1000, this.coordinates.get(coordinatesCounter + 1) * 1000 )).setPickupServiceTime(1).setDeliveryCoord(Coordinate.newInstance(this.coordinates.get(coordinatesCounter + 2) * 1000 , this.coordinates.get(coordinatesCounter + 3) * 1000)).setDeliveryServiceTime(1).build());
			coordinatesCounter += 4;
		}
		
		/*
		 * Construction du probleme a proprement parler.
		 */
		return vrpBuilder.build();	
	}

}
