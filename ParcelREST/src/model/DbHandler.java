package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import email.EmailSender;

/**
 * Gere la connexion et les requetes a la base de donnees.
 */
public class DbHandler {

	private Connection connection;

	/**
	 * Constructeur: ouvre une connexion avec la base de donnees.
	 * @see webService.LoginHandler#login(int, String)
	 * @see webService.ScanHandler#scan(String, String)
	 * @see webService.StatusUpdateHandler#updateStatus(String, String)
	 */
	public DbHandler(){
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
	 * Verifie avec la base de donnees si l'utilisateur associe au matricule
	 * et au mot de passe specifies existe.
	 * @param matricule Le matricule de l'utilisateur: nombre a 6 chiffres
	 * @param password Le mot de passe de la personne
	 * @return Un string valant 1 si l'utilisateur existe, 0 autrement.
	 * @see webService.LoginHandler#login(int, String)
	 */
	public String isRealUser(int matricule, String password) {
		boolean isValid = false;
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			// On verifie dans la base de donnees si l'utilisateur existe.
			result = statement.executeQuery("SELECT * FROM user WHERE matricule = " + matricule + " AND password = '" + password + "'");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}
		try {
			// Si le result set n'est pas vide, l'utilisateur existe.
			while (result.next()) isValid = true;
		} catch (SQLException e) {
			System.out.println("Error parsing query result.");
			e.printStackTrace();
		}
		this.close(); // Fermeture de la connexion a la base de donnees.
		if (isValid) return "1"; 
		else return "0";
	}

