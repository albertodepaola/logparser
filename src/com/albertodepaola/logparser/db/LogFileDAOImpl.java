package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.albertodepaola.logparser.model.LogFile;

public class LogFileDAOImpl extends DBRepository<LogFile> {

	private static final String SQL_INSERT = "insert into log_file (startDate, duration, threshold, processDate, md5) values (?, ?, ?, ?, ?)";
	

	public LogFile insert(LogFile entity) {
		// TODO Auto-generated method stub
		try ( PreparedStatement ps = getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
			// TODO map the entity to prepared statemnt
			
			ps.setTimestamp(1, new java.sql.Timestamp(entity.getStartDate().getTime()));
			ps.setString(2, entity.getDuration().name());
			ps.setInt(3, entity.getThreshold());
			ps.setTimestamp(4, new java.sql.Timestamp(entity.getProcessDate().getTime()));
			ps.setString(5, entity.getMd5());
						
			ps.execute();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if(generatedKeys.next()) {
				
				entity.setId(generatedKeys.getLong(1));
			} else {
				System.out.println("No generated keys");
			}
			
			
		} catch (Exception e) {
			System.err.println("Error inserting logfile");
			e.printStackTrace();
			
		} 
		
		return entity;
	}

	public List<LogFile> listAll() throws SQLException {
		
		try (Statement stmt = getConnection().createStatement();) {
			// TODO sacar el limit, por ahora queda para ir de a dos
			
			ResultSet rs = stmt.executeQuery("select * from log_entry");
			List<LogFile> logEntries = new ArrayList<>();
			while (rs.next()) {
				// TODO MAP result set to object
				/*				
				logEntries.add(new LogFile(rs.getString("APP_UID"), rs.getString("APP_NUMBER"), new File("empty"),
						rs.getString("APP_UID")));
				*/
			}
			return logEntries;
		} catch (SQLException e) {
			throw new SQLException("Error loading log entries", e);
		}
	}

	public LogFile update(LogFile entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean delete(LogFile entity) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
