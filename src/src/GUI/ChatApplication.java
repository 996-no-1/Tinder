package GUI;

import java.awt.EventQueue;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Client.Certificate;
import Client.ClientInfo;
import Client.FileSend;
import Client.SystemMsgForCertificate;
import Client.SystemMsgForNotify;
import SecurityAlgorithm.CaesarAlgorithm;
import SecurityAlgorithm.DES;
import SecurityAlgorithm.DH;
import SecurityAlgorithm.MD5;
import SecurityAlgorithm.PlayFairAlgorithm;
import SecurityAlgorithm.RSA;
import SecurityAlgorithm.TDES;
import Client.Message;
import SecurityAlgorithm.DigitalSignature;

/**
 * This class is the client starter. this class handle the business transaction
 * of the application. And communicate with the view layer ,other client and the
 * server
 * 
 * @author Zhichao Wang 2019/06/24
 * @version 1.0
 */
public class ChatApplication {
	static final int PORT = 2021; // Tcp port
	static final String HOST = "127.0.0.1"; // The host address
	ServerSocket serverSocket;// serversocket in server
	private int TCPPORT = 4000;// TCP Port,default port as server
	private String clientindex;// current client ID
	Socket tcpSocket;// use to connect with server
	private String path = "C:/Users/12284/Desktop/system/";

	ClientLogUI clientLogwindow;
	ClientChatUI clientChatwindow;
	InfoPromptUI infoPromptwindow;
	MessageEditUI messageEditwindow;

	private Envelope envelope = null;
	private String AsMode = "RSA";
	private String sMode = "DES";

	private static InputStream in = null;
	private static ObjectInputStream ois = null;
	private static OutputStream out = null;
	private static ObjectOutputStream oos = null;
	private RSA rsa = null;
	private DH dh = null;
	private DH clientDh = null;
	private BigInteger YB = null;
	private MD5 md5 = null;
	// 记录用户相关信息
	private int index = 0;
	String username = null;
	private Certificate certificate = null;
	private SystemMsgForCertificate systemMsgForCertificate = null;
	private Map<String, BigInteger> negotiateDH = new HashMap<>();

	public void setEnvelope(Envelope envelope1) {
		envelope = envelope1;
		System.err.println("Got A Message from " + envelope1.getSourceName());
	}

	/**
	 * set the asymmetric encryption mode
	 * 
	 * @param mode
	 */
	public void setAsMode(String mode) {
		this.AsMode = mode;
		System.err.println("use mode " + mode);
	}

	/**
	 * set the symmetric encryption mode
	 * 
	 * @param mode
	 */
	public void setSMode(String SMode) {
		this.sMode = SMode;
		System.err.println("use mode " + SMode);
	}

