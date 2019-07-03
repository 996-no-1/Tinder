package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.entity.User;
import db.util.DBFactory;

/**
 * Database access object for entity User
 * 
 * @author DesmondCobb
 * @version 20190703.1454
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
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int userCount = checkRs.getInt(1);
			if (userCount == 1) {
				String getSql = "select * from user where username=?";
				PreparedStatement getPs = conn.prepareStatement(getSql);
				getPs.setString(1, username);
				ResultSet getRs = getPs.executeQuery();
				getRs.next();
				String gender = getRs.getString(2);
				int age = getRs.getInt(3);
				String note = getRs.getString(4);
				boolean isBanned = true;
				if (getRs.getInt(5) == 0) {
					isBanned = false;
				}
				String hashedPassword = getRs.getString(6);
				target = new User(username, gender, age, note, isBanned, hashedPassword);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return target;
	}

	/**
	 * 
	 * @param newUser
	 * @return isSuccessful
	 */
	public boolean addUser(User newUser, String hashedPassword) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			String username = newUser.getUsername();
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int userCount = checkRs.getInt(1);
			if (userCount == 0) {
				String insSql = "insert into user values(?,?,?,?,?,?)";
				PreparedStatement insPs = conn.prepareStatement(insSql);
				insPs.setString(1, username);
				insPs.setString(2, newUser.getGender());
				insPs.setInt(3, newUser.getAge());
				insPs.setString(4, newUser.getNote());
				if (newUser.isBan()) {
					insPs.setInt(5, 1);
				} else {
					insPs.setInt(5, 0);
				}
				insPs.setString(6, hashedPassword);
				insPs.executeQuery();
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Lock or unlock a user
	 * 
	 * @param username
	 * @param operation
	 *            "lock" to lock; "unlock" to unlock
	 * @return
	 */
	public boolean setBanUser(String username, String operation) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int userCount = checkRs.getInt(1);
			if (userCount == 1) {
				String upd8Sql = "update user set isban=? where username=?";
				PreparedStatement upd8Ps = conn.prepareStatement(upd8Sql);
				if ("lock".equals(operation)) {
					upd8Ps.setInt(1, 1);
					upd8Ps.setString(2, username);
				} else {
					upd8Ps.setInt(1, 0);
					upd8Ps.setString(2, username);
				}
				upd8Ps.executeQuery();
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Delete user by username
	 * 
	 * @param username
	 * @return
	 */
	public boolean deleteUser(String username) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int userCount = checkRs.getInt(1);
			if (userCount == 1) {
				String delSql = "delete from user where username=?";
				PreparedStatement delPs = conn.prepareStatement(delSql);
				delPs.setString(1, username);
				delPs.executeQuery();
				GroupDao groupDao = new GroupDao();
				groupDao.clearAllBindOfUser(username);
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Get all users
	 * 
	 * @return
	 */
	public List<User> getAllUser() {
		List<User> list = new ArrayList<User>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			String getSql = "select * from user";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			ResultSet getRs = getPs.executeQuery();
			while (getRs.next()) {
				String username = getRs.getString(1);
				String gender = getRs.getString(2);
				int age = getRs.getInt(3);
				String note = getRs.getString(4);
				boolean isBanned = true;
				if (getRs.getInt(5) == 0) {
					isBanned = false;
				}
				String hashedPassword = getRs.getString(6);
				User aUser = new User(username, gender, age, note, isBanned, hashedPassword);
				list.add(aUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Update user information
	 * 
	 * @param updatedUser
	 * @return
	 */
	public boolean updateUser(User updatedUser) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			String username = updatedUser.getUsername();
			String checkSql = "select count(*) from user where username=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, username);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int userCount = checkRs.getInt(1);
			if (userCount == 1) {
				String up8Sql = "update user set gender=?,age=?,note=?,password=? where username=?";
				PreparedStatement up8Ps = conn.prepareStatement(up8Sql);
				up8Ps.setString(1, updatedUser.getGender());
				up8Ps.setInt(2, updatedUser.getAge());
				up8Ps.setString(3, updatedUser.getNote());
				up8Ps.setString(4, updatedUser.getHashedPassword());
				up8Ps.setString(5, username);
				up8Ps.executeQuery();
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

}
