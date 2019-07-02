package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.*;

/**
 * Database access object UserDao for accessing users
 * 
 * @author DesmondCobb
 *
 */

public class UserDao {

	/**
	 * Get a user by username
	 * 
	 * @param username
	 * @return
	 */
	public User getUser(String username) {
		User target = null;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			// Check if user exists
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int count = checkRs.getInt(1);
			if (0 == count) {
				// User not found
			} else {
				String getSql = "select * from user where username=?";
				PreparedStatement getPs = conn.prepareStatement(getSql);
				getPs.setString(1, username);
				ResultSet getRs = getPs.executeQuery();
				getRs.next();
				String password = getRs.getString(2);
				String publicKey = getRs.getString(3);
				// Generate User object
				target = new User();
				target.setUsername(username);
				target.setPassword(password);
				target.setPublickey(publicKey);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return target;
	}

	/**
	 * Add a new user
	 * 
	 * @param ci
	 * @return status
	 */
	public int addUser(User ci) {
		// Status code
		// 0 = OK
		// 1 = Internal error, maybe not successful
		// -1 = User already exists
		int status = 0;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			// Check if username is in use
			String checkSql = "select count(*) from user where userName = ?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, ci.getUsername());
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int count = checkRs.getInt(1);
			if (count > 0) {
				status = -1;
			} else {
				// Insert new user into database
				String addSql = "insert into user values(?,?,?)";
				PreparedStatement addPs = conn.prepareStatement(addSql);
				addPs.setString(1, ci.getUsername());
				addPs.setString(2, ci.getPassword());
				if (null == ci.getPublickey() || "".equals(ci.getPublickey())) {
					addPs.setString(3, "");
				} else {
					addPs.setString(3, ci.getPublickey());
				}
				ResultSet addRs = addPs.executeQuery();
				// Check is insertion is successful
				String sureSql = "select count(*) from user where username=?";
				PreparedStatement surePs = conn.prepareStatement(sureSql);
				surePs.setString(1, ci.getUsername());
				ResultSet sureRs = surePs.executeQuery();
				sureRs.next();
				int sureCount = sureRs.getInt(1);
				if (1 == sureCount) {
					status = 0;
				} else {
					status = 1;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			status = 1;
		}
		return status;
	}

	/**
	 * Update a public key for a user
	 * 
	 * @param cache
	 * @return
	 */
	public int updatePublicKey(User cache) {
		// Status code
		// 0 = OK
		// 1 = Internal error, maybe not successful
		// -1 = User not found
		int status = 0;
		String username = cache.getUsername();
		String newPK = cache.getPublickey();
		if (null == newPK) {
			newPK = "";
		}
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			// Check if user exists
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int count = checkRs.getInt(1);
			if (0 == count) {
				status = -1;
			} else {
				// Update public key
				String updateSql = "update user set publickey=? where username=?";
				PreparedStatement updatePs = conn.prepareStatement(updateSql);
				updatePs.setString(1, newPK);
				updatePs.setString(2, username);
				ResultSet updateRs = updatePs.executeQuery();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			status = 1;
		}
		return status;
	}

}
