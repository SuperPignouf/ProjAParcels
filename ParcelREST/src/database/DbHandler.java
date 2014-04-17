package database;

import java.sql.*;

public class DbHandler {

	private Connection connection;

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
			result = statement.executeQuery("SELECT * FROM user WHERE matricule = " + matricule + " AND password = '" + password + "'");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}
		try {
			while (result.next()) isValid = true;
		} catch (SQLException e) {
			System.out.println("Error parsing query result.");
			e.printStackTrace();
		}
		this.close();
		if (isValid) return "1"; 
		else return "0";
	}

	private void close(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("Error closing connection to DB.");
			e.printStackTrace();
		}
	}

	public String scanningProduct(String format, String content) {
		String returnValue = "Item not found.";
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
			result = statement.executeQuery("SELECT content, description, parcel_id FROM parcel WHERE scan_code = '" + content + "' AND scan_code_type = '" + format + "'");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}

		try {
			if (result.next())
				try {
					returnValue = result.getString(1) + ": " + result.getString(2);
					parcelID = result.getString(3);
				} catch (SQLException e1) {
					System.out.println("Error parsing query result.");
					e1.printStackTrace();
				}
		} catch (SQLException e1) {
			System.out.println("Error parsing query result.");
			e1.printStackTrace();
		}

		try {
			result = statement.executeQuery("SELECT PS.status FROM parcel_status PS WHERE PS.parcel_id = " + parcelID + " AND NOT EXISTS "
					+ "(SELECT * FROM parcel_status WHERE parcel_id = PS.parcel_id AND from_date > PS.from_date)");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}

		try {
			if (result.next())
				try {
					returnValue += " \n Current status: " + result.getString(1);
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

	public String updateStatus(String format, String content) {
		Statement statement = null;
		String parcelID = "-1";
		String returnValue = "Item already delivered";

		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			result = statement.executeQuery("SELECT parcel_id FROM parcel WHERE scan_code = '" + content + "' AND scan_code_type = '" + format + "'");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}

		try {
			if (result.next())
				try {
					parcelID = result.getString(1);
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
					String currentStatus = result.getString(1);

					if(currentStatus.equals("sender") || currentStatus.equals("stored")){
						returnValue = "transit";
						this.updateDB(parcelID, returnValue);
					}
					else if(currentStatus.equals("transit")){
						returnValue = "delivered";
						this.updateDB(parcelID, returnValue);
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

	private void updateDB(String parcelID, String returnValue) {
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		try {
			statement.executeUpdate("INSERT INTO `parcels`.`parcel_status` (" 
					+ "`parcel_id` ,"
					+ "`status` ,"
					+ "`comment` ,"
					+ "`from_date` ,"
					+ "`to_date`"
					+ ")"
					+ "VALUES ("
					+ "'" + parcelID + "', '" + returnValue + "', NULL ,"
					+ "CURRENT_TIMESTAMP , '9999-12-31 23:59:59'"
					+ ")");
		} catch (SQLException e) {
			System.out.println("Error upating database.");
			e.printStackTrace();
		}

	}

}
