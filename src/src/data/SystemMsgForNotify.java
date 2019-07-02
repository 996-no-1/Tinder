package data;

import java.io.Serializable;
/**
 * Used for negotiation between server and client
 *
 */
public class SystemMsgForNotify implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7L;
	String sender;
	String msg;
	int type;//0(log)1(pure msg)2(request certificate)
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
}
