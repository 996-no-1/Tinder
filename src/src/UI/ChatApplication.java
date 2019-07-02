package UI;
import java.awt.EventQueue;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sun.org.apache.xpath.internal.operations.Bool;

import aes.AES;
import algorithm.DES;
import algorithm.DigitalSignature;
import algorithm.MD5;
import algorithm.RSA;
import data.Certificate;
import data.ClientInfo;
import data.Envelope;
import data.FileSaved;
import data.FileSend;
import data.Message;
import data.SystemMsgForNotify;
/**
 * This class is used to connect to the server and interact as a backend with the front end.
 */
public class ChatApplication {

	// Modify IP and port
	static final int PORT = 2200; // Tcp port
	static final String HOST = "127.0.0.1"; // The host address
	Socket tcpSocket;// Used to connect to the server

	ClientLogUI clientLogwindow;// Used to manage windows
	ClientChatUI clientChatwindow;// Used to manage windows
	InfoPromptUI infoPromptwindow;// Used to manage windows
	MessageEditUI messageEditwindow;// Used to manage windows
	ViewHistoryUI viewHistorywindow;
	String path = "C:/Users/12284/Desktop/system/";
	private Envelope envelope = null;

	private static InputStream in = null;
	private static ObjectInputStream ois = null;
	private static OutputStream out = null;
	private static ObjectOutputStream oos = null;
	private RSA rsa = null;
	private RSA serverRsa = null;
	// Record user related information
	 String username = null;
	private String password = null;
	private Certificate certificate = null;
	private Certificate otherCertificate = null;
	private Certificate serverCertificate = null;
/**
 * Serve information in your mailbox
 * @param envelope1
 */
	public void setEnvelope(Envelope envelope1) {
		envelope = envelope1;
		System.err.println("Got A Message from " + envelope1.getSourceName());
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
			viewHistorywindow=new ViewHistoryUI(this);
			// Get the server output stream and send the customer login information to the server
			out = tcpSocket.getOutputStream();
			oos = new ObjectOutputStream(out);
			// Accept the response from the server
			in = tcpSocket.getInputStream();
			ois = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		// Thread that processes messages asynchronously
		receiveMSgFromView();
		// Start the login window of the client
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
	 * Used to asynchronously accept data from the front end for processing in a manner similar to factory mode
	 */
	private void receiveMSgFromView() {
		// Handling information by detecting if a new value exists in the envelope
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (envelope != null) {
						// when receive message
						System.err.println(envelope.getSourceName());
						if (envelope.getSourceName().equals("ClientLogUI")) {
							// get the message and distribute it
							System.err.println("processing log");
							List<Object> msg = envelope.getMsg();
							if (msg.get(1) instanceof String) {
								String username = (String) msg.get(0);
								String password = (String) msg.get(1);
								int type = (int) msg.get(2);
								// login verify
								if(!clientLog(username, password, type)){
									envelope=null;
									
									continue;
								}
								// send certificate
								negotiateCertificate();
								// receive message
								receiveMsgFromServer();
							}
							envelope = null;
						} else if (envelope.getSourceName().equals("ClientChatUI")) {
							// widget for sender
							List<Object> objects = envelope.getMsg();
							if (objects.size() == 1) {
								// send file button
								FileSend fileSend = (FileSend) objects.get(0);
								fileSend.setFrom(username);
								String md5 = new MD5(new String(fileSend.getFile())).processMD5();
								fileSend.setMD5(md5);
								DigitalSignature digitalSignature = new DigitalSignature();
								digitalSignature.setD(rsa.getD());
								digitalSignature.setN(rsa.getN());
								fileSend.setSignature(digitalSignature.getSignature(md5));
								fileSend.setTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
								try {
									oos.writeObject(fileSend);
									System.err.println(fileSend.getFilename());
									oos.flush();
								} catch (Exception e1) {
									envelope = null;
									continue;
								}

							} else if (objects.size() == 2) {
								// upload
								String withWho = (String) objects.get(0);
								File file = (File) objects.get(1);
								System.err.println(file.getAbsolutePath());
								// read and encrypt
								try {
									BufferedReader br = new BufferedReader(new FileReader(file));
									String content = "";
									String cur = "";
									while (!((cur = br.readLine()) == null)) {
										content += cur;
									}
									br.close();
									DES des = new DES(username + password);
									List<BitSet> encrypt = des.encryptText(content);
									String result = BitSetToString(encrypt);
									FileSaved fileSaved = new FileSaved();
									fileSaved.setFile(result);
									fileSaved.setSender(username);
									fileSaved.setReceiver(withWho);
									try {
										oos.writeObject(fileSaved);
										oos.flush();
									} catch (EOFException e1) {
										envelope = null;
										continue;
									}
								} catch (Exception e1) {
									e1.printStackTrace();
									envelope = null;
								}
							}
						} else if (envelope.getSourceName().equals("MessageEditUI")) {
							List<Object> objects = envelope.getMsg();
							Message message = new Message();
							message.setFrom(username);
							message.setTo((String) objects.get(0));
							String msg = (String) objects.get(1);
							
							File file = new File(path + "/" + (String)objects.get(0) + ".txt");
							PrintWriter pw;
							try {
								pw = new PrintWriter(new FileWriter(file,true));
								pw.println(message.getFrom()+" >> "+msg+" "+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
								pw.close();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							
							
							DigitalSignature digitalSignature = new DigitalSignature();
							digitalSignature.setD(rsa.getD());
							digitalSignature.setN(rsa.getN());
							List<BigInteger> signs = digitalSignature.getSignature(msg);
							String key = (String) objects.get(2);
							msg = new AES().Encrypt(msg, key);
							RSA temp = new RSA();
							// ask certificate
							SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
							systemMsgForNotify.setType(2);
							systemMsgForNotify.setSender(username);
							systemMsgForNotify.setMsg(message.getTo());
							try {
								oos.writeObject(systemMsgForNotify);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.err.println("before");
							while (otherCertificate == null)
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							;
							System.err.println("after");
							temp.setE(otherCertificate.getE());
							temp.setN(otherCertificate.getN());
							List<BigInteger> keys = temp.encryptMsg(key);
							message.setMessage(msg);
							message.setKey(keys);
							message.setSignature(signs);
							message.setTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
							try {
								oos.writeObject(message);
								oos.flush();
								
								
								
								
							} catch (Exception e1) {
								envelope = null;
							}
							otherCertificate = null;
						}
						// reset
						envelope = null;
						System.err.print("");
					}
				}
			}

		}).start();

	}

