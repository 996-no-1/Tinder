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
	private int age;
	private String gender;
	private String note;

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

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

}
