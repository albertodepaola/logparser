package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.albertodepaola.logparser.model.Configuration;

public abstract class DBRepository<T> {
	public static Connection getConnection() throws SQLException {

		// return
		// DriverManager.getConnection("jdbc:mysql://localhost:3306/logparser?user=root&password=rootroot&rewriteBatchedStatements=true");
		Map<String, String> dbConfig = Configuration.getConfiguration().getDatabaseConfiguration();

		String connectionString = dbConfig.get("url") + ":" + dbConfig.get("port") + "/" + dbConfig.get("database")
				+ "?user=" + dbConfig.get("user") + "&password=" + dbConfig.get("password") + "&"
				+ dbConfig.get("aditionalParameters");
		return DriverManager.getConnection(connectionString);
	}

	public abstract T insert(T entity);

	public abstract List<T> listAll() throws SQLException;

	public abstract T update(T entity);

	public abstract Boolean delete(T entity);

}
