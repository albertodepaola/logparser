package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.albertodepaola.logparser.model.BlockedIp;
import com.albertodepaola.logparser.model.LogFile;

public class BlockedIpDAOImpl extends DBRepository<BlockedIp> {

	private static final String SQL_INSERT = "insert into blocked_ip (logFile, ipv4, description, ocurrences, updateTime) values (?, ?, ?, ?, ?)";

	public BlockedIp insert(BlockedIp entity) {
		PreparedStatement ps = null;
		try {
			// TODO map the entity to prepared statemnt based on configuration file
			ps = getConnection().prepareStatement(SQL_INSERT);
			ps.setLong(1, entity.getLogFile().getId());
			ps.setString(2, entity.getIp());
			ps.setString(3, entity.getDescription());
			ps.setInt(4, entity.getOcurrences());
			ps.setDate(5, new java.sql.Date(new Date().getTime()));
			
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

	public List<BlockedIp> listAll() throws SQLException {
		
		try (Statement stmt = getConnection().createStatement();) {
			// TODO sacar el limit, por ahora queda para ir de a dos
			
			ResultSet rs = stmt.executeQuery("select * from blocked_ip");
			List<BlockedIp> logEntries = new ArrayList<>();
			while (rs.next()) {
				// TODO MAP result set to object
				/*				
				logEntries.add(new LogEntry(rs.getString("APP_UID"), rs.getString("APP_NUMBER"), new File("empty"),
						rs.getString("APP_UID")));
				*/
			}
			return logEntries;
		} catch (SQLException e) {
			throw new SQLException("Error loading blocked ips", e);
		}
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
					ps.setInt(4, entity.getOcurrences());
					ps.setDate(5, new java.sql.Date(new Date().getTime()));
					
					ps.addBatch();
					ps.clearParameters();
					i++;
					if(i % 1000 == 0 || i == logEntries.size()) {
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
