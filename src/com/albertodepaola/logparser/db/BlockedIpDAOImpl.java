package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.albertodepaola.logparser.model.BlockedIp;
import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.LogFile;

public class BlockedIpDAOImpl extends DBRepository<BlockedIp> {

	private static final String SQL_INSERT = "insert into blocked_ip (logFile, ipv4, description, occurrences, updateTime) values (?, ?, ?, ?, ?)";

	public BlockedIp insert(BlockedIp entity) throws SQLException {
		
		try (PreparedStatement ps = getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setLong(1, entity.getLogFile().getId());
			ps.setString(2, entity.getIp());
			ps.setString(3, entity.getDescription());
			ps.setInt(4, entity.getOccurrences());
			ps.setDate(5, new java.sql.Date(new Date().getTime()));
			
			ps.execute();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if(generatedKeys.next()) {
				entity.setId(generatedKeys.getLong(1));
			} 
						
		} catch (SQLException e) {
			throw new SQLException("Error insertig blocked ip.", e);
		} 
		return entity;
	}

	public List<BlockedIp> listAll() throws SQLException {
		return null;
	}
	
	public void insertBatch(Collection<BlockedIp> logEntries, LogFile lf) throws SQLException {
		
		try (
		        Connection connection = getConnection();
		        PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
		    ) {
		        int i = 0;

		        for (BlockedIp entity : logEntries) {
		        	entity.setLogFile(lf);
		        	ps.setLong(1, lf.getId());
					ps.setString(2, entity.getIp());
					ps.setString(3, entity.getDescription());
					ps.setInt(4, entity.getOccurrences());
					ps.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
					
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
	
	@Override
	public BlockedIp update(BlockedIp entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(BlockedIp entity) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
