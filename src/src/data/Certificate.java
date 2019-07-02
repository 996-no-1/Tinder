package data;
import java.io.Serializable;
import java.math.BigInteger;
/**
 * This class encapsulates the credentials of the server and client
 *
 */
public class Certificate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private BigInteger E;
	private BigInteger N;
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
	 * @return the e
	 */
	public BigInteger getE() {
		return E;
	}
	/**
	 * @param e the e to set
	 */
	public void setE(BigInteger e) {
		E = e;
	}
	/**
	 * @return the n
	 */
	public BigInteger getN() {
		return N;
	}
	/**
	 * @param n the n to set
	 */
	public void setN(BigInteger n) {
		N = n;
	}
	
	
	
}
