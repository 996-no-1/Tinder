package database;

import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.*;
import data.FileSend;

/**
 * Database access object FileDao for accessing files
 * 
 * @author DesmondCobb
 *
 */

public class FileDao {

	/**
	 * Add a new file to database
	 * 
	 * @param newFile
	 * @return
	 */
	public int addFile(FileSend newFile) {
		// Status code
		// 0 = OK
		// 1 = Internal error, maybe not successful
		int status = 0;
		String from = newFile.getFrom();
		String to = newFile.getTo();
		String filename = newFile.getFilename();
		String md5 = newFile.getMD5();
		List<BigInteger> signature = newFile.getSignature();
		byte[] file = newFile.getFile();

		try {
			Blob fileBlob = new javax.sql.rowset.serial.SerialBlob(file);
			Connection conn = DBFactory.getINSTANCE().getConnection();

			String addSql = "insert into file values(?,?,?,?,?,?,?)";
			PreparedStatement addPs = conn.prepareStatement(addSql);
			addPs.setString(1, TimeFactory.getTimeSomeWhat());
			addPs.setString(2, from);
			addPs.setString(3, to);
			addPs.setString(4, filename);
			addPs.setBlob(5, fileBlob);
			addPs.setString(6, md5);
			int signSize = signature.size();
			String signString = "";
			for (int i = 0; i < signSize - 1; i++) {
				signString += signature.get(i).toString();
				signString += "_";
			}
			signString += signature.get(signSize - 1).toString();
			addPs.setString(7, signString);
			addPs.executeQuery();
			// ResultSet addRs = addPs.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			status = 1;
		}

		return status;
	}

	/**
	 * Get file list from a user to another
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<FileSend> getFile(String from, String to) {
		List<FileSend> fileList = null;
		try {
			Connection conn = DBFactory.getINSTANCE().getConnection();
			String getSql = "select * from file where fromuser=? and touser=?";
			PreparedStatement getPs = conn.prepareStatement(getSql);
			getPs.setString(1, from);
			getPs.setString(2, to);
			ResultSet getRs = getPs.executeQuery();
			while (getRs.next()) {
				if (null == fileList) {
					fileList = new ArrayList<FileSend>();
				}
				String time = TimeFactory.parseSomeWhat2GroupStandard(getRs.getString(1));
				String fileName = getRs.getString(4);
				String md5 = getRs.getString(6);
				String signString = getRs.getString(7);
				Blob fileBlob = getRs.getBlob(5);
				byte[] file = fileBlob.getBytes(1, (int) fileBlob.length());
				FileSend gFile = new FileSend();
				// Reform FileSend object
				gFile.setFile(file);
				gFile.setFilename(fileName);
				gFile.setFrom(from);
				gFile.setTo(to);
				gFile.setMD5(md5);
				// Parse signature to BigIntegers
				List<BigInteger> signature = new ArrayList<BigInteger>();
				String[] sigs = signString.split("_");
				for (int i = 0; i < sigs.length; i++) {
					BigInteger tmp = new BigInteger(sigs[i]);
					signature.add(tmp);
				}
				gFile.setSignature(signature);
				gFile.setTime(time);
				fileList.add(gFile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return fileList;
	}
}
