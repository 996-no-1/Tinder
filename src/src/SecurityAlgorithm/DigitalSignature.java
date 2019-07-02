package SecurityAlgorithm;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
/**
 * digital signature algorthm
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class DigitalSignature {

	
	private  BigInteger e;
	private  BigInteger n;
	private  BigInteger d;
	
	
	
	/**
	 * @return the e
	 */
	public BigInteger getE() {
		return e;
	}

	/**
	 * @param e the e to set
	 */
	public void setE(BigInteger e) {
		this.e = e;
	}

	/**
	 * @return the n
	 */
	public BigInteger getN() {
		return n;
	}

	/**
	 * @param n the n to set
	 */
	public void setN(BigInteger n) {
		this.n = n;
	}

	/**
	 * @return the d
	 */
	public BigInteger getD() {
		return d;
	}

	/**
	 * @param d the d to set
	 */
	public void setD(BigInteger d) {
		this.d = d;
	}


	
/**
 * this function is used to calculate the big number's power and module
 * @param a
 * @param b
 * @param c
 * @return
 */
	private BigInteger powerMod(BigInteger a,BigInteger b,BigInteger c) {
		if(b.equals(BigInteger.ONE)){
			return a.mod(c);
		}else {
			if (b.mod(new BigInteger("2")).equals(BigInteger.ZERO)) {
				return powerMod(a.multiply(a).mod(c), b.divide(new BigInteger("2")), c);
			}else {
				//return powerMod(a.multiply(a).mod(c),powerMod(b.divide(new BigInteger("2")),a,BigInteger.ZERO), c);
				return powerMod(a.multiply(a).mod(c), b.divide(new BigInteger("2")), c).multiply(a).mod(c);
			}
			
			
		}
		
	}
	
	
	/**
	 * generate
	 * @param msg
	 * @param privateKey
	 * @return
	 */
	public List<BigInteger> getSignature(String msg) {
		if(msg.length()>32)msg=msg.substring(0,32);
		List<BigInteger> cypherText=new LinkedList<>();
		char[] temp=msg.toCharArray();
		for (int i = 0; i < temp.length; i++) {
			cypherText.add(powerMod(new BigInteger(""+(int)temp[i]),d,n));
		}
		return cypherText;
	}
	
	/**
	 * verify
	 * @param msg
	 * @param signature
	 * @param publicKey
	 * @return
	 */
	public boolean validSignature(String msg,List<BigInteger> signature) {
		String plainText="";
		if(msg.length()>32)msg=msg.substring(0,32);
		for (BigInteger bigInteger : signature) {
			plainText+=(char)Integer.parseInt(powerMod(bigInteger, e, n).toString());
		}
		if (plainText.equals(msg)) {
			return true;
		}else return false;
	}
}
