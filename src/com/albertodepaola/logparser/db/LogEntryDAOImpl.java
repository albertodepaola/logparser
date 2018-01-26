package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.albertodepaola.logparser.model.LogEntry;
import com.albertodepaola.logparser.model.LogFile;

public class LogEntryDAOImpl extends DBRepository<LogEntry> {

	private static final String SQL_INSERT = "insert into log_entry (ipv4, date, request, status, userAgent, completeLine, logFile) values (?, ?, ?, ?, ?, ?, ?)";
	

	public LogEntry insert(LogEntry entity) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		try {
			// TODO map the entity to prepared statemnt
			ps = getConnection().prepareStatement(SQL_INSERT);
			ps.setString(1, entity.getIp());
			ps.setDate(2, new java.sql.Date(entity.getDate().getTime()));
			ps.setString(3, entity.getRequest());
			ps.setInt(4, entity.getStatus());
			ps.setString(5, entity.getUserAgent());
			ps.setString(6, entity.getCompleteLine());
			ps.setLong(7, entity.getLogFile().getId());
			
			boolean isResultSet = ps.execute();
			if(isResultSet) {
				ResultSet resultSet = ps.getResultSet();
				if(resultSet.next()) {
					System.out.println(resultSet.getString(1));
					System.out.println(resultSet.getString(2));
					System.out.println(resultSet.getString(3));
				} else {
					System.out.println("Empty result set");
				}
			} else {
				int updateCount = ps.getUpdateCount();
				System.out.println("Update count: " + updateCount);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) { } // silence exception on close 
			}
		}
		return null;
	}

	public List<LogEntry> listAll() throws SQLException {
		
		try (Statement stmt = getConnection().createStatement();) {
			// TODO sacar el limit, por ahora queda para ir de a dos
			
			ResultSet rs = stmt.executeQuery("select * from log_entry");
			List<LogEntry> logEntries = new ArrayList<>();
			while (rs.next()) {
				// TODO MAP result set to object
				/*				
				logEntries.add(new LogEntry(rs.getString("APP_UID"), rs.getString("APP_NUMBER"), new File("empty"),
						rs.getString("APP_UID")));
				*/
			}
			return logEntries;
		} catch (SQLException e) {
			throw new SQLException("Error loading log entries", e);
		}
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
					ps.setDate(2, new java.sql.Date(entity.getDate().getTime()));
					ps.setString(3, entity.getRequest());
					ps.setInt(4, entity.getStatus());
					ps.setString(5, entity.getUserAgent());
					ps.setString(6, entity.getCompleteLine());
					ps.setLong(7, lf.getId());
					
					ps.addBatch();
					ps.clearParameters();
					i++;
					if(i % 10000 == 0 || i == logEntries.size()) {
						System.out.println("batch: " + i);
						ps.executeBatch();
						// TODO verify result
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
