package db.entity;

/**
 * User entity class
 * 
 * @author DesmondCobb
 * @version 20190703.1146
 *
 */
public class User {

	private String username = null;
	private String gender = null;
	private int age = 0;
	private String note = null;
	private boolean ban = false;
	private String hashedPassword = null;

	/**
	 * Default constructor
	 */
	public User() {
	}

	/**
	 * Constructor using fields
	 * 
	 * @param username
	 * @param nickname
	 * @param gender
	 * @param age
	 * @param email
	 */
	public User(String username, String gender, int age, String note, boolean isBanned, String hashedPassword) {
		super();
		this.username = username;
		this.gender = gender;
		this.age = age;
		this.setNote(note);
		this.ban = isBanned;
		this.hashedPassword = hashedPassword;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the ban
	 */
	public boolean isBan() {
		return ban;
	}

	/**
	 * @param ban
	 *            the ban to set
	 */
	public void setBan(boolean ban) {
		this.ban = ban;
	}

	/**
	 * @return the hashedPassword
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * @param hashedPassword
	 *            the hashedPassword to set
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

}
