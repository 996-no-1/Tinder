package Client;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * This class represent the certificate of the client
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class Certificate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String asMode;
	private BigInteger RSAN;
	private BigInteger RSAE;
	private BigInteger DHP;
	private BigInteger DHG;
	private String sMode;
	private long createDate;
	private long lifeSpan;

	/**
	 * @return the rSAN
	 */
	public BigInteger getRSAN() {
		return RSAN;
	}

	/**
	 * @param rSAN
	 *            the rSAN to set
	 */
	public void setRSAN(BigInteger rSAN) {
		RSAN = rSAN;
	}

	/**
	 * @return the rSAE
	 */
	public BigInteger getRSAE() {
		return RSAE;
	}

	/**
	 * @param rSAE
	 *            the rSAE to set
	 */
	public void setRSAE(BigInteger rSAE) {
		RSAE = rSAE;
	}

	/**
	 * @return the dHP
	 */
	public BigInteger getDHP() {
		return DHP;
	}

	/**
	 * @param dHP
	 *            the dHP to set
	 */
	public void setDHP(BigInteger dHP) {
		DHP = dHP;
	}

	/**
	 * @return the dHG
	 */
	public BigInteger getDHG() {
		return DHG;
	}

	/**
	 * @param dHG
	 *            the dHG to set
	 */
	public void setDHG(BigInteger dHG) {
		DHG = dHG;
	}

	/**
	 * @return the createDate
	 */
	public long getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the lifeSpan
	 */
	public long getLifeSpan() {
		return lifeSpan;
	}

	/**
	 * @param lifeSpan
	 *            the lifeSpan to set
	 */
	public void setLifeSpan(long lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	/**
	 * @return the asMode
	 */
	public String getAsMode() {
		return asMode;
	}

	/**
	 * @param asMode
	 *            the asMode to set
	 */
	public void setAsMode(String asMode) {
		this.asMode = asMode;
	}

	/**
	 * @return the sMode
	 */
	public String getsMode() {
		return sMode;
	}

	/**
	 * @param sMode
	 *            the sMode to set
	 */
	public void setsMode(String sMode) {
		this.sMode = sMode;
	}

}
