package ServerImp;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Client.Certificate;
import Client.ClientInfo;
import Client.FileSend;
import Client.Message;
import Client.SystemMsgForCertificate;
import Client.SystemMsgForNotify;
import SecurityAlgorithm.CaesarAlgorithm;
import db.dao.GroupDao;
import db.dao.UserDao;
import db.entity.Group;
import db.entity.User;
import db.util.DBFactory;

/**
 * This class implements Runnable interface to handle many clients requests. And
 * help to transfer massages among clients in secret way.
 * 
 * @author Zhichao Wang 2019/05/24
 * @version 1.0
 */
public class ServerHandler implements Runnable {
	private Map<String, String> clientLog = new HashMap<>();// 存储系统的合法用户
	private Map<String, Socket> clientSockets = new HashMap<>();// this obj is  used to  maintain the  information  between  server and  clients.
	private Map<String, Certificate> certificateList = new HashMap<>();// To  store  the  client 's RSA public Key
	private Map<String, Integer> curClientIndex = new HashMap<>();
	private Integer userIndex;
	private Socket socket;
	private  InputStream in = null;
	private  OutputStream out = null;
	private  OutputStream selfout = null;
	private  ObjectInputStream ois = null;
	private  ObjectOutputStream oos = null;
	private  ObjectOutputStream alloos = null;
	private String curClient = null;
	private String clientAsMode=null;
	private UserDao udao = new UserDao();
	 Map<String, ObjectOutputStream> clientOut=new HashMap<>();
	 Map<String, ObjectInputStream> clientIn=new HashMap<>();
/**
 * initiate server static data
 * @param socket
 * @param clientList
 * @param count
 * @param certificateList
 * @param clientLog2
 * @param clientIndex
 * @param clientIn
 * @param clientOut
 */
	public ServerHandler(Socket socket, Map<String, Socket> clientList, int count,
			Map<String, Certificate> certificateList, Map<String, String> clientLog2,
			Map<String, Integer> clientIndex,Map<String, ObjectInputStream> clientIn, Map<String, ObjectOutputStream> clientOut) {
		this.socket = socket;
		this.userIndex = count;
		this.certificateList = certificateList;
		this.curClientIndex = clientIndex;
		this.clientLog = clientLog2;
		
		this.clientSockets = clientList;
		this.clientIn=clientIn;
		this.clientOut=clientOut;
	}

