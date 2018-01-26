package com.albertodepaola.logparser.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.albertodepaola.logparser.model.LogFile;

public class LogFileDAOImpl extends DBRepository<LogFile> {

	private static final String SQL_INSERT = "insert into log_file (startDate, duration, threshold, processDate, md5) values (?, ?, ?, ?, ?)";
	

	public LogFile insert(LogFile entity) throws SQLException {

		try ( PreparedStatement ps = getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setTimestamp(1, new java.sql.Timestamp(entity.getStartDate().getTime()));
			ps.setString(2, entity.getDuration().name());
			ps.setInt(3, entity.getThreshold());
			ps.setTimestamp(4, new java.sql.Timestamp(entity.getProcessDate().getTime()));
			ps.setString(5, entity.getMd5());
						
			ps.execute();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if(generatedKeys.next()) {
				entity.setId(generatedKeys.getLong(1));
			} 
			
		} catch (SQLException e) {
			throw new SQLException("Error inserting log file.", e);
		} 
		
		return entity;
	}

	public List<LogFile> listAll() throws SQLException {
		return null;
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
