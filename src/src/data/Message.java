package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * Used to encapsulate messages and related encrypted information between users
 *
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;
	private String time;
	private String from;
	private String to;
	private String message;
	private List<BigInteger> signature;
	private List<BigInteger> key;

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
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
	 * @return the signature
	 */
	public List<BigInteger> getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(List<BigInteger> signature) {
		this.signature = signature;
	}

	/**
	 * @return the key
	 */
	public List<BigInteger> getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(List<BigInteger> key) {
		this.key = key;
	}

}