	@Override
	public void run() {
		try {
			selfout = socket.getOutputStream();
			oos = new ObjectOutputStream(selfout);
			in = socket.getInputStream();
			ois = new ObjectInputStream(in);
			// receive log request from client
			ClientInfo logReq = null;
			while (true) {
				Thread.sleep(100);
				logReq = (ClientInfo) ois.readObject();
						SystemMsgForNotify failInfo = new SystemMsgForNotify();
						String msg = "please check your password!";
						failInfo.setMsg(msg);
						failInfo.setSender("Server");
						oos.writeObject(failInfo);oos.flush();
						
						System.err.println("Wrong password!");
						
						continue;
				}
				
				if (clientLog.containsKey(logReq.getUsrname())) {
					if (clientLog.get(logReq.getUsrname()).equals(logReq.getMD5())&&curClientIndex.get(logReq.getUsrname()) == null) {
						// store client information
						clientSockets.put(logReq.getUsrname(), socket);
						curClientIndex.put(logReq.getUsrname(), 1);
						curClient = logReq.getUsrname();
						SystemMsgForNotify failInfo = new SystemMsgForNotify();
						String msg = new CaesarAlgorithm("10").encryptMsg("Welcome to the chat room!");
						failInfo.setMsg(msg);
						failInfo.setType(0);
						failInfo.setSender("System");
						if(udao.getUser(curClient).isBan()) {
							failInfo.setIndex(1);
						}else {
							failInfo.setIndex(0);
						}
						oos.writeObject(failInfo);
						oos.flush();
						
						
						
						clientIn.put(curClient, ois);
						clientOut.put(curClient, oos);
						break;
					}
				}
				// log fail.Send failed information to client
				SystemMsgForNotify failInfo = new SystemMsgForNotify();
				String msg = new CaesarAlgorithm("10").encryptMsg("Please check your account!");
				failInfo.setMsg(msg);
				failInfo.setSender("System");
				oos.writeObject(failInfo);
				oos.flush();
			}
			
			
			/**
			 * New thread to handle the constant receiving from client.
			 */
			while (true) {
				try {
					try {
						if (socket.isClosed()) {
							offlineNotify();
							return;
						}
						Thread.sleep(100);
						Object object = ois.readObject();
						
						
						if (object instanceof SystemMsgForNotify) {
							
							//handler client's request
							SystemMsgForNotify systemMsgForNotify=(SystemMsgForNotify)object;
							
							//System.err.println("request"+systemMsgForNotify.getSender());
							SystemMsgForCertificate systemMsgForCertificate=new SystemMsgForCertificate();
							Socket to=clientSockets.get(systemMsgForNotify.getReceiver());
							//systemMsgForCertificate.setHost(to.getInetAddress().getHostName());
							//systemMsgForCertificate.setPort(curClientIndex.get(systemMsgForNotify.getReceiver()));
							//System.err.println(systemMsgForNotify.getReceiver()+" 回应  "+systemMsgForCertificate.getPort());
							systemMsgForCertificate.setCertificate(certificateList.get(systemMsgForNotify.getReceiver()));
							
							clientOut.get(systemMsgForNotify.getSender()).writeObject(systemMsgForCertificate);
							clientOut.get(systemMsgForNotify.getSender()).flush();
						}else if(object instanceof User) {
							UserDao udao=new UserDao();
							udao.updateUser((User)object);
						}else if(object instanceof Message) {
							Message revMsg = (Message) object;
							
							if(revMsg.getSender().equals("Admin") && revMsg.getReceiver().equals("Server")) {
								if(revMsg.getMsg().toString().equals("Begin Add Department")) {
									
									//Return all user name list
									UserDao udao = new UserDao();
									List<User> allUserList = udao.getAllUser();
									List<String> allUserNameList = new ArrayList<String>();
									for (User user : allUserList) {
										allUserNameList.add(user.getUsername());
									}
									oos.writeObject(allUserNameList);oos.flush();
									
								}else if(revMsg.getMsg().toString().startsWith("BeginModifyGroup")) {
									
									List<String> userNeedRefresh = new ArrayList<>();
									
									GroupDao gdao = new GroupDao();
									String[] params = revMsg.getMsg().toString().split("\\.\\.");
									
									String groupName = params[1];
									
									List<String> extraGroupMemberNameList,insideGroupMemberNameList;
									
									Message sm = new Message();
									sm.setSender("Server");
									sm.setReceiver("Admin");
									sm.setMsg("GroupModifying");
									
									oos.writeObject(sm);oos.flush();
									
									extraGroupMemberNameList = gdao.getAllUserNotIn(groupName);
									insideGroupMemberNameList = gdao.getAllUserIn(groupName);
									
									oos.writeObject(extraGroupMemberNameList);oos.flush();
									oos.writeObject(insideGroupMemberNameList);oos.flush();
									
									while(true) {
										
										System.err.println("Begin roop");
										
										Message req = (Message) ois.readObject();
										
										if(req.getMsg().toString().startsWith("AddUserToGroup")) {
											String[] paramsList = req.getMsg().toString().split("\\.\\.");
											List<String> addUserNameList = new ArrayList<>();
											
											for (int i = 1;i < paramsList.length;i++) {
												addUserNameList.add(paramsList[i]);
												if(userNeedRefresh.indexOf(paramsList[i]) == -1) {
													userNeedRefresh.add(paramsList[i]);
												}
											}
											
											gdao.addUserToGroup(groupName, addUserNameList);
											
											Message addResMsg = new Message();
											addResMsg.setSender("Server");
											addResMsg.setReceiver("Admin");
											addResMsg.setMsg("Add User into Group Success!");
											
											oos.writeObject(addResMsg);oos.flush();
											
											extraGroupMemberNameList = gdao.getAllUserNotIn(groupName);
											insideGroupMemberNameList = gdao.getAllUserIn(groupName);
											
											oos.writeObject(extraGroupMemberNameList);oos.flush();
											oos.writeObject(insideGroupMemberNameList);oos.flush();
											
										}else if(req.getMsg().toString().startsWith("DiscardUserFromGroup")) {
											String[] paramsList = req.getMsg().toString().split("\\.\\.");
											List<String> discardUserNameList = new ArrayList<>();
											
											for (int i = 1;i < paramsList.length;i++) {
												discardUserNameList.add(paramsList[i]);
												if(userNeedRefresh.indexOf(paramsList[i]) == -1) {
													userNeedRefresh.add(paramsList[i]);
												}
											}
											
											for (String string : discardUserNameList) {
												gdao.deleteUserFromGroup(groupName, string);
											}
											
											Message addResMsg = new Message();
											addResMsg.setSender("Server");
											addResMsg.setReceiver("Admin");
											addResMsg.setMsg("Discard User from Group Success!");
											
											oos.writeObject(addResMsg);oos.flush();
											
											extraGroupMemberNameList = gdao.getAllUserNotIn(groupName);
											insideGroupMemberNameList = gdao.getAllUserIn(groupName);
											
											oos.writeObject(extraGroupMemberNameList);oos.flush();
											oos.writeObject(insideGroupMemberNameList);oos.flush();
										}else {
											
											List<Group> groupList = gdao.getAllGroup();
											List<List<String>> groupMemberList = new ArrayList<List<String>>();
											
											UserDao udao = new UserDao();
											List<String> notInGroupMembers = udao.getNotInGroupUserList();
											notInGroupMembers.add(0, "Default");
											groupMemberList.add(notInGroupMembers);
											
											for (Group group : groupList) {
												List<String> tmp = new ArrayList<String>();
												tmp.add(group.getGroupname());
												for (String tmpUsername : group.getMemberUsernameList()) {
													tmp.add(tmpUsername);
												}
												groupMemberList.add(tmp);
											}
											
											for (String string : userNeedRefresh) {
												ObjectOutputStream TMPOOS = clientOut.get(string);
												if(TMPOOS != null) {
													TMPOOS.writeObject(groupMemberList);TMPOOS.flush();
												}
											}
											break;
										}
									}
								}else if(revMsg.getMsg().toString().startsWith("GroupAdd")) {
									String[] params = revMsg.getMsg().toString().split("\\.\\.");
									if(params.length > 1) {
										
										//Add new group
										String groupName = params[1];
										ArrayList<String> groupUserList = new ArrayList<String>();
										for(int i = 2;i < params.length;i++) {
											groupUserList.add(params[i]);
										}
										
										GroupDao gdao = new GroupDao();
										Boolean suc = gdao.createGroup(groupName);
										gdao.addUserToGroup(groupName, groupUserList);
										
										//Instructions of success or fail for adding new group
										if(suc) {
											Message msg = new Message();
											msg.setReceiver("Admin");
											msg.setSender("Server");
											msg.setMsg("New Group Add Success");
											oos.writeObject(msg);oos.flush();
											
											List<Group> groupList = gdao.getAllGroup();
											List<String> groupNameList = new ArrayList<String>();
											
											for (Group group : groupList) {
												groupNameList.add(group.getGroupname());
											}
											
											oos.writeObject(groupNameList);oos.flush();
										}else {
											
										}
									}
								}
							}else {
								String[] pl = revMsg.getReceiver().split("\\[");
								if(pl.length < 2) {
									
									System.err.println("Sender: " + revMsg.getSender());
									System.err.println("Receiver: " + revMsg.getReceiver());
									
									String receive = revMsg.getReceiver();
									clientOut.get(receive).writeObject(revMsg);clientOut.get(receive).flush();
								}else if(pl[1].equals("group]")){
									GroupChatFunc gcf = new GroupChatFunc(clientOut);
									gcf.processGroupChat(revMsg);
									
									System.err.println("Group Msg Send");
									
								}else {
									
								}
							}
						}else if(object instanceof FileSend) {
							FileSend sfs = (FileSend) object;
							if(sfs.getTo().split("\\[").length > 1 && sfs.getTo().split("\\[")[1].equals("group]")) {
								GroupChatFunc gcf = new GroupChatFunc(clientOut);
								gcf.processGroupChat(sfs,1);
							}else {
								String receive = sfs.getTo();
								clientOut.get(receive).writeObject(sfs);clientOut.get(receive).flush();
							}
						}else if(object instanceof ClientInfo) {
							ClientInfo tmp = (ClientInfo) object;
							UserDao udao = new UserDao();
							User tmpU = new User();
							tmpU.setUsername(tmp.getUsername());
							tmpU.setAge(tmp.getAge());
							tmpU.setGender(tmp.getGender());
							tmpU.setNote(tmp.getNote());
							tmpU.setHashedPassword(tmp.getMD5());
							udao.updateUser(tmpU);
							
							List<ClientInfo> clientInfoList = new ArrayList<>();
							List<User> ul = udao.getAllUser();
							
							for (User user : ul) {
								ClientInfo tt = new ClientInfo();
								tt.setUsername(user.getUsername());
								tt.setNote(user.getNote());
								tt.setGender(user.getGender());
								tt.setAge(user.getAge());
								clientInfoList.add(tt);
							}
							
							for(Iterator<String> ite = clientOut.keySet().iterator();ite.hasNext();) {

								String nn = ite.next();
								ObjectOutputStream TMPOOS = clientOut.get(nn);
								TMPOOS.writeObject(clientInfoList);TMPOOS.flush();							}
						}
						
					} catch (EOFException e) {
						e.printStackTrace();
						offlineNotify();
						return;
					}
				}catch (SocketException e) {
					e.printStackTrace();
					ois.close();
					oos.close();
					socket.close();
					return;
				} 
				catch (IOException e) {
					e.printStackTrace();
					try {
						ois.close();
						oos.close();
						socket.close();
						break;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void offlineNotify() {
		System.err.println(curClient + " exit");
		curClientIndex.remove(curClient);
		clientSockets.remove(curClient);
		clientOut.remove(curClient);
		clientIn.remove(curClient);
		SystemMsgForNotify broadcast = new SystemMsgForNotify();
		broadcast.setReceiver("everyone");
		broadcast.setSender("server");
		broadcast.setType(1);
		broadcast.setMsg(new CaesarAlgorithm("10").encryptMsg("Client " + curClient + " is offline."));
		try {
			List<String> list=new ArrayList<>();
			for (String string : curClientIndex.keySet()) {
				list.add(string);
			}
			for (ObjectOutputStream boradcastout : clientOut.values()) {
				boradcastout.writeObject(list);
				boradcastout.flush();
				boradcastout.writeObject(broadcast);
				boradcastout.flush();
				boradcastout=null;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}
