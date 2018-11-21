package com.bhz.lht.mystudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestOracle {

	public static void main(String[] args) throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//oracle.heat:1521/oracle", "rldc", "rldc");
		if(conn != null) {
			System.out.println("Successful connected to database.");
			ResultSet rs = conn.createStatement().executeQuery("select * from dual");
			rs.next();
			String result = rs.getString(1);
			System.out.println("X = " + result);
			rs.close();
			conn.close();
		} else {
			System.err.println("Failed connected to database.");
		}
	}

}
