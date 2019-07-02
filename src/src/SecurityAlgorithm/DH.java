package SecurityAlgorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DH public-private algorthm
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class DH {

	private BigInteger p;
	private BigInteger g;
	private BigInteger random;
	private BigInteger YA;
	private BigInteger YB;
	private BigInteger KA;

	public DH(BigInteger p) {
		this.p = p;
	}

	/**
	 * @return the p
	 */
	public BigInteger getP() {
		return p;
	}

	/**
	 * @param p
	 *            the p to set
	 */
	public void setP(BigInteger p) {
		this.p = p;
	}

	/**
	 * @return the g
	 */
	public BigInteger getG() {
		return g;
	}

	/**
	 * @param g
	 *            the g to set
	 */
	public void setG(BigInteger g) {
		this.g = g;
	}

	/**
	 * @return the random
	 */
	public BigInteger getRandom() {
		return random;
	}

	/**
	 * @param random
	 *            the random to set
	 */
	public void setRandom(BigInteger random) {
		this.random = getRandom(64);
	}

	/**
	 * @return the yA
	 */
	public BigInteger getYA() {
		return YA;
	}

	/**
	 * @param yA
	 *            the yA to set
	 */
	public void setYA(BigInteger yA) {
		YA = yA;
	}

	/**
	 * @return the yB
	 */
	public BigInteger getYB() {
		return YB;
	}

	/**
	 * @param yB
	 *            the yB to set
	 */
	public void setYB(BigInteger yB) {
		YB = yB;
	}

	/**
	 * @return the kA
	 */
	public BigInteger getKA() {
		return KA;
	}

	/**
	 * @param kA
	 *            the kA to set
	 */
	public void setKA(BigInteger kA) {
		KA = kA;
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
				// return powerMod(a.multiply(a).mod(c),powerMod(b.divide(new
				// BigInteger("2")),a,BigInteger.ZERO), c);
				return powerMod(a.multiply(a).mod(c), b.divide(new BigInteger("2")), c).multiply(a).mod(c);
			}
		}

	}

	/**
	 * this function is used to calculate the prime root number.
	 * 
	 * @param primeNumber
	 * @return
	 */
	public BigInteger getPrimeRoot(BigInteger primeNumber) {
		BigInteger result = null;
		for (int i = 2; i < 20; i++) {
			result = new BigInteger("" + i);
			BigInteger cur = new BigInteger("1");
			List<BigInteger> moduel = new ArrayList<>();
			Boolean flag = false;
			while (!cur.equals(primeNumber)) {
				// System.err.print(cur+" ");
				BigInteger mod = powerMod(result, cur, primeNumber);
				if (moduel.contains((BigInteger) mod)) {
					flag = true;
					break;
				} else {

					moduel.add(mod);
					cur = cur.add(BigInteger.ONE);
				}
			}
			if (!flag) {
				return result;
			}
		}
		return null;

	}

	/**
	 * get a random number
	 * 
	 * @param length
	 * @return
	 */
	public BigInteger getRandom(int length) {
		random = BigInteger.probablePrime(length, new Random());
		return random;
	}

	/**
	 * generate YA
	 * 
	 * @return
	 */
	public BigInteger genrateYA() {
		BigInteger bigInteger = powerMod(g, random, p);
		YA = bigInteger;
		return YA;
	}

	/**
	 * generate KA
	 * 
	 * @return
	 */
	public BigInteger genrateKA() {
		BigInteger bigInteger = powerMod(YB, random, p);
		KA = bigInteger;
		return KA;
	}

}