	/**
	 * reseive server
	 */
	private void receiveMsgFromServer() {
		new Thread() {
			public void run() {
				try {
					while (true) {
						Object object = null;
						try {
							Thread.sleep(200);
							object = ois.readObject();
							System.err.println("hhhp");
						} catch (EOFException e1) {
							e1.printStackTrace();
						}
						if (object instanceof SystemMsgForNotify) {
							// receive message
							SystemMsgForNotify systemMsgForNotify = (SystemMsgForNotify) object;
							if (systemMsgForNotify.getType() == 0) {
								continue;
							}
							String msg = systemMsgForNotify.getMsg();
							// update info
							System.err.println("ppp" + msg);
							clientChatwindow.updateMsg(msg, systemMsgForNotify.getSender());
						} else if (object instanceof Certificate) {
							System.err.println("IN");
							otherCertificate = (Certificate) object;
						} else if (object instanceof List<?>) {
							@SuppressWarnings("unchecked")
							List<String> clientList = (List<String>) object;
							for (String string : clientList) {
								System.err.println(string);
							}
							// update
							clientChatwindow.refreshTree(clientList);
						} else if (object instanceof Message) {
							Message message = (Message) object;
							BigInteger E = null;
							BigInteger N = null;
							// request for certificate
							SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
							systemMsgForNotify.setType(2);
							systemMsgForNotify.setSender(username);
							systemMsgForNotify.setMsg(message.getFrom());
							try {
								oos.writeObject(systemMsgForNotify);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// wait
							while (true) {
								Object obj = null;
								try {
									obj = ois.readObject();
									if (obj instanceof Certificate) {
										Certificate senderCertificate = (Certificate) obj;
										E = certificate.getE();
										N = certificate.getN();
										DigitalSignature digitalSignature = new DigitalSignature();
										digitalSignature.setE(E);
										digitalSignature.setN(N);
										String key = rsa.decryptMsg(message.getKey());
										String mString = new AES().Decrypt(message.getMessage(), key);
										System.err.println(mString);
										Boolean verify = true;
										// digitalSignature.validSignature(mString,message.getSignature());
										if (verify)
											mString += "(Verified)"+" "+message.getTime();
										else
											mString += "(Unverified)"+" "+message.getTime();
										System.err.println(mString);
										clientChatwindow.updateMsg(mString, message.getFrom());
										File file = new File(path + "/" + message.getFrom() + ".txt");
										PrintWriter pw = new PrintWriter(new FileWriter(file,true));
										pw.println(message.getFrom()+" >> "+mString);
										pw.close();
										
										//new append
										File publicFile = new File("C:/Users/12284/Desktop/system" + "/"+username
												+"/" + message.getFrom() + ".txt");
												Envelope envelope1 = new Envelope();
												envelope1.setSourceName("ClientChatUI");
												List<Object> objects1 = new ArrayList<>();
												objects1.add(message.getFrom());
												objects1.add(publicFile);
												envelope1.setMsg(objects1);
												setEnvelope(envelope1);
												
										
										
										
										break;
									}
								} catch (EOFException e1) {
									continue;
								}
							}

						} else if (object instanceof FileSend) {
							FileSend fileSend = (FileSend) object;
							BigInteger E = null;
							BigInteger N = null;
							SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
							systemMsgForNotify.setType(2);
							systemMsgForNotify.setSender(username);
							systemMsgForNotify.setMsg(fileSend.getFrom());
							try {
								oos.writeObject(systemMsgForNotify);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.err.println("Got a certificate");
							// wait
							while (true) {
								Object obj = null;
								try {
									obj = ois.readObject();
									if (obj instanceof Certificate) {
										Certificate senderCertificate = (Certificate) obj;
										E = senderCertificate.getE();
										N = senderCertificate.getN();
										DigitalSignature digitalSignature = new DigitalSignature();
										digitalSignature.setE(E);
										digitalSignature.setN(N);
										Boolean verify = true;
										// digitalSignature.validSignature(fileSend.getMD5(),
										// fileSend.getSignature());
										String mString = "You Got A File!";
										if (verify)
											mString += "(Verified)";
										else
											mString += "(Unverified)";
										clientChatwindow.updateMsg(mString, fileSend.getFrom());
										File file = new File(path + "/" + fileSend.getFilename());
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
									}
								} catch (EOFException e1) {
									continue;
								}
								break;
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}
/**
 * 
 * @param bitSet
 * @return
 */
	private String BitSetToString(List<BitSet> bitSet) {
		String result = "";
		for (BitSet bitSet2 : bitSet) {
			for (int i = 0; i < 64; i++) {
				if (bitSet2.get(i)) {
					result += "1";
				} else {
					result += "0";
				}
			}
		}
		return result;
	}

	/**
	 * send and receive
	 * 
	 * @param username
	 * @param password
	 */
	private boolean clientLog(String username, String password, int type) {
		try {
			System.err.println(username);
			System.err.println(password);

			if (serverCertificate == null)
				serverCertificate = (Certificate) ois.readObject();
			System.err.println("certificate" + serverCertificate.getUsername());

			ClientInfo clientInfo = new ClientInfo();
			clientInfo.setUsername(username);
			serverRsa = new RSA();
			serverRsa.setE(serverCertificate.getE());
			serverRsa.setN(serverCertificate.getN());
			clientInfo.setPassword(serverRsa.encryptMsg((new MD5(password).processMD5())));
			clientInfo.setType(type);

			oos.writeObject(clientInfo);
			oos.flush();
			while (true) {
				Object obj = null;
				try {
					obj = ois.readObject();
				} catch (EOFException e1) {
					continue;
				}
				if (obj instanceof SystemMsgForNotify) {
					SystemMsgForNotify msgForNotify = (SystemMsgForNotify) obj;
					if (msgForNotify.getType() == 1) {
						continue;
					}
					String content = msgForNotify.getMsg();
					if (content.contains("check")) {
						// error alert
						infoPromptwindow.setLabel(content);
						infoPromptwindow.nextMove(clientLogwindow.frmLogPage);
						clientLogwindow.frmLogPage.setVisible(false);
						// notice alert
						Thread.sleep(1000);
						infoPromptwindow.frmWarning.setVisible(true);
						return false;
					} else {
						/**
						 * 登录成功
						 */
						this.username = username;
						this.password = password;
						File file = new File(path + "/" + username);
						file.mkdir();
						// error alert
						infoPromptwindow.setLabel(content);
						// hint alert
						infoPromptwindow.nextMove(clientChatwindow.frmChat);
						clientLogwindow.frmLogPage.setVisible(false);
						clientChatwindow.setUsername(username);
						// start alert
						infoPromptwindow.frmWarning.setVisible(true);
						path += "/" + username;
						return true;
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
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
	 * 客户端向服务器发送相关的凭证
	 */
	private void negotiateCertificate() {
		certificate = new Certificate();
		// generate key
		rsa = new RSA();
		rsa.getRSAKey();
		certificate.setE(rsa.getE());
		certificate.setN(rsa.getN());
		certificate.setUsername(username);
		try {

			oos.writeObject(certificate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatApplication chatApplication = new ChatApplication();
		chatApplication.start();
	}

}
