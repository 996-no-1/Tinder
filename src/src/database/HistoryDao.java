package database;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.FileSaved;

/**
 * Database access object FileDao for accessing chat history
 * 
 * @author DesmondCobb
 *
 */

public class HistoryDao {

	/**
	 * Save uploaded chat history to database
	 * 
	 * @param history
	 * @return
	 */
	public int uploadHistory(FileSaved history) {
		int status = 0;
		String from = history.getSender();
		String to = history.getReceiver();
		String chatHistoryString = history.getFile();
		byte[] chatHistory = null;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			// Check if any history was uploaded before
			String checkSql = "select count(*) from history where fromuser=? and touser=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, from);
			checkPs.setString(2, to);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int checkCount = checkRs.getInt(1);
			// Remove former history
			if (0 != checkCount) {
				String delSql = "delete from history where fromuser=? and touser=?";
				PreparedStatement delPs = conn.prepareStatement(delSql);
				delPs.setString(1, from);
				delPs.setString(2, to);
				delPs.executeQuery();
			}

			chatHistory = chatHistoryString.getBytes("ISO-8859-1");
			Blob fileBlob = new javax.sql.rowset.serial.SerialBlob(chatHistory);
			// Save new history
			String addSql = "insert into history values(?,?,?)";
			PreparedStatement addPs = conn.prepareStatement(addSql);
			addPs.setString(1, from);
			addPs.setString(2, to);
			addPs.setBlob(3, fileBlob);

			addPs.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			status = 1;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			status = 1;
		}
		return status;
	}

	/**
	 * Get saved chat history file
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public FileSaved getHistory(String from, String to) {
		FileSaved history = null;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			// Check if there is chat history
			String checkSql = "select count(*) from history where fromuser=? and touser=?";
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			checkPs.setString(1, from);
			checkPs.setString(2, to);
			ResultSet checkRs = checkPs.executeQuery();
			checkRs.next();
			int checkCount = checkRs.getInt(1);
			if (0 == checkCount) {
				// Not found
			} else {
				String getSql = "select * from history where fromuser=? and touser=?";
				PreparedStatement getPs = conn.prepareStatement(getSql);
				getPs.setString(1, from);
				getPs.setString(2, to);
				ResultSet getRs = getPs.executeQuery();
				getRs.next();
				String fromuser = getRs.getString(1);
				String touser = getRs.getString(2);
				Blob historyBlob = getRs.getBlob(3);
				byte[] historyBytes = historyBlob.getBytes(1, (int) historyBlob.length());
				String historyString = new String(historyBytes, "ISO-8859-1");
				history = new FileSaved();
				history.setFile(historyString);
				history.setSender(fromuser);
				history.setReceiver(touser);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return history;
	}

}
