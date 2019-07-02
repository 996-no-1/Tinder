package Client;

import java.io.Serializable;

/**
 * used to transfer message during log phase
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class ClientInfo implements Serializable {
	private static final long serialVersionUID = 2L;
	private String username;
	private String MD5;

	/**
	 * @return the password
	 */
	public String getUsrname() {
		return username;
	}

	/**
	 * @param username
	 *            the password to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the mD5
	 */
	public String getMD5() {
		return MD5;
	}

	/**
	 * @param mD5
	 *            the mD5 to set
	 */
	public void setMD5(String mD5) {
		MD5 = mD5;
	}

}
