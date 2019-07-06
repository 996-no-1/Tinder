package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.entity.Group;
import db.util.DBFactory;

/**
 * Database access object for entity Group
 * 
 * @author DesmondCobb
 * @version 20190706.1856
 * 
 */
public class GroupDao {

	/**
	 * Create a new group
	 * 
	 * @param groupname
	 * @return
	 */
	public boolean createGroup(String groupname) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String checkSql = "select count(*) from groups where groupname=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, groupname);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int groupCount = checkRs.getInt(1);

			if (groupCount == 0) {
				String createSql = "insert into groups values(?)";
				PreparedStatement createPs = conn.prepareStatement(createSql);
				createPs.setString(1, groupname);
				createPs.executeQuery();

				status = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Add a bunch of users to a group
	 * 
	 * @param groupname
	 * @param userList
	 * @return
	 */
	public boolean addUserToGroup(String groupname, List<String> userList) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String groupCheckSql = "select count(*) from groups where groupname=?";
			PreparedStatement groupCheckPs = conn.prepareStatement(groupCheckSql);
			groupCheckPs.setString(1, groupname);
			ResultSet groupCheckRs = groupCheckPs.executeQuery();
			groupCheckRs.next();
			int groupCount = groupCheckRs.getInt(1);
			if (groupCount == 0) {
				return status;
			}

			for (int i = 0; i < userList.size(); i++) {
				String username = userList.get(i);

				String userCheckSql = "select count(*) from user where username=?";
				PreparedStatement userCheckPs = conn.prepareStatement(userCheckSql);
				userCheckPs.setString(1, username);
				ResultSet userCheckRs = userCheckPs.executeQuery();
				userCheckRs.next();
				int userCount = userCheckRs.getInt(1);

				String bindCheckSql = "select count(*) from group_user where groupname=? and username=?";
				PreparedStatement bindCheckPs = conn.prepareStatement(bindCheckSql);
				bindCheckPs.setString(1, groupname);
				bindCheckPs.setString(2, username);
				ResultSet bindCheckRs = bindCheckPs.executeQuery();
				bindCheckRs.next();
				int bindCount = bindCheckRs.getInt(1);

				if (bindCount == 0 && userCount == 1) {
					String insSql = "insert into group_user values(?,?)";
					PreparedStatement insPs = conn.prepareStatement(insSql);
					insPs.setString(1, groupname);
					insPs.setString(2, username);
					insPs.executeQuery();
				}

			}
			status = true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Add a single user to a group
	 * 
	 * @param groupname
	 * @param username
	 * @return
	 */
	public boolean addUserToGroup(String groupname, String username) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String userCheckSql = "select count(*) from user where username=?";
			PreparedStatement userCheckPs = conn.prepareStatement(userCheckSql);
			userCheckPs.setString(1, username);
			ResultSet userCheckRs = userCheckPs.executeQuery();
			userCheckRs.next();
			int userCount = userCheckRs.getInt(1);
			if (userCount == 0) {
				// User not found
				return status;
			}

			String groupCheckSql = "select count(*) from groups where groupname=?";
			PreparedStatement groupCheckPs = conn.prepareStatement(groupCheckSql);
			groupCheckPs.setString(1, groupname);
			ResultSet groupCheckRs = groupCheckPs.executeQuery();
			groupCheckRs.next();
			int groupCount = groupCheckRs.getInt(1);
			if (groupCount == 0) {
				// Group not exist
				return status;
			}

			String bindCheckSql = "select count(*) from group_user where groupname=? and username=?";
			PreparedStatement bindCheckPs = conn.prepareStatement(bindCheckSql);
			bindCheckPs.setString(1, groupname);
			bindCheckPs.setString(2, username);
			ResultSet bindCheckRs = bindCheckPs.executeQuery();
			bindCheckRs.next();
			int bindCount = bindCheckRs.getInt(1);

			if (bindCount == 0) {
				// No redundant bind
				String insSql = "insert into group_user values(?,?)";
				PreparedStatement insPs = conn.prepareStatement(insSql);
				insPs.setString(1, groupname);
				insPs.setString(2, username);
				insPs.executeQuery();

				status = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Delete a existing group by groupname
	 * 
	 * @param groupname
	 * @return
	 */
	public boolean deleteGroup(String groupname) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String checkSql = "select count(*) from groups where groupname=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, groupname);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int groupCount = checkRs.getInt(1);
			if (groupCount != 0) {
				String delGroupSql = "delete from groups where groupname=?";
				PreparedStatement delGroupPs = conn.prepareStatement(delGroupSql);
				delGroupPs.setString(1, groupname);
				delGroupPs.executeQuery();

				String delGroupUserSql = "delete from group_user where groupname=?";
				PreparedStatement delGroupUserPs = conn.prepareStatement(delGroupUserSql);
				delGroupUserPs.setString(1, groupname);
				delGroupUserPs.executeQuery();

				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Delete a given user from a group
	 * 
	 * @param groupname
	 * @param username
	 * @return
	 */
	public boolean deleteUserFromGroup(String groupname, String username) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String checkGroupSql = "select count(*) from group_user where groupname=? and username=?";
			PreparedStatement checkGroupPs = conn.prepareStatement(checkGroupSql);
			checkGroupPs.setString(1, groupname);
			checkGroupPs.setString(2, username);
			ResultSet checkGroupRs = checkGroupPs.executeQuery();
			checkGroupRs.next();
			int userGroupCount = checkGroupRs.getInt(1);

			if (userGroupCount == 1) {
				String delSql = "delete from group_user where groupname=? and username=?";
				PreparedStatement delPs = conn.prepareStatement(delSql);
				delPs.setString(1, groupname);
				delPs.setString(2, username);
				delPs.executeQuery();
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Clear all bind between group and a given user
	 * 
	 * @param username
	 * @return
	 */
	public boolean clearAllBindOfUser(String username) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String checkGroupSql = "select count(*) from group_user where username=?";
			PreparedStatement checkGroupPs = conn.prepareStatement(checkGroupSql);
			checkGroupPs.setString(1, username);
			ResultSet checkGroupRs = checkGroupPs.executeQuery();
			checkGroupRs.next();
			int userGroupCount = checkGroupRs.getInt(1);

			if (userGroupCount != 0) {
				String delSql = "delete from group_user where username=?";
				PreparedStatement delPs = conn.prepareStatement(delSql);
				delPs.setString(1, username);
				delPs.executeQuery();
				status = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Get all groups
	 * 
	 * @return
	 */
	public List<Group> getAllGroup() {
		List<Group> list = new ArrayList<Group>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String groupSql = "select distinct groupname from groups";
			PreparedStatement groupPs = conn.prepareStatement(groupSql);
			ResultSet groupRs = groupPs.executeQuery();
			while (groupRs.next()) {
				String groupname = groupRs.getString(1);
				String userSql = "select username from group_user where groupname=?";
				PreparedStatement userPs = conn.prepareStatement(userSql);
				userPs.setString(1, groupname);
				ResultSet userRs = userPs.executeQuery();
				ArrayList<String> userList = new ArrayList<String>();
				while (userRs.next()) {
					userList.add(userRs.getString(1));
				}
				Group aGroup = new Group(groupname, userList);
				list.add(aGroup);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Get all users in a given group
	 * 
	 * @param groupname
	 * @return
	 */
	public List<String> getAllUserIn(String groupname) {
		List<String> list = new ArrayList<String>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String groupCheckSql = "select count(*) from groups where groupname=?";
			PreparedStatement groupCheckPs = conn.prepareStatement(groupCheckSql);
			groupCheckPs.setString(1, groupname);
			ResultSet groupCheckRs = groupCheckPs.executeQuery();
			groupCheckRs.next();
			int groupCount = groupCheckRs.getInt(1);
			if (groupCount == 0) {
				return null;
			}

			String getSql = "select username from group_user where groupname=?";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			getPs.setString(1, groupname);
			ResultSet getRs = getPs.executeQuery();
			while (getRs.next()) {
				String username = getRs.getString(1);
				list.add(username);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Get all users who are not in a given group
	 * 
	 * @param groupname
	 * @return
	 */
	public List<String> getAllUserNotIn(String groupname) {
		List<String> list = new ArrayList<String>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			List<String> in = getAllUserIn(groupname);
			if (in == null) {
				return null;
			}

			String getSql = "select username from user";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			ResultSet getRs = getPs.executeQuery();

			while (getRs.next()) {
				String username = getRs.getString(1);
				if (in.indexOf(username) == -1) {
					list.add(username);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