	/**
	 * constructor
	 * 
	 */
	public ChatApplication() {
		try {
			tcpSocket = new Socket(HOST, PORT);
			clientLogwindow = new ClientLogUI(this);
			clientChatwindow = new ClientChatUI(this);
			infoPromptwindow = new InfoPromptUI();
			messageEditwindow = new MessageEditUI(this);
			// get the output stream of the server
			out = tcpSocket.getOutputStream();
			oos = new ObjectOutputStream(out);
			// receive the response from server
			in = tcpSocket.getInputStream();
			ois = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		// Asynchronous processing message from view layer
		receiveMSgFromView();
		// start the window of the log page
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientLogwindow.frmLogPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * receive message from view layer.distribute to different business logic part
	 * 
	 */
	private void receiveMSgFromView() {
		// check envelope box and handle new envelope
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (envelope != null) {
						if (envelope.getSourceName().equals("ClientLogUI")) {
							System.err.println("processing");
							List<Object> msg = envelope.getMsg();
							if (msg.get(1) instanceof String) {
								String username = (String) msg.get(0);
								String password = (String) msg.get(1);
								// log verify
								if (!clientLog(username, password)) {
									envelope = null;
									continue;
								}
								// send certificate to server
								negotiateCertificate();
								//start to receive message from server constantly
								// receive certificate and notify
								receiveMsgFromServer();
								// negotiate with other client and send message
								serverStart();
							}
							envelope = null;
						} else if (envelope.getSourceName().equals("ClientChatUI")) {
							List<Object> objects = envelope.getMsg();
							if (objects.get(0) instanceof List<?>) {
								// send file button
								List<FileSend> fileSends = (List<FileSend>) objects.get(0);
								for (FileSend fileSend : fileSends) {
									fileSend.setFrom(username);
									String md5 = new MD5(new String(fileSend.getFile())).processMD5();
									fileSend.setMD5(md5);
									DigitalSignature digitalSignature = new DigitalSignature();
									digitalSignature.setD(rsa.getD());
									digitalSignature.setN(rsa.getN());
									fileSend.setSignature(digitalSignature.getSignature(md5));
									fileSend.setTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
									try {
										// ask other client port for server
										SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
										systemMsgForNotify.setSender(username);
										systemMsgForNotify.setReceiver(fileSend.getTo());
										systemMsgForNotify.setType(3);
										oos.writeObject(systemMsgForNotify);
										oos.flush();
										while (systemMsgForCertificate == null)
											System.err.print("");
										// send file 
										Socket tempSocket = new Socket(systemMsgForCertificate.getHost(),
												TCPPORT + systemMsgForCertificate.getPort());
										OutputStream tempOut = tempSocket.getOutputStream();
										ObjectOutputStream tempoos = new ObjectOutputStream(tempOut);
										tempoos.writeObject(fileSend);
										tempoos.close();
										systemMsgForCertificate = null;
										tempSocket.close();
										clientChatwindow.frmChat.setVisible(false);
										infoPromptwindow.setLabel("Send Successfully!");
										infoPromptwindow.nextMove(clientChatwindow.frmChat);
										infoPromptwindow.frmWarning.setVisible(true);
									} catch (Exception e1) {
										envelope = null;
										continue;
									}
								}

							} else if (objects.size() == 1 && objects.get(0) instanceof String) {
								String receiver = (String) objects.get(0);
								SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
								systemMsgForNotify.setType(2);
								systemMsgForNotify.setSender(username);
								systemMsgForNotify.setReceiver(receiver);
								// ask for certificate
								try {
									oos.writeObject(systemMsgForNotify);
									oos.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
								while (systemMsgForCertificate == null)
									System.err.print("");
								// get the target client's certificate
								Certificate certificate = systemMsgForCertificate.getCertificate();
								messageEditwindow
										.setAlgorithmBox(certificate.getsMode() + "+" + certificate.getAsMode());
								if (certificate.getAsMode().equals("RSA")) {
									messageEditwindow.setKeyFiled(true);
								} else {
									messageEditwindow.setKeyFiled(false);
									// negotiate DH info
									clientDh = new DH(certificate.getDHP());
									clientDh.setG(certificate.getDHG());
									clientDh.getRandom(16);
									clientDh.genrateYA();
									// send YA
									Socket tempSocket;
									try {
										tempSocket = new Socket(systemMsgForCertificate.getHost(),
												TCPPORT + systemMsgForCertificate.getPort());
										OutputStream tempOut = tempSocket.getOutputStream();
										ObjectOutputStream tempoos = new ObjectOutputStream(tempOut);
										List<Object> temp = new ArrayList<>();
										temp.add(username);
										temp.add(clientDh.getYA());
										temp.add(index);
										tempoos.writeObject(temp);
										while (YB == null)
											System.err.print("");
										// obtain YB
										clientDh.setYB(YB);
										YB = null;
										clientDh.genrateKA();
										// close connect
										tempoos.close();
										tempSocket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}

								}

							}

						} else if (envelope.getSourceName().equals("MessageEditUI")) {
							List<Object> objects = envelope.getMsg();
							Message message = new Message();
							message.setSender(username);
							message.setReceiver((String) objects.get(0));
							String msg = (String) objects.get(1);
							DigitalSignature digitalSignature = new DigitalSignature();
							digitalSignature.setD(rsa.getD());
							digitalSignature.setN(rsa.getN());
							List<BigInteger> signs = digitalSignature.getSignature(msg);
							String key = (String) objects.get(2);
							Certificate otherCertificate = systemMsgForCertificate.getCertificate();
							if (otherCertificate.getAsMode().equals("DH")) {
								key = clientDh.getKA().toString();
							}
							RSA temp = new RSA();
							temp.setE(otherCertificate.getRSAE());
							temp.setN(otherCertificate.getRSAN());
							message.setMsg(algorithmFactory(otherCertificate.getsMode(), key, msg, 0));
							if (otherCertificate.getAsMode().equals("RSA")) {
								List<BigInteger> keys = temp.encryptMsg(key);
								message.setKey(keys);
							}
							message.setDigitalSignature(signs);
							try {
								System.err.println("eeqwe");
								Socket tempSocket = new Socket(systemMsgForCertificate.getHost(),
										TCPPORT + systemMsgForCertificate.getPort());
								System.err.println("server： " + systemMsgForCertificate.getHost() + " " + TCPPORT
										+ systemMsgForCertificate.getPort());
								ObjectOutputStream tempoos = new ObjectOutputStream(tempSocket.getOutputStream());
								tempoos.writeObject(message);
								tempoos.flush();
								tempoos.close();
								systemMsgForCertificate = null;
								tempSocket.close();
								clientDh = null;
							} catch (Exception e1) {
								envelope = null;
							}
							systemMsgForCertificate = null;
						}

					}
					// clear envelope box
					envelope = null;
					System.err.print("");
				}
			}

		}).start();

	}

	/**
	 * receive message from other client
	 */
	protected void serverStart() {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					System.err.println("client's server start");
					serverSocket = new ServerSocket(TCPPORT + index);
					while (true) {
						Socket clientSocket = serverSocket.accept();
						System.err.println(clientSocket.getPort() + " online");
						InputStream clientIn = clientSocket.getInputStream();
						ObjectInputStream clientOis = new ObjectInputStream(clientIn);
						try {
							Object object = null;
							try {
								object = clientOis.readObject();
							} catch (EOFException e) {
								e.printStackTrace();
								continue;
							}
							// recognize the date type of the message 
							if (object instanceof FileSend) {
								// 若接受到文件
								System.err.println("Get a file");
								FileSend fileSend = (FileSend) object;
								BigInteger E = null;
								BigInteger N = null;
								SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
								systemMsgForNotify.setType(2);
								systemMsgForNotify.setSender(username);
								systemMsgForNotify.setReceiver(fileSend.getFrom());
								try {
									oos.writeObject(systemMsgForNotify);
									oos.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}

								// wait
								while (systemMsgForCertificate == null)
									System.err.print("");
								System.err.println("Got a certificate");
								Certificate senderCertificate = systemMsgForCertificate.getCertificate();
								E = senderCertificate.getRSAE();
								N = senderCertificate.getRSAN();
								DigitalSignature digitalSignature = new DigitalSignature();
								digitalSignature.setE(E);
								digitalSignature.setN(N);
								Boolean verify = true;
								
								
								
								
								///
								verify=digitalSignature.validSignature(fileSend.getMD5(),
								 fileSend.getSignature());
								String mString = "You Got A File!";
								if (verify)
									mString += "(Verified)";
								else
									mString += "(Unverified)";
								clientChatwindow.updateMsg(mString, fileSend.getFrom());
								File file = new File(path + "/" + username + "/" + fileSend.getFilename());
								BufferedOutputStream bbos = null;
								FileOutputStream fos = null;
								File file1 = null;
								try {
									byte[] buffer = fileSend.getFile();
									file1 = new File(path + fileSend.getFilename());
									fos = new FileOutputStream(file);
									bbos = new BufferedOutputStream(fos);
									bbos.write(buffer);
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									systemMsgForCertificate = null;
									if (bbos != null) {
										try {
											bbos.close();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									if (fos != null) {
										try {
											fos.close();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
								}
							} else if (object instanceof List<?>) {
								// receive YB
								List<Object> tempobject = (List<Object>) object;
								BigInteger YB = (BigInteger) tempobject.get(1);
								dh.getRandom(16);
								dh.genrateYA();
								dh.setYB(YB);
								dh.genrateKA();
								negotiateDH.put((String) tempobject.get(0), dh.getKA());
								// send my YA
								Socket tempSocket = new Socket(HOST, TCPPORT + (int) tempobject.get(2));
								ObjectOutputStream tempoos = new ObjectOutputStream(tempSocket.getOutputStream());
								tempoos.writeObject(dh.getYA());
								tempoos.flush();
								tempoos.close();
								tempSocket.close();
							} else if (object instanceof BigInteger) {
								YB = (BigInteger) object;
							} else if (object instanceof Message) {
								System.err.println("got a message");
								Message message = (Message) object;
								System.err.println(message.getSender());
								if (message.getKey() == null) {
									System.err.println("something wrong");
								}
								BigInteger E = null;
								BigInteger N = null;
								// request for certificate
								SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
								systemMsgForNotify.setType(2);
								systemMsgForNotify.setSender(username);
								systemMsgForNotify.setReceiver(message.getSender());
								// 向服务器询问凭证
								try {
									oos.writeObject(systemMsgForNotify);
									oos.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
								System.err.println("before");
								// wait
								while (systemMsgForCertificate == null)
									System.err.print("");
								System.out.println("Got");
								Certificate senderCertificate = systemMsgForCertificate.getCertificate();
								E = senderCertificate.getRSAE();
								N = senderCertificate.getRSAN();
								DigitalSignature digitalSignature = new DigitalSignature();
								digitalSignature.setE(E);
								digitalSignature.setN(N);
								String key = "";
								String mString = "";
								if (AsMode.equals("RSA")) {
									key = rsa.decryptMsg((List<BigInteger>) message.getKey());
									mString = (String) algorithmFactory(sMode, key, message.getMsg(), 1);
									System.err.println("信息 " + mString);
								} else {
									key = negotiateDH.get(message.getSender()).toString();
									mString = (String) algorithmFactory(sMode, key, message.getMsg(), 1);
								}
								System.err.println(mString);
								Boolean verify = true;
								verify=digitalSignature.validSignature(mString,message.getDigitalSignature());
								if (verify)
									mString += "(Verified)";
								else
									mString += "(Unverified)";
								clientChatwindow.updateMsg(mString, message.getSender());
								systemMsgForCertificate = null;
							}
						} catch (IOException e) {
							e.printStackTrace();
							try {
								clientIn.close();
								clientOis.close();
								clientSocket.close();
								break;
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} catch (EOFException e) {
					System.err.println("e");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * receive message from server
	 */
	private void receiveMsgFromServer() {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					while (true) {
						Thread.sleep(100);
						Object object = null;
						try {
							object = ois.readObject();
						} catch (EOFException e1) {
							e1.printStackTrace();
						}
						if (object instanceof SystemMsgForNotify) {
							// receive notify from server
							SystemMsgForNotify systemMsgForNotify = (SystemMsgForNotify) object;
							String msg = new CaesarAlgorithm("10").decryptMsg((String) systemMsgForNotify.getMsg());
							// update system message
							clientChatwindow.updateMsg(msg, systemMsgForNotify.getSender());
						} else if (object instanceof List<?>) {
							List<String> clientList = (List<String>) object;
							// update client list
							System.out.println("列表 ");
							System.out.println(clientList.size());
							if (clientChatwindow.msgSendBtn.isEnabled()) {
								clientChatwindow.refreshTree(clientList);
							}else {
								clientChatwindow.refreshBox(clientList);
							}
							
						} else if (object instanceof SystemMsgForCertificate) {
							// receive address and certificate
							System.err.println("hhhhcer");
							systemMsgForCertificate = (SystemMsgForCertificate) object;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * handle log transcation
	 * 
	 * @param username
	 * @param password
	 */
	private boolean clientLog(String username, String password) {
		System.err.println(username);
		System.err.println(password);
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setUsername(username);
		clientInfo.setMD5(new MD5(password).processMD5());
		try {
			oos.writeObject(clientInfo);
			oos.flush();
			while (true) {
				Object obj = ois.readObject();
				if (obj instanceof SystemMsgForNotify) {
					SystemMsgForNotify msgForNotify = (SystemMsgForNotify) obj;
					if (msgForNotify.getType() == 1) {
						continue;
					}
					String content = (String) msgForNotify.getMsg();
					content = new CaesarAlgorithm("10").decryptMsg(content);
					if (content.contains("check")) {
						// start alert page
						infoPromptwindow.setLabel(content);
						infoPromptwindow.nextMove(clientLogwindow.frmLogPage);
						clientLogwindow.frmLogPage.setVisible(false);
						infoPromptwindow.frmWarning.setVisible(true);
						return false;
					} else {
						/**
						 * log verified
						 */
						this.username = username;
						index = msgForNotify.getIndex();
						clientChatwindow.setUsername(username);
						infoPromptwindow.setLabel(content);
						infoPromptwindow.nextMove(clientChatwindow.frmChat);
						clientLogwindow.frmLogPage.setVisible(false);
						infoPromptwindow.frmWarning.setVisible(true);
						return true;
					}

				}

			}

		} catch (Exception e) {
			try {
				in.close();
				ois.close();
				out.close();
				oos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * send certificate to server
	 */
	private void negotiateCertificate() {
		certificate = new Certificate();
		if (AsMode.equals("RSA")) {
			// set the mode of the client
			certificate.setAsMode(AsMode);
		} else {
			certificate.setAsMode(AsMode);
			BigInteger p = BigInteger.probablePrime(5, new Random());
			dh = new DH(p);
			BigInteger g = dh.getPrimeRoot(p);
			dh.setG(g);
			certificate.setDHG(g);
			certificate.setDHP(p);
		}
		// generate local rsa public and private key
		rsa = new RSA();
		rsa.getRSAKey();
		certificate.setRSAE(rsa.getE());
		certificate.setRSAN(rsa.getN());
		certificate.setsMode(sMode);
		try {
			oos.writeObject(certificate);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * factory schema
	 * 
	 * @param mode
	 * @param key
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object algorithmFactory(String mode, String key, Object msg, int type) {
		if (key == null || key.length() == 0)
			key = "hello";
		if (type == 0) {
			switch (mode) {
			case "Caesar":
				return new CaesarAlgorithm(String.valueOf(((int) key.charAt(0)))).encryptMsg((String) msg);
			case "PlayFair":
				return new PlayFairAlgorithm(key).encryptMsg((String) msg);
			case "DES":
				return new DES(key).encryptText((String) msg);
			case "3DES":
				return new TDES(key).encryptMsg((String) msg);
			}
		} else {
			switch (mode) {
			case "Caesar":
				return new CaesarAlgorithm(String.valueOf(((int) key.charAt(0)))).decryptMsg((String) msg);
			case "PlayFair":
				return new PlayFairAlgorithm(key).decryptMsg((String) msg);
			case "DES":
				return new DES(key).decryptText((List<BitSet>) msg);
			case "3DES":
				return new TDES(key).decryptMsg((List<BitSet>) msg);
			}
		}

		return null;

	}

	public static void main(String[] args) {
		ChatApplication chatApplication = new ChatApplication();
		chatApplication.start();
	}

}
