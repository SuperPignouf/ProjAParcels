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
		
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error making statement.");
			e.printStackTrace();
		}
		ResultSet result = null;
		try {
			result = statement.executeQuery("SELECT content, description FROM parcel WHERE scan_code = " + content + " AND scan_code_type = '" + format + "'");
		} catch (SQLException e) {
			System.out.println("Error executing query.");
			e.printStackTrace();
		}
		
		try {
			if (result.next())
				try {
					returnValue = result.getString(1) + ": " + result.getString(2);
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

}
