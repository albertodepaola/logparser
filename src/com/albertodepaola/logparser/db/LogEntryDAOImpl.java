package com.albertodepaola.logparser.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.albertodepaola.logparser.model.LogEntry;

public class LogEntryDAOImpl extends DBRepository<LogEntry> {

	public LogEntry insert(LogEntry entity) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		try {
			// TODO map the entity to prepared statemnt
			ps = getConnection().prepareStatement("insert into log_entry (ipv4, date, request, status, userAgent, completeLine) values (?, ?, ?, ?, ?, ?)");
			ps.setString(1, entity.getIp());
			ps.setDate(2, (Date) entity.getDate());
			ps.setString(3, entity.getRequest());
			ps.setInt(4, entity.getStatus());
			ps.setString(5, entity.getUserAgent());
			ps.setString(6, entity.getCompleteLine());
			
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
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet fileRs = null;
		try {
			stmt = getConnection().createStatement();
			// TODO sacar el limit, por ahora queda para ir de a dos
			
			rs = stmt.executeQuery("select * from log_entry");
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
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}
			if (fileRs != null) {
				try {
					fileRs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				fileRs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmt = null;
			}
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
