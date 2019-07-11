package SecurityAlgorithm;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * RSA algorthm
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class RSA {

	private BigInteger e;
	private BigInteger n;
	private BigInteger d;

	/**
	 * @param e
	 *            the e to set
	 */
	public void setE(BigInteger e) {
		this.e = e;
	}

	/**
	 * @param n
	 *            the n to set
	 */
	public void setN(BigInteger n) {
		this.n = n;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(BigInteger d) {
		this.d = d;
	}

	/**
	 * use to generate the public and the private key
	 * 
	 * @return
	 */
	public void getRSAKey() {
		BigInteger p = BigInteger.probablePrime(128, new Random()), q = BigInteger.probablePrime(128, new Random());
		while (!p.isProbablePrime(128)) {
			p = BigInteger.probablePrime(128, new Random());
		}
		while (!q.isProbablePrime(128) || q.equals(p)) {
			p = BigInteger.probablePrime(128, new Random());
		}
		BigInteger temp = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
		e = BigInteger.probablePrime(128, new Random());
		while (!e.isProbablePrime(128) || e.compareTo(temp) >= 0) {
			e = BigInteger.probablePrime(128, new Random());
		}
		n = p.multiply(q);
		List<BigInteger[]> bigIntegers = new LinkedList<>();
		BigInteger[] bigIntegersPair = { e, temp };

		bigIntegers.add(bigIntegersPair.clone());
		int count = 1;
		BigInteger modNum = new BigInteger("0");
		while (true) {
			if (count % 2 != 0) {
				modNum = bigIntegersPair[1].mod(bigIntegersPair[0]);
				bigIntegersPair[1] = modNum;
			} else {
				modNum = bigIntegersPair[0].mod(bigIntegersPair[1]);
				bigIntegersPair[0] = modNum;
			}
			bigIntegers.add(bigIntegersPair.clone());
			count++;
			if (bigIntegersPair[0].equals(BigInteger.ONE) || bigIntegersPair[1].equals(BigInteger.ONE)) {
				break;
			}
		}
		BigInteger k = null;
		if (bigIntegersPair[0].equals(BigInteger.ONE)) {
			k = new BigInteger("1");
			count = 2;
		} else {
			d = new BigInteger("1");
			count = 1;
		}
		while (bigIntegers.size() != 0) {
			BigInteger[] bigIntegersTemp = bigIntegers.get(bigIntegers.size() - 1);
			if (count % 2 == 0)
				d = k.multiply(bigIntegersTemp[1]).add(new BigInteger("1")).divide(bigIntegersTemp[0]);
			else
				k = d.multiply(bigIntegersTemp[0]).subtract(new BigInteger("1")).divide(bigIntegersTemp[1]);
			bigIntegers.remove(bigIntegers.size() - 1);
			count++;
		}
	}

	/**
	 * this function is used to calculate the big number's power and module
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private BigInteger powerMod(BigInteger a, BigInteger b, BigInteger c) {
		if (b.equals(BigInteger.ONE)) {
			return a.mod(c);
		} else {
			if (b.mod(new BigInteger("2")).equals(BigInteger.ZERO)) {
				return powerMod(a.multiply(a).mod(c), b.divide(new BigInteger("2")), c);
			} else {
				return powerMod(a.multiply(a).mod(c), b.divide(new BigInteger("2")), c).multiply(a).mod(c);
			}

		}

	}

	/**
	 * use RSA to encrypt plaintext
	 * 
	 * @param msg
	 * @return
	 */
	public List<BigInteger> encryptMsg(String msg) {
		List<BigInteger> cypherText = new LinkedList<>();
		char[] temp = msg.toCharArray();
		for (int i = 0; i < temp.length; i++) {
			cypherText.add(powerMod(new BigInteger("" + (int) temp[i]), e, n));
		}
		return cypherText;
	}

	/**
	 * use RSA to decrypt cyphertext
	 * 
	 * @param cypherText
	 * @return
	 */
	public String decryptMsg(List<BigInteger> cypherText) {
		String plainText = "";
		for (BigInteger bigInteger : cypherText) {
			plainText += (char) Integer.parseInt(powerMod(bigInteger, d, n).toString());
		}
		return plainText;
	}

	/**
	 * @return the e
	 */
	public BigInteger getE() {
		return e;
	}

	/**
	 * @return the n
	 */
	public BigInteger getN() {
		return n;
	}

	/**
	 * @return the d
	 */
	public BigInteger getD() {
		return d;
	}

}
