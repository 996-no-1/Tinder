package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
/**
 * Files passed between customers
 */
public class FileSend implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	private String time;
	private String from;
	private String to;
	private String filename;
	private byte[] file;
	private String MD5;
	private List<BigInteger> signature;

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the file
	 */
	public byte[] getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}

	/**
	 * @return the signature
	 */
	public List<BigInteger> getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(List<BigInteger> signature) {
		this.signature = signature;
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

}
