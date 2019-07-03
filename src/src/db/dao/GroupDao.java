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
 * @version 20190703.1440
 * 
 */
public class GroupDao {

	/**
	 * Create a group with a user list
	 * 
	 * @param groupname
	 * @param memberUsernameList
	 * @return
	 */
	public boolean addGroup(String groupname, ArrayList<String> memberUsernameList) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			for (int i = 0; i < memberUsernameList.size(); i++) {
				String username = memberUsernameList.get(i);

				String checkGroupSql = "select count(*) from group_user where groupname=? and username=?";
				PreparedStatement checkGroupPs = conn.prepareStatement(checkGroupSql);
				checkGroupPs.setString(1, groupname);
				checkGroupPs.setString(2, username);
				ResultSet checkGroupRs = checkGroupPs.executeQuery();
				checkGroupRs.next();
				int userGroupCount = checkGroupRs.getInt(1);

				String checkUserSql = "select count(*) from user where username=?";
				PreparedStatement checkUserPs = conn.prepareStatement(checkUserSql);
				checkUserPs.setString(1, username);
				ResultSet checkUserRs = checkUserPs.executeQuery();
				checkUserRs.next();
				int userUserCount = checkUserRs.getInt(1);

				if (userUserCount == 1 && userGroupCount == 0) {
					String addGroupUserBindSql = "insert into group_user values(?,?)";
					PreparedStatement addGroupUserBindPs = conn.prepareStatement(addGroupUserBindSql);
					addGroupUserBindPs.setString(1, groupname);
					addGroupUserBindPs.setString(2, username);
					addGroupUserBindPs.executeQuery();
				}
			}
			status = true;

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

			String checkSql = "select count(*) from group_user where groupname=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, groupname);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int groupCount = checkRs.getInt(1);
			if (groupCount != 0) {
				String delSql = "delete from group_user where groupname=?";
				PreparedStatement delPs = conn.prepareStatement(delSql);
				delPs.setString(1, groupname);
				delPs.executeQuery();
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

			String groupSql = "select distinct groupname from group_user";
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

}
