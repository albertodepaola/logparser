package com.albertodepaola.logparser.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DBRepository<T> {
	public static Connection getConnection() throws SQLException {
		// TODO pasar a confirutacion, usar tables.json existente
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/logparser?user=root&password=rootroot&rewriteBatchedStatements=true");
	}

	public abstract T insert(T entity);
	public abstract List<T> listAll() throws SQLException;
	public abstract T update(T entity);
	public abstract Boolean delete(T entity);
	
	
	private static ResultSet executeQuery(String query) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO armar algo mas lindo o estandar de este proyecto.
			throw e;
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			/*
			 * if (rs != null) { try { rs.close(); } catch (SQLException sqlEx)
			 * { } // ignore
			 * 
			 * rs = null; }
			 */
			/*
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmt = null;
			}
			*/
		}
	}
/*
	public static List<Informe> cargarInformes() throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet fileRs = null;
		try {
			stmt = getConnection().createStatement();
			// TODO sacar el limit, por ahora queda para ir de a dos
			rs = stmt.executeQuery("select * from ineba.grilla_casos_operador_informe limit 2");
			List<Informe> informes = new ArrayList<>();
			while (rs.next()) {
				String appUid = rs.getString("APP_UID");
				fileRs = stmt.executeQuery("select APP_DOC_UID, version, tipo, nombreArchivo from caso_documento_informe where APP_UID = '" + appUid + "';");
				
				informes.add(new Informe(rs.getString("APP_UID"), rs.getString("APP_NUMBER"), new File("empty"),
						rs.getString("APP_UID")));
			}
			return informes;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Error al cargar informes", e);
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
*/
}
