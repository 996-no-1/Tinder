package ClientGUI;

import java.awt.EventQueue;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Client.AudioPlay;
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
	String clientindex;// current client status
	Socket tcpSocket;// use to connect with server
	String path = "C:/Users/12284/Desktop/system/";

	ClientLogUI clientLogwindow;
	ClientHomeUI clientHomeUI;
	InfoPromptUI infoPromptwindow;
	Map<String, ChatRoomUI> chatRoomUIMap;
	PrivacyInfoUI privacyInfoUI;

	private Envelope envelope = null;
	private String AsMode = "RSA";
	private String sMode = "DES";

	private static InputStream in = null;
	private static ObjectInputStream ois = null;
	private static OutputStream out = null;
	private static ObjectOutputStream oos = null;
	private RSA rsa = null;
	private MD5 md5 = null;
	// 记录用户时候被封禁
	int index = 0;
	String username = null;
	private Certificate certificate = null;
	private SystemMsgForCertificate systemMsgForCertificate = null;
	private Map<String, BigInteger> negotiateDH = new HashMap<>();
	private String wavPath = "C:/Users/12284/Desktop/msn_wav/";

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
			chatRoomUIMap = new HashMap<>();
			clientHomeUI = new ClientHomeUI(this);
			privacyInfoUI = new PrivacyInfoUI(this);
			infoPromptwindow = new InfoPromptUI();

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
	 * receive message from view layer.distribute to different business logic
	 * part
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
								// start to receive message from server
								// constantly
								// receive certificate and notify
								receiveMsgFromServer();
							}
							envelope = null;
						} else if (envelope.getSourceName().equals("ChatRoomUI")) {
							List<Object> objects = envelope.getMsg();
							if (objects.get(0) instanceof List<?>) {
								// send file button
								System.err.println("send file");
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
										// send file
										oos.writeObject(fileSend);
										oos.flush();
										systemMsgForCertificate = null;
										envelope = null;
									} catch (Exception e1) {
										envelope = null;
										continue;
									}
								}
							} else if (objects.size() == 3) {
								Message message = new Message();
								message.setSender(username);
								message.setReceiver((String) objects.get(0));
								String receiver = (String) objects.get(0);
								String msg = (String) objects.get(1);
								if (receiver.contains("group")) {
									// 判断是否为群聊
									message.setMsg(msg);
									try {
										oos.writeObject(message);
										oos.flush();
										systemMsgForCertificate = null;
										envelope = null;
									} catch (Exception e1) {
										envelope = null;
									}
									// 写入本地聊天文件
									File file = new File(path + "/" + message.getReceiver() + ".txt");
									PrintWriter pw;
									try {
										pw = new PrintWriter(new FileWriter(file, true));
										pw.println(message.getSender() + " : " + msg + " "
												+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
										pw.close();
									} catch (IOException e2) {
										e2.printStackTrace();
									}
									
									
									continue;
								}
								DigitalSignature digitalSignature = new DigitalSignature();
								digitalSignature.setD(rsa.getD());
								digitalSignature.setN(rsa.getN());
								List<BigInteger> signs = digitalSignature.getSignature(msg);
								message.setDigitalSignature(signs);
								String key = (String) objects.get(2);
								Certificate otherCertificate = chatRoomUIMap.get((String) objects.get(0)).certificate;
								RSA temp = new RSA();
								temp.setE(otherCertificate.getRSAE());
								temp.setN(otherCertificate.getRSAN());
								message.setMsg(algorithmFactory(otherCertificate.getsMode(), key, msg, 0));
								if (otherCertificate.getAsMode().equals("RSA")) {
									List<BigInteger> keys = temp.encryptMsg(key);
									message.setKey(keys);
								}
								try {
									oos.writeObject(message);
									oos.flush();
									systemMsgForCertificate = null;
								} catch (Exception e1) {
									envelope = null;
								}
								// 写入本地聊天文件
								File file = new File(path + "/" + message.getSender() + ".txt");
								PrintWriter pw;
								try {
									pw = new PrintWriter(new FileWriter(file, true));
									pw.println(message.getSender() + " : " + msg + " "
											+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
									pw.close();
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								
								systemMsgForCertificate = null;
							}

						} else if (envelope.getSourceName().equals("ClientHomeUI")) {
							List<Object> objects = envelope.getMsg();
							if (objects.size() == 1 && objects.get(0) instanceof String) {
								String receiver = (String) objects.get(0);
								SystemMsgForNotify systemMsgForNotify = new SystemMsgForNotify();
								systemMsgForNotify.setType(2);
								systemMsgForNotify.setSender(username);
								systemMsgForNotify.setReceiver(receiver);
								if (!receiver.contains("group")) {
									// ask for certificate
									try {
										oos.writeObject(systemMsgForNotify);
										oos.flush();
									} catch (IOException e) {
										e.printStackTrace();
									}
									while (systemMsgForCertificate == null)
										System.err.print("get a certificate");
									// get the target client's certificate
									Certificate certificate = systemMsgForCertificate.getCertificate();
									chatRoomUIMap.get(receiver).certificate = certificate;
								} else {
									Certificate certificate = new Certificate();
									certificate.setAsMode("RSA");
									certificate.setsMode("Caesar");
									chatRoomUIMap.get(receiver).certificate = certificate;
								}
								
								systemMsgForCertificate = null;
								// 之后进入聊天界面
								// 加载用户聊天记录
								BufferedReader bufferedReader;
								try {
									
									File file=new File(path + "/" + receiver + ".txt");
									if(!file.exists())file.createNewFile();
									bufferedReader = new BufferedReader(
											new FileReader(file));
									String msg = "";
									String cur = "";
									while ((cur = bufferedReader.readLine()) != null) {
										msg += cur + "\n";
									}
									bufferedReader.close();
									chatRoomUIMap.get(receiver).setMsgArea(msg);
								} catch (IOException e) {
									e.printStackTrace();
								}

							}
							envelope = null;
						}
						// else if
						// (envelope.getSourceName().equals("PrivacyInfoUI")) {
						// List<Object> objects = envelope.getMsg();
						// String username = (String) objects.get(0);
						// String password = (String) objects.get(1);
						// String gender = (String) objects.get(2);
						// int age = (int) objects.get(3);
						// String note = (String) objects.get(4);
						// User user = new User(username, gender, age, note,
						// false, new MD5(password).processMD5());
						// try {
						// oos.writeObject(user);
						// oos.flush();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						// }

					}
					// clear envelope box
					envelope = null;
					System.err.print("");
				}
			}

		}).start();

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
							clientHomeUI.updateMsg(msg, systemMsgForNotify.getSender());
						} else if (object instanceof List<?>) {
							// 收到在线列表
							List<String> clientList = (List<String>) object;
							// update client list
							System.out.println("列表 ");
							System.out.println(clientList.size());
							// 接收群组信息
							List<List<String>> groups = (List<List<String>>) ois.readObject();
							// 更新列表
							clientHomeUI.refreshTree(clientList, groups);

						} else if (object instanceof SystemMsgForCertificate) {
							// receive address and certificate
							systemMsgForCertificate = (SystemMsgForCertificate) object;
						} else if (object instanceof FileSend) {
							// 若接受到文件
							System.err.println("Get a file");
							FileSend fileSend = (FileSend) object;
							if (fileSend.getTo().contains("group")) {
								AudioPlay.playAudio(wavPath + "newemail.wav");
								// 若收到的是群聊文件
								String mString = "Got A File!(Verified) ";
								// 判断是否打开了窗口
								if (chatRoomUIMap.containsKey(fileSend.getTo())) {
									// 聊天已经打开
									chatRoomUIMap.get(fileSend.getTo()).refreshMsgArea(mString+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), fileSend.getFrom());
								} else {
									clientHomeUI.refreshMsgList(fileSend.getTo(), 0);
								}
								// 写入本地聊天文件
								File file = new File(path + "/" + fileSend.getTo() + ".txt");
								PrintWriter pw;
								try {
									pw = new PrintWriter(new FileWriter(file, true));
									pw.println(fileSend.getFrom() + " : " + "Got a file" + " "
											+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
									pw.close();
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								continue;
							}
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
							systemMsgForCertificate=(SystemMsgForCertificate)ois.readObject();
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
							verify = digitalSignature.validSignature(fileSend.getMD5(), fileSend.getSignature());
							String mString = "Got A File!";
							if (verify)
								mString += "(Verified)";
							else
								mString += "(Unverified)";
							// 判断是否打开了窗口
							if (chatRoomUIMap.containsKey(fileSend.getFrom())) {
								// 聊天已经打开
								chatRoomUIMap.get(fileSend.getFrom()).refreshMsgArea(mString+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), fileSend.getFrom());
							} else {
								// 消息上浮
								clientHomeUI.refreshMsgList(fileSend.getFrom(), 0);
							}
							// 写入本地聊天文件
							File fileLocal = new File(path + "/" + fileSend.getFrom() + ".txt");
							PrintWriter pw;
							try {
								pw = new PrintWriter(new FileWriter(fileLocal, true));
								pw.println(fileSend.getFrom() + " : " + "Got a file" + " "
										+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
								pw.close();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							AudioPlay.playAudio(wavPath + "newemail.wav");
							File file = new File(path +"/" + fileSend.getFilename());
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
						} else if (object instanceof Message) {
							System.err.println("got a message");
							Message message = (Message) object;
							// 判断是否是由群聊产生
							if (message.getReceiver().contains("group")) {
								// 用户收到群聊消息
								String msg = (String) message.getMsg();
								AudioPlay.playAudio(wavPath + "newalert.wav");
								System.err.println("message from group");
								if (chatRoomUIMap.containsKey(message.getReceiver())) {
									// 若窗口已开启
									chatRoomUIMap.get(message.getReceiver()).refreshMsgArea(msg+ " "+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), message.getSender());
								} else {
									// 消息上浮
									clientHomeUI.refreshMsgList(message.getReceiver(), 0);
								}
								// 写入聊天文件
								File fileLocal = new File(path + "/" + message.getReceiver() + ".txt");
								PrintWriter pw;
								try {
									pw = new PrintWriter(new FileWriter(fileLocal, true));
									pw.println(message.getSender() + " : " + msg + " "
											+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
									pw.close();
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								systemMsgForCertificate = null;
								continue;
							}
							System.err.println(message.getSender());
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
							systemMsgForCertificate=(SystemMsgForCertificate)ois.readObject();
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
							}
							System.err.println(mString);
							Boolean verify = true;
							verify = digitalSignature.validSignature(mString, message.getDigitalSignature());
							if (verify)
								mString += "(Verified)";
							else
								mString += "(Unverified)";
							// 用户收到私聊消息
							AudioPlay.playAudio(wavPath + "newalert.wav");
							if (chatRoomUIMap.containsKey(message.getSender())) {
								// 若窗口已开启
								chatRoomUIMap.get(message.getSender()).refreshMsgArea(mString+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), message.getSender());
							} else {
								// 消息上浮
								clientHomeUI.refreshMsgList(message.getSender(), 0);
							}
							// 写入聊天文件
							File fileLocal = new File(path + "/" + message.getSender() + ".txt");
							PrintWriter pw;
							try {
								pw = new PrintWriter(new FileWriter(fileLocal, true));
								pw.println(message.getSender() + " : " + mString + " "
										+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
								pw.close();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							systemMsgForCertificate = null;
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
						// 表示用户是否被封禁
						index = msgForNotify.getIndex();
						System.err.println("index: "+index);
						clientHomeUI.setUsername(username);
						path+="/"+username;
						File file=new File(path);
						if (!file.exists()) {
							file.mkdir();
						}
						infoPromptwindow.setLabel(content);
						infoPromptwindow.nextMove(clientHomeUI.frmTinderClient);
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
