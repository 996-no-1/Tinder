package db.entity;

/**
 * User entity class
 * 
 * @author DesmondCobb
 *
 */
public class User {
	
	private String username = null;
	private String nickname = null;
	private String gender = null;
	private int age = 0;
	private String email = null;

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
	public User(String username, String nickname, String gender, int age, String email) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.gender = gender;
		this.age = age;
		this.email = email;
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
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
