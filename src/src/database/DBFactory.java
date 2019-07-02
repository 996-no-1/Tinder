package database;

import java.sql.*;

/**
 * Database connection factory
 * 
 * @author DesmondCobb
 *
 */

public class DBFactory {

	private String URL = "jdbc:mariadb://localhost/nis";
	private String USER = "nis";
	private String PASSWD = "123456";
	private static DBFactory INSTANCE;

	static {
		try {

			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get singleton instance
	 * 
	 * @return
	 */
	public static DBFactory getINSTANCE() {
		synchronized (DBFactory.class) {
			if (INSTANCE == null) {
				INSTANCE = new DBFactory();
			}
		}
		return INSTANCE;
	}

	/**
	 * Get connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWD);
	}

	/**
	 * Close connection
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 * @throws SQLException
	 */
	public static void closeConnection(Connection conn, Statement st, ResultSet rs) throws SQLException {
		if (conn != null) {
			conn.close();
		}
		if (st != null) {
			st.close();
		}
		if (rs != null) {
			rs.close();
		}
	}
}
