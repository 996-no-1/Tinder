package algorithm;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

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
	 * 生成电子签名
	 * @param msg
	 * @param privateKey
	 * @return
	 */
	public List<BigInteger> getSignature(String msg) {
		List<BigInteger> cypherText=new LinkedList<>();
		char[] temp=msg.toCharArray();
		for (int i = 0; i < temp.length; i++) {
			//cypherText.add(quickPow(new BigInteger(""+(int)temp[i]), e).mod(n));
			cypherText.add(powerMod(new BigInteger(""+(int)temp[i]),d,n));
		}
		return cypherText;
	}
	
	/**
	 * 验证电子签名
	 * @param msg
	 * @param signature
	 * @param publicKey
	 * @return
	 */
	public boolean validSignature(String msg,List<BigInteger> signature) {
		String plainText="";
		for (BigInteger bigInteger : signature) {
			plainText+=(char)Integer.parseInt(powerMod(bigInteger, e, n).toString());
		}
	//	System.err.println("sign : "+plainText);
		if (plainText.equals(msg)) {
			return true;
		}else return false;
	}
}
