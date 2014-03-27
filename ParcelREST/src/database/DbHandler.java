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
		if (isValid) return "valid"; 
		else return "invalid";
	}
	
	private void close(){
		try {
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("Error closing connection to DB.");
			e.printStackTrace();
		}
	}

}
