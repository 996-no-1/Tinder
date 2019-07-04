package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.entity.GroupMessage;
import db.entity.UserMessage;
import db.util.DBFactory;
import db.util.TimeFactory;

/**
 * Database access object for entity Message(including group message and
 * user-to-user message)
 * 
 * @author DesmondCobb
 * @version 20190704.0943
 *
 */
public class MessageDao {

	/**
	 * Save a user-to-user message to database
	 * 
	 * @param fromUsername
	 * @param toUsername
	 * @param message
	 * @return
	 */
	public boolean saveUserMessage(String fromUsername, String toUsername, String message) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			long currentTime = TimeFactory.getTime();

			String insSql = "insert into user_message values(?,?,?,?)";
			PreparedStatement insPs = conn.prepareStatement(insSql);
			insPs.setString(1, fromUsername);
			insPs.setString(2, toUsername);
			insPs.setString(3, message);
			insPs.setLong(4, currentTime);
			insPs.executeQuery();

			status = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Get all messages between a user and another user
	 * 
	 * @param fromUsername
	 * @param toUsername
	 * @return
	 */
	public List<UserMessage> getAllUserMessage(String userA, String userB) {
		List<UserMessage> messageList = new ArrayList<UserMessage>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String getSql = "select * from user_message where (fromusername=? and tousername=?) or (fromusername=? and tousername=?)";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			getPs.setString(1, userA);
			getPs.setString(2, userB);
			getPs.setString(3, userB);
			getPs.setString(4, userA);
			ResultSet getRs = getPs.executeQuery();

			while (getRs.next()) {
				String from = getRs.getString(1);
				String to = getRs.getString(2);
				String message = getRs.getString(3);
				long time = getRs.getLong(4);
				UserMessage aUserMessage = new UserMessage(from, to, message, time);
				messageList.add(aUserMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messageList;
	}

	/**
	 * Get messages from a user to another user in 30 minutes
	 * 
	 * @param fromUsername
	 * @param toUsername
	 * @return
	 */
	public List<UserMessage> getLatestUserMessageIn30Min(String userA, String userB) {
		List<UserMessage> messageList = new ArrayList<UserMessage>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String timeSql = "select max(time) from user_message where (fromusername=? and tousername=?) or (fromusername=? and tousername=?)";
			PreparedStatement timePs = conn.prepareStatement(timeSql);
			timePs.setString(1, userA);
			timePs.setString(2, userB);
			timePs.setString(3, userB);
			timePs.setString(4, userA);
			ResultSet timeRs = timePs.executeQuery();
			if (!timeRs.next()) {
				return messageList;
			}
			long latestMessageTime = timeRs.getLong(1);
			long mark = TimeFactory.getTimeSomeWhatBefore(latestMessageTime);

			String getSql = "select * from user_message where ((fromusername=? and tousername=?) or (fromusername=? and tousername=?)) and (time>?)";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			getPs.setString(1, userA);
			getPs.setString(2, userB);
			getPs.setString(3, userB);
			getPs.setString(4, userA);
			getPs.setLong(5, mark);
			ResultSet getRs = getPs.executeQuery();

			while (getRs.next()) {
				String from = getRs.getString(1);
				String to = getRs.getString(2);
				String message = getRs.getString(3);
				long time = getRs.getLong(4);
				UserMessage aUserMessage = new UserMessage(from, to, message, time);
				messageList.add(aUserMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messageList;
	}

	/**
	 * Save a user-to-user message to database
	 * 
	 * @param groupname
	 * @param sender
	 * @param message
	 * @return
	 */
	public boolean saveGroupMessage(String groupname, String sender, String message) {
		boolean status = false;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			long currentTime = TimeFactory.getTime();

			String insSql = "insert into group_message values(?,?,?,?)";
			PreparedStatement insPs = conn.prepareStatement(insSql);
			insPs.setString(1, groupname);
			insPs.setString(2, sender);
			insPs.setString(3, message);
			insPs.setLong(4, currentTime);
			insPs.executeQuery();

			status = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Get all group messages of a group
	 * 
	 * @param groupname
	 * @return
	 */
	public List<GroupMessage> getAllGroupMessage(String groupname) {
		List<GroupMessage> messageList = new ArrayList<GroupMessage>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String getSql = "select * from group_message where groupname=?";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			getPs.setString(1, groupname);
			ResultSet getRs = getPs.executeQuery();

			while (getRs.next()) {
				String sender = getRs.getString(2);
				String message = getRs.getString(3);
				long time = getRs.getLong(4);
				GroupMessage aGroupMessage = new GroupMessage(groupname, sender, message, time);
				messageList.add(aGroupMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messageList;
	}

	/**
	 * Get latest group message in 30 minutes
	 * 
	 * @param groupname
	 * @return
	 */
	public List<GroupMessage> getLatestGroupMessageIn30Min(String groupname) {
		List<GroupMessage> messageList = new ArrayList<GroupMessage>();
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String timeSql = "select max(time) from group_message where groupname=?";
			PreparedStatement timePs = conn.prepareStatement(timeSql);
			timePs.setString(1, groupname);
			ResultSet timeRs = timePs.executeQuery();
			if (!timeRs.next()) {
				return messageList;
			}
			long latestMessageTime = timeRs.getLong(1);
			long mark = TimeFactory.getTimeSomeWhatBefore(latestMessageTime);

			String getSql = "select * from group_message where groupname=? and time>?";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			getPs.setString(1, groupname);
			getPs.setLong(2, mark);
			ResultSet getRs = getPs.executeQuery();

			while (getRs.next()) {
				String sender = getRs.getString(2);
				String message = getRs.getString(3);
				long time = getRs.getLong(4);
				GroupMessage aGroupMessage = new GroupMessage(groupname, sender, message, time);
				messageList.add(aGroupMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messageList;
	}

}
