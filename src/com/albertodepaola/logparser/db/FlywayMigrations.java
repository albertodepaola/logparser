package com.albertodepaola.logparser.db;

import java.sql.SQLException;

import org.flywaydb.core.Flyway;

public class FlywayMigrations {

	public static void main(String[] args) throws SQLException {

		Flyway flyway = new Flyway();

// 		DriverManager.getConnection("jdbc:mysql://localhost:3306/logparser?" + "user=root&password=");
		
        // Point it to the database
        flyway.setDataSource("jdbc:mysql://localhost:3306/logparser", "root", "");

        flyway.setLocations("classpath:com.albertodepaola.logparser.db.migrations");
        
        // Start the migration
        flyway.migrate();
	}

}
