package data;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
/**
 * This class is used to encapsulate the user's username and password.
 */
public class ClientInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String username;
	private List<BigInteger> password;
	private int type;//0(sign up),1(sign in)
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public List<BigInteger> getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(List<BigInteger> password) {
		this.password = password;
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
