package Client;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * This class used to transfer among clients during chat
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 4L;
	private String sender;
	private String receiver;
	private Object msg;
	private Object key;
	private List<BigInteger> digitalSignature;
	private BigInteger MD5;
	private int msgType;

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver
	 *            the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the msg
	 */
	public Object getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(Object msg) {
		this.msg = msg;
	}

	/**
	 * @return the key
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(Object key) {
		this.key = key;
	}

	/**
	 * @return the digitalSignature
	 */
	public List<BigInteger> getDigitalSignature() {
		return digitalSignature;
	}

	/**
	 * @param digitalSignature
	 *            the digitalSignature to set
	 */
	public void setDigitalSignature(List<BigInteger> digitalSignature) {
		this.digitalSignature = digitalSignature;
	}

	/**
	 * @return the mD5
	 */
	public BigInteger getMD5() {
		return MD5;
	}

	/**
	 * @param mD5
	 *            the mD5 to set
	 */
	public void setMD5(BigInteger mD5) {
		MD5 = mD5;
	}

	/**
	 * @return the msgType
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType
	 *            the msgType to set
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

}