	/**
	 * Ferme la connexion a la base de donnees.
	 * @see #isRealUser(int, String)
	 * @see #scanningProduct(String, String)
	 * @see #updateStatus(String, String)
	 */
	private void close(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("Error closing connection to DB.");
			e.printStackTrace();
		}
	}

	/**
	 * Verifie avec la base de donnees si le paquet associe au format et contenu
	 * specifies existe, s'il existe on cherche aussi le statut actuel du paquet.
	 * @param format Le format d'encodage du code barres ou RFID.
	 * @param content Le contenu du code barres ou de la puce RFID.
	 * @return Un string contenant une description du paquet et de son statut actuel s'il existe.
	 *  Sinon un message d'erreur.
	 * @see webService.ScanHandler#scan(String, String)
	 */
	public String scanningProduct(String format, String content) {
		String returnValue = "Item not found."; // Valeur par defaut
		String parcelID = "-1";

		Statement statement = null;
		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			//On va chercher le nom et la description du colis dans la base de donnnees, de meme que l'ID pour une requete future.
			result = statement.executeQuery("SELECT content, description, parcel_id FROM parcel WHERE scan_code = '" + content + "' AND scan_code_type = '" + format + "'");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}

		try {
			// Si le result set n'est pas vide, le colis existe.
			if (result.next())
				try {
					returnValue = result.getString(1) + ": " + result.getString(2); // On met le nom et la description dans le String de return.
					parcelID = result.getString(3); // Et on garde l'ID pour plus tard.
				} catch (SQLException e1) {
					System.out.println("Error parsing query result.");
					e1.printStackTrace();
				}
		} catch (SQLException e1) {
			System.out.println("Error parsing query result.");
			e1.printStackTrace();
		}

		try {
			// On recupere le statut le plus recent (=actuel) du colis dans la base de donnees.
			result = statement.executeQuery("SELECT PS.status FROM parcel_status PS WHERE PS.parcel_id = " + parcelID + " AND NOT EXISTS "
					+ "(SELECT * FROM parcel_status WHERE parcel_id = PS.parcel_id AND from_date > PS.from_date)");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}

		try {
			if (result.next())
				try {
					returnValue += " \n Current status: " + result.getString(1); // On ajoute le statut du colis au string de retour.
				} catch (SQLException e1) {
					System.out.println("Error parsing query result.");
					e1.printStackTrace();
				}
		} catch (SQLException e1) {
			System.out.println("Error parsing query result.");
			e1.printStackTrace();
		}

		this.close(); // On ferme la connexion avec la base de donnees.
		return returnValue;
	}

	/**
	 * Met a jour le statut du colis associe au couple format / content specifie
	 * en creant une nouvelle entree statut dans la table des statuts associee
	 * au colis.
	 * @param format Le format d'encodage du code barres ou RFID.
	 * @param longitude 
	 * @param latitude 
	 * @param content Le contenu du code barres ou de la puce RFID.
	 * @return Un string contenant le nouveau statut du colis.
	 * @see webService.StatusUpdateHandler#updateStatus(String, String)
	 */
	public String updateStatus(String format, String code, String latitude, String longitude) {
		Statement statement = null;
		String parcelID = "-1";
		String returnValue = "Item not registered."; // Valeur initiale: message d'erreur.
		String parcelContent = null;
		String parcelDescription = null;
		String currentStatus = null;
		String senderEmail = null;
		String receiverEmail = null;

		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			// On recupere l'ID du colis pour la suite.
			result = statement.executeQuery("SELECT P.parcel_id, P.content, P.description, C1.email, C2.email FROM parcel P, orders O, client C1, client C2 WHERE P.scan_code = '" + code + "' AND P.scan_code_type = '" + format + "'"
					+ "AND O.order_id = P.order_id AND C1.client_id = O.sender_id AND C2.client_id = O.receiver_id");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}

		try {
			if (result.next()) // Si le colis existe
				try {
					returnValue = "Item already delivered."; // On met a jour le message d'erreur.
					parcelID = result.getString(1);
					parcelContent = result.getString(2);
					parcelDescription = result.getString(3);
					senderEmail = result.getString(4);
					receiverEmail = result.getString(5);
					//On recupere le statut actuel du colis.
					result = statement.executeQuery("SELECT PS.status FROM parcel_status PS WHERE PS.parcel_id = " + parcelID + " AND NOT EXISTS "
							+ "(SELECT * FROM parcel_status WHERE parcel_id = PS.parcel_id AND from_date > PS.from_date)");
				} catch (SQLException e1) {
					System.out.println("Error");
					e1.printStackTrace();
				}
		} catch (SQLException e1) {
			System.out.println("Error parsing query result.");
			e1.printStackTrace();
		}

		try {
			if (result.next())
				try {
					currentStatus = result.getString(1);
					// On calcule la nouvelle valeur du statut du colis: si le colis etait chez l'expediteur ("sender")
					// ou en entrepot ("stored"), alors le nouveau statut est en transit.
					if(currentStatus.equals("sender") || currentStatus.equals("stored")){
						returnValue = "transit";
						this.updateDB(parcelID, returnValue, latitude, longitude); // Mise a jour de l'etat du colis dans la base de donnees.
						new EmailSender(parcelContent, parcelDescription, currentStatus, returnValue, senderEmail, receiverEmail);
					}
					// Si le colis etait en transit, le nouveau statut est delivre avec succes.
					else if(currentStatus.equals("transit")){
						returnValue = "delivered";
						this.updateDB(parcelID, returnValue, latitude, longitude); // Mise a jour de l'etat du colis dans la base de donnees.
						new EmailSender(parcelContent, parcelDescription, currentStatus, returnValue, senderEmail, receiverEmail);
					}

				} catch (SQLException e1) {
					System.out.println("Error parsing query result.");
					e1.printStackTrace();
				}
		} catch (SQLException e1) {
			System.out.println("Error parsing query result.");
			e1.printStackTrace();
		}
		this.close();
		return returnValue;
	}

	/**
	 * Effectue la mise a jour a proprement parler de l'etat du colis
	 * dans la base de donnees: insere un nouveau statut associe
	 * au colis specifie.
	 * @param parcelID L'ID dans la base de donnees du colis dont il faut mettre le statut a jour.
	 * @param newStatus Le nouveau statut du colis.
	 * @param longitude La latitude de scan
	 * @param latitude La longitude de scan
	 * @see #updateStatus(String, String)
	 */
	private void updateDB(String parcelID, String newStatus, String latitude, String longitude) {
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		try {
			//plafonne la periode de validite du statut precedant a maintenant.			
			statement.executeUpdate("UPDATE parcel_status "
					+ "SET to_date = CURRENT_TIMESTAMP "
					+ "WHERE parcel_id = " + parcelID
							+ " ORDER BY from_date DESC "
							+ "LIMIT 1");
			
			// Cree un nouveau statut.
			statement.executeUpdate("INSERT INTO parcel_status (" 
					+ "parcel_id ,"
					+ "status ,"
					+ "comment ,"
					+ "latitude ,"
					+ "longitude ,"
					+ "from_date ,"
					+ "to_date"
					+ ")"
					+ "VALUES ("
					+ "'" + parcelID + "', '" + newStatus + "', NULL , " + latitude + ", " + longitude +", "
					+ "CURRENT_TIMESTAMP , '9999-12-31 23:59:59'"
					+ ")");
		} catch (SQLException e) {
			System.out.println("Error upating database.");
			e.printStackTrace();
		}

	}
}
