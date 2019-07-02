package Client;

import java.io.Serializable;

/**
 * Server notify client about the address and the certificate of other client.
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class SystemMsgForCertificate implements Serializable {
	private static final long serialVersionUID = 5L;
	private String host;
	private int port;
	private Certificate certificate;

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the certificate
	 */
	public Certificate getCertificate() {
		return certificate;
	}

	/**
	 * @param certificate
	 *            the certificate to set
	 */
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

}
