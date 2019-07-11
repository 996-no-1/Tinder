package Client;

import java.io.Serializable;

/**
 * This class define the massages which are sent between clients. Default mode
 * is caesar algorithm.
 * 
 * @author Zhichao Wang 2019/5/24
 * @version 1.0
 */
public class SystemMsgForNotify implements Serializable {

	private static final long serialVersionUID = 6L;
	private String sender;
	private String receiver;
	private int index;
	private String msg;
	private int type;// 0���ڴ����¼��1���ڴ���ͨ�ţ�2����ѯ�������û���,3���ڻ�ȡ����ļ����˿�

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

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
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
