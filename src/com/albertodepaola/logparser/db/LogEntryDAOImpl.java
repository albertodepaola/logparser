package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.LogEntry;
import com.albertodepaola.logparser.model.LogFile;

public class LogEntryDAOImpl extends DBRepository<LogEntry> {

	private static final String SQL_INSERT = "insert into log_entry (ipv4, date, request, status, userAgent, completeLine, logFile) values (?, ?, ?, ?, ?, ?, ?)";
	

	public LogEntry insert(LogEntry entity) throws SQLException {

		try (PreparedStatement ps = getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){
			// TODO map the entity to prepared statemnt
			ps.setString(1, entity.getIp());
			ps.setDate(2, new java.sql.Date(entity.getDate().getTime()));
			ps.setString(3, entity.getRequest());
			ps.setInt(4, entity.getStatus());
			ps.setString(5, entity.getUserAgent());
			ps.setString(6, entity.getCompleteLine());
			ps.setLong(7, entity.getLogFile().getId());
			
			ps.execute();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if(generatedKeys.next()) {
				entity.setId(generatedKeys.getLong(1));
			} 
			
		} catch (SQLException e) {
			throw new SQLException("Error inserting log entry.", e);
			
		} 
		return entity;
	}

	public List<LogEntry> listAll() throws SQLException {
		return null;
	}
	
	public void insertBatch(Collection<LogEntry> logEntries, LogFile lf) throws SQLException {
		
		try (
		        Connection connection = getConnection();
		        PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
		    ) {
		        int i = 0;

		        for (LogEntry entity : logEntries) {
		        	entity.setLogFile(lf);
		        	ps.setString(1, entity.getIp());
					ps.setTimestamp(2, new java.sql.Timestamp(entity.getDate().getTime()));
					ps.setString(3, entity.getRequest());
					ps.setInt(4, entity.getStatus());
					ps.setString(5, entity.getUserAgent());
					ps.setString(6, entity.getCompleteLine());
					ps.setLong(7, lf.getId());
					
					ps.addBatch();
					ps.clearParameters();
					i++;
					if(i % Configuration.getConfiguration().getBatchSize() == 0 || i == logEntries.size()) {
						ps.executeBatch();
					}
		        }
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public LogEntry update(LogEntry entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean delete(LogEntry entity) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
