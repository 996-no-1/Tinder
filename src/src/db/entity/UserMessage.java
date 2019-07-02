package db.entity;

/**
 * User message entity
 * 
 * @author DesmondCobb
 *
 */
public class UserMessage {

	private String fromUsername = null;
	private String toUsername = null;
	private String message = null;
	private long time = -1;

	/**
	 * @param fromUsername
	 * @param toUsername
	 * @param message
	 * @param time
	 */
	public UserMessage(String fromUsername, String toUsername, String message, long time) {
		super();
		this.fromUsername = fromUsername;
		this.toUsername = toUsername;
		this.message = message;
		this.time = time;
	}

	/**
	 * @return the fromUsername
	 */
	public String getFromUsername() {
		return fromUsername;
	}

	/**
	 * @param fromUsername
	 *            the fromUsername to set
	 */
	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	/**
	 * @return the toUsername
	 */
	public String getToUsername() {
		return toUsername;
	}

	/**
	 * @param toUsername
	 *            the toUsername to set
	 */
	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
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
