package db.entity;

/**
 * Group message entity
 * 
 * @author DesmondCobb
 *
 */
public class GroupMessage {

	private String groupname = null;
	private String senderUsername = null;
	private String message = null;
	private long time = -1;

	/**
	 * @param groupname
	 * @param senderUsername
	 * @param message
	 * @param time
	 */
	public GroupMessage(String groupname, String senderUsername, String message, long time) {
		super();
		this.groupname = groupname;
		this.senderUsername = senderUsername;
		this.message = message;
		this.time = time;
	}

	/**
	 * @return the groupname
	 */
	public String getGroupname() {
		return groupname;
	}

	/**
	 * @param groupname
	 *            the groupname to set
	 */
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	/**
	 * @return the senderUsername
	 */
	public String getSenderUsername() {
		return senderUsername;
	}

	/**
	 * @param senderUsername
	 *            the senderUsername to set
	 */
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

}
