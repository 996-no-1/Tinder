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
import SecurityAlgorithm.MD5;
import ServerTheardPool.ServerTheardPool;
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
	private Map<String, String> clientLog = new HashMap<>();				// Store the user name and hashed password of all clients
	private Map<String, Socket> clientSockets = new HashMap<>();			// this object is  used to  maintain the  information  between  server and  clients.
	private Map<String, Certificate> certificateList = new HashMap<>();		// To  store  the  client 's RSA public Key
	private Map<String, Integer> curClientIndex = new HashMap<>();			//Store current online user
	private Integer userIndex;
	private Socket socket;
	private  InputStream in = null;
	private  OutputStream out = null;
	private  OutputStream selfout = null;
	private  ObjectInputStream ois = null;
	private  ObjectOutputStream oos = null;
	private  ObjectOutputStream alloos = null;
	private String curClient = null;										//The user that this thread connected
	private String clientAsMode=null;
	private UserDao udao = new UserDao();
	private Integer isAdmin = 0;
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
			
			//Handle user login or administrator login
			while (true) {
				Thread.sleep(100);
				logReq = (ClientInfo) ois.readObject();
				
				//Reload client user name and hashed password information into parameter
				reloadClientLog();
				
				
				//Handle administrator login
				if(logReq.getUsername().equals("Admin")) {
					
					//If user name and password is correct
					if(clientLog.get(logReq.getUsername()).equals(logReq.getMD5())) {
						
						
						//Transfer system message to notify
						SystemMsgForNotify failInfo = new SystemMsgForNotify();
						String msg = "Welcome to the chat room!";
						failInfo.setMsg(msg);
						failInfo.setSender("Server");
						oos.writeObject(failInfo);oos.flush();
						
						System.err.println("Password verified!");
						
						
						//Transfer name list of all groups
						List<String> groupNameList = new ArrayList<String>();
						
						GroupDao gdao = new GroupDao();
						List<Group> groupList = gdao.getAllGroup();
						for (Group group : groupList) {
							groupNameList.add(group.getGroupname());
							System.err.println("Group Test: " + group.getGroupname());
						}
						
						oos.writeObject(groupNameList);oos.flush();
						
						System.err.println("Transfer group name list");
						
						
						//Transfer name list of all users
						List<String> userNameList = new ArrayList<String>();
						
						for(Iterator<String> ite = clientLog.keySet().iterator();ite.hasNext();) {
							String tmp = ite.next();
							if(tmp != "Admin") userNameList.add(tmp);
						}
						
						oos.writeObject(userNameList);oos.flush();
						
						System.err.println("Transfer user name list");
						
						
						//Transfer name list of all online list
						List<String> onlineUserNameList = new ArrayList<String>();
						for (String string : curClientIndex.keySet()) {
							onlineUserNameList.add(string);
						}
						
						oos.writeObject(onlineUserNameList);oos.flush();
						
						System.err.println("Transfer online user name list");
						
						ServerTheardPool.setAdminState(1);isAdmin = 1;
						ServerTheardPool.setAdminOut(oos);
						
						break;
					}
					
					//If password is wrong
					else {
						SystemMsgForNotify failInfo = new SystemMsgForNotify();
						String msg = "please check your password!";
						failInfo.setMsg(msg);
						failInfo.setSender("Server");
						oos.writeObject(failInfo);oos.flush();
						
						System.err.println("Wrong password!");
						
						continue;
					}
				}
				
				//Handle user login
				if (clientLog.containsKey(logReq.getUsrname())) {
					
					//If user name and password is correct and the account it login is not online
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
			
			//For client which is login success to negotiate certificate and get members information
			if(!logReq.getUsername().equals("Admin")) {
				//receive client's certificate
				Certificate certificate = (Certificate) ois.readObject();
				
				System.err.println("certificateE: " + certificate.getRSAE() + " certificateN: " + certificate.getRSAN());
				
				
				certificateList.put(curClient, certificate);
				clientAsMode=certificate.getAsMode();
				// Broadcast client list and send online notice to every client
				SystemMsgForNotify broadcast = new SystemMsgForNotify();
				broadcast.setReceiver("everyone");
				broadcast.setSender("server");
				broadcast.setType(1);
				broadcast.setMsg(new CaesarAlgorithm("10").encryptMsg("Client " + curClient + " is online!"));
				List<String> list=new ArrayList<>();
				for (String string : curClientIndex.keySet()) {
					list.add(string);
				}
				for (ObjectOutputStream boradcastout : clientOut.values()) {
					
					//Transfer online clients list
					boradcastout.writeObject(list);
					boradcastout.flush();
					
					//Transfer all group and their member list
					GroupDao gdao = new GroupDao();
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
					boradcastout.writeObject(groupMemberList);
					boradcastout.flush();
					
					//Transfer user information
					List<ClientInfo> clientInfoList = new ArrayList<>();
					List<User> ul = udao.getAllUser();
					
					for (User user : ul) {
						ClientInfo tmp = new ClientInfo();
						tmp.setUsername(user.getUsername());
						tmp.setNote(user.getNote());
						tmp.setGender(user.getGender());
						tmp.setAge(user.getAge());
						clientInfoList.add(tmp);
					}
					boradcastout.writeObject(clientInfoList);
					boradcastout.flush();
					
					//Transfer clientInfo list
					
					boradcastout.writeObject(broadcast);
					boradcastout.flush();
					boradcastout=null;
				}
				
				//Refresh administrator's GUI list if administrator is online
				if(ServerTheardPool.getAdminState() == 1) {
					
					SystemMsgForNotify smfn = new SystemMsgForNotify();
					smfn.setSender("System");
					smfn.setReceiver("Admin");
					smfn.setMsg("Client Login! " + curClient);
					
					ServerTheardPool.getAdminOut().writeObject(smfn);ServerTheardPool.getAdminOut().flush();
				}
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
						
						
						//If object from client is 'SystemMsgForNotify' type
						if (object instanceof SystemMsgForNotify) {
							
							//handler client's request
							SystemMsgForNotify systemMsgForNotify=(SystemMsgForNotify)object;
							
							//System.err.println("request"+systemMsgForNotify.getSender());
							SystemMsgForCertificate systemMsgForCertificate=new SystemMsgForCertificate();
							Socket to=clientSockets.get(systemMsgForNotify.getReceiver());
//							systemMsgForCertificate.setHost(to.getInetAddress().getHostName());
//							systemMsgForCertificate.setPort(curClientIndex.get(systemMsgForNotify.getReceiver()));
							//System.err.println(systemMsgForNotify.getReceiver()+" ªÿ”¶  "+systemMsgForCertificate.getPort());
							systemMsgForCertificate.setCertificate(certificateList.get(systemMsgForNotify.getReceiver()));
							
							clientOut.get(systemMsgForNotify.getSender()).writeObject(systemMsgForCertificate);
							clientOut.get(systemMsgForNotify.getSender()).flush();
						}
						
						//If object from client is 'User' type
						else if(object instanceof User) {
							UserDao udao=new UserDao();
							udao.updateUser((User)object);
						}
						
						//If object from client is 'Message' type
						else if(object instanceof Message) {
							Message revMsg = (Message) object;
							
							//Handle message from administrator
							if(revMsg.getSender().equals("Admin") && revMsg.getReceiver().equals("Server")) {
								
								//Handle adding department part one
								if(revMsg.getMsg().toString().equals("Begin Add Department")) {
									
									//Return all user name list
									UserDao udao = new UserDao();
									List<User> allUserList = udao.getAllUser();
									List<String> allUserNameList = new ArrayList<String>();
									for (User user : allUserList) {
										allUserNameList.add(user.getUsername());
									}
									oos.writeObject(allUserNameList);oos.flush();
									
								}
								
								//Handle logout
								else if(revMsg.getMsg().toString().equals("LOGOUTOUTOUT")) {
									ServerTheardPool.setAdminState(0);
									Message msg=new Message();
									msg.setSender("Server");
									msg.setReceiver("Admin");
									msg.setMsg("Logout Success");
									oos.writeObject(msg);oos.flush();
									
									System.err.println("State: " + ServerTheardPool.getAdminState());
								}
								
								//Handle adding department part two
								else if(revMsg.getMsg().toString().startsWith("GroupAdd")) {
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
										
										//Instructions of success or fail for adding new group
										if(suc) {
											
											gdao.addUserToGroup(groupName, groupUserList);
											
											//Transfer success message
											Message msg = new Message();
											msg.setReceiver("Admin");
											msg.setSender("Server");
											msg.setMsg("New Group Add Success");
											oos.writeObject(msg);oos.flush();
											
											//Transfer name list of all groups
											List<Group> groupList = gdao.getAllGroup();
											List<String> groupNameList = new ArrayList<String>();
											
											for (Group group : groupList) {
												groupNameList.add(group.getGroupname());
											}
											
											oos.writeObject(groupNameList);oos.flush();
											
											//Transfer name list of all online users
											List<String> onlineUserNameList = new ArrayList<String>();
											for (String string : curClientIndex.keySet()) {
												onlineUserNameList.add(string);
											}
											
											oos.writeObject(onlineUserNameList);oos.flush();
											
											System.err.println("Transfer online user name list");
										}else {
											
											//Transfer fail message
											Message msg = new Message();
											msg.setReceiver("Admin");
											msg.setSender("Server");
											msg.setMsg("Group Name existed already!");
											oos.writeObject(msg);oos.flush();
										}
										
										//Refresh group structure UI of all online users
										sendOnlineClientListToAllOnlineUser();
										sendGroupInfoListToAllOnlineUser();
										sendClientInfoListToAllOnlineUser();
									}
								}
								
								//Handle modifying group
								else if(revMsg.getMsg().toString().startsWith("BeginModifyGroup")) {
									
									List<String> userNeedRefresh = new ArrayList<>();
									
									GroupDao gdao = new GroupDao();
									String[] params = revMsg.getMsg().toString().split("\\.\\.");
									
									String groupName = params[1];
									
									List<String> extraGroupMemberNameList,insideGroupMemberNameList;
									
									//Transfer begin message
									Message sm = new Message();
									sm.setSender("Server");
									sm.setReceiver("Admin");
									sm.setMsg("GroupModifying");
									
									oos.writeObject(sm);oos.flush();
									
									//Refresh group structure UI of administrator's modify window
									extraGroupMemberNameList = gdao.getAllUserNotIn(groupName);
									insideGroupMemberNameList = gdao.getAllUserIn(groupName);
									
									oos.writeObject(extraGroupMemberNameList);oos.flush();
									oos.writeObject(insideGroupMemberNameList);oos.flush();
									
									//Handle instructions in administrator's modify window
									while(true) {
										
										System.err.println("Begin roop");
										
										Message req = (Message) ois.readObject();
										
										//Handle add user into group instruction
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
											
										}
										
										//Handle discard user from group instruction
										else if(req.getMsg().toString().startsWith("DiscardUserFromGroup")) {
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
										}
										
										//Handle return button
										else {
											
											//Refresh group structure of all online users
											sendOnlineClientListToAllOnlineUser();
											sendGroupInfoListToAllOnlineUser();
											sendClientInfoListToAllOnlineUser();
											
											break;
										}
									}
								}
								
								//Handle blocking account
								else if(revMsg.getMsg().toString().startsWith("Block_Account")) {
									String[] splits=revMsg.getMsg().toString().trim().split("\\s");
									UserDao ud=new UserDao();
									if(splits.length>1) {
										for(int i=1;i<splits.length;i++) {
											ud.setBanUser(splits[i],"lock");
										}
									}
									List<User> allUserList = udao.getAllUser();
									Message msg=new Message();
									msg.setSender("Server");
									msg.setReceiver("Admin");
									StringBuffer emsg = new StringBuffer();
									emsg.append("BlockAccountComplete ");
									for (User user : allUserList) {
										emsg.append(user.getUsername());
										emsg.append(" ");
									}
									msg.setMsg(emsg);
									oos.writeObject(msg);oos.flush();
									
									List<String> onlineUserNameList = new ArrayList<String>();
									for (String string : curClientIndex.keySet()) {
										onlineUserNameList.add(string);
									}
									
									oos.writeObject(onlineUserNameList);oos.flush();
									
									System.err.println("Transfer online user name list");
								}
								
								//Handle unlock account
								else if(revMsg.getMsg().toString().startsWith("Unlock_Account")) {
									String[] splits=revMsg.getMsg().toString().trim().split("\\s");
									if(splits.length>1) {
										UserDao ud=new UserDao();
										for(int i=1;i<splits.length;i++) {
											ud.setBanUser(splits[i],"Unlock");
										}
									}
									List<User> allUserList = udao.getAllUser();
									Message msg=new Message();
									msg.setSender("Server");
									msg.setReceiver("Admin");
									StringBuffer emsg = new StringBuffer();
									emsg.append("UnlockAccountComplete ");
									for (User user : allUserList) {
										emsg.append(user.getUsername());
										emsg.append(" ");
									}
									msg.setMsg(emsg);
									oos.writeObject(msg);oos.flush();
									
									List<String> onlineUserNameList = new ArrayList<String>();
									for (String string : curClientIndex.keySet()) {
										onlineUserNameList.add(string);
									}
									
									oos.writeObject(onlineUserNameList);oos.flush();
									
									System.err.println("Transfer online user name list");
								}
								
								//Handle delete account
								else if(revMsg.getMsg().toString().startsWith("Delete_Account")) {
									String[] splits=revMsg.getMsg().toString().trim().split("\\s");
									if(splits.length>1) {
										UserDao ud=new UserDao();
										for(int i=1;i<splits.length;i++) {
											ud.deleteUser(splits[i]);
										}
									}
									List<User> allUserList = udao.getAllUser();
									Message msg=new Message();
									msg.setSender("Server");
									msg.setReceiver("Admin");
									StringBuffer emsg = new StringBuffer();
									emsg.append("DeleteAccountComplete ");
									for (User user : allUserList) {
										emsg.append(user.getUsername());
										emsg.append(" ");
									}
									msg.setMsg(emsg);
									oos.writeObject(msg);oos.flush();
									
									List<String> onlineUserNameList = new ArrayList<String>();
									for (String string : curClientIndex.keySet()) {
										onlineUserNameList.add(string);
									}
									
									oos.writeObject(onlineUserNameList);oos.flush();
									
									System.err.println("Transfer online user name list");
									
									sendOnlineClientListToAllOnlineUser();
									sendGroupInfoListToAllOnlineUser();
									sendClientInfoListToAllOnlineUser();
									
									reloadClientLog();
								}
								
								//Handle reset password
								else if(revMsg.getMsg().toString().startsWith("ResetPasswd")) {
									String[] splits=revMsg.getMsg().toString().trim().split("\\s");
									if(splits.length==3) {
										UserDao ud=new UserDao();
										String username=splits[1];
										String passwd=splits[2];
										User user=ud.getUser(username);
										user.setHashedPassword(new MD5(passwd).processMD5());
										ud.updateUser(user);
									}
									else {
										Exception not2=new Exception("submit msg goes wrong");
										not2.printStackTrace();
									}
									List<User> allUserList = udao.getAllUser();
									StringBuffer msgSend=new StringBuffer();
									Message msg=new Message();
									msg.setSender("Server");
									msg.setReceiver("Admin");
									msgSend.append("ResetPasswdSuccessfully");
									msgSend.append(" ");
									for (User user : allUserList) {
										msgSend.append(user.getUsername());
										msgSend.append(" ");
									}
									msg.setMsg(msgSend);
									oos.writeObject(msg);oos.flush();
									
									List<String> onlineUserNameList = new ArrayList<String>();
									for (String string : curClientIndex.keySet()) {
										onlineUserNameList.add(string);
									}
									
									oos.writeObject(onlineUserNameList);oos.flush();
									
									System.err.println("Transfer online user name list");
									
									reloadClientLog();
								}
								
								//Handle add account
								else if(revMsg.getMsg().toString().startsWith("ADDACCOUNT")){
									String[] str = revMsg.getMsg().toString().split("\\:");
									String[] params = str[1].split("\\|");
									
									String username = params[0];
									String age = params[1];
									String gender = params[2];
									String password = params[3];
									
									System.err.println("Age: " + age);
									
									UserDao ud = new UserDao();
									User newUser = new User();
									newUser.setUsername(username);
									newUser.setAge(Integer.parseInt(age));
									newUser.setGender(gender);
									Boolean suc = ud.addUser(newUser, password);
									
									if(suc) {
										Message msg = new Message();
										msg.setReceiver("Admin");
										msg.setSender("Server");
										msg.setMsg("Add User Account Success");
										oos.writeObject(msg);
										oos.flush();
									}else {
										Message msg = new Message();
										msg.setReceiver("Admin");
										msg.setSender("Server");
										msg.setMsg("User Name has been used!");
										oos.writeObject(msg);
										oos.flush();
										continue;
									}
									
									UserDao udao = new UserDao();
									List<User> allUser = udao.getAllUser();
									List<String> allUserName = new ArrayList<>();
									
									for (User user : allUser) {
										allUserName.add(user.getUsername());
									}
									oos.writeObject(allUserName);oos.flush();
									
									sendOnlineClientListToAllOnlineUser();
									sendGroupInfoListToAllOnlineUser();
									sendClientInfoListToAllOnlineUser();
									
									reloadClientLog();
								} 
								
								//Handle delete department
								else if(revMsg.getMsg().toString().startsWith("Begin Delete Department")){
									String[] str = revMsg.getMsg().toString().split(":");
									String departmentNames = str[1];
									GroupDao gd = new GroupDao();
									Boolean suc = gd.deleteGroup(departmentNames);

									if(suc) {
										Message msg = new Message();
										msg.setReceiver("Admin");
										msg.setSender("Server");
										msg.setMsg("Delete Department Success");
										oos.writeObject(msg);
										oos.flush();
									}else {
										
									}
									
									GroupDao gdao = new GroupDao();
									List<Group> groupList = gdao.getAllGroup();
									List<String> groupNameList = new ArrayList<String>();
									
									for (Group group : groupList) {
										groupNameList.add(group.getGroupname());
									}
									
									oos.writeObject(groupNameList);oos.flush();
									
									sendOnlineClientListToAllOnlineUser();
									sendGroupInfoListToAllOnlineUser();
									sendClientInfoListToAllOnlineUser();
								}
							}
							
							//Handle message from client
							else {
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
						}
						
						//If object from client is 'FileSend' type
						else if(object instanceof FileSend) {
							FileSend sfs = (FileSend) object;
							if(sfs.getTo().split("\\[").length > 1 && sfs.getTo().split("\\[")[1].equals("group]")) {
								GroupChatFunc gcf = new GroupChatFunc(clientOut);
								gcf.processGroupChat(sfs,1);
							}else {
								String receive = sfs.getTo();
								clientOut.get(receive).writeObject(sfs);clientOut.get(receive).flush();
							}
						}
						
						//If object from client is 'ClientInfo' type
						else if(object instanceof ClientInfo) {
							ClientInfo tmp = (ClientInfo) object;
							UserDao udao = new UserDao();
							User tmpU = new User();
							tmpU.setUsername(tmp.getUsername());
							tmpU.setAge(tmp.getAge());
							tmpU.setGender(tmp.getGender());
							tmpU.setNote(tmp.getNote());
							tmpU.setHashedPassword(tmp.getMD5());
							udao.updateUser(tmpU);
							
							sendClientInfoListToAllOnlineUser();
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
					offlineNotify();
					System.err.println("clinet logout");
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

	
	/**
	 * Handle logout
	 */
	public void offlineNotify() {
		
		if(isAdmin == 1) {
			
			ServerTheardPool.setAdminState(0);
			return;
		}
		
		if(ServerTheardPool.getAdminState() == 1) {
			
			System.err.println("One client off and admin get msg");
			
			SystemMsgForNotify smfn = new SystemMsgForNotify();
			smfn.setSender("System");
			smfn.setReceiver("Admin");
			smfn.setMsg("Client Logout! " + curClient);
			
			ServerTheardPool.getAdminOut().writeObject(smfn);
			ServerTheardPool.getAdminOut().flush();
		}
		
		System.err.println(curClient + " exit");
		curClientIndex.remove(curClient);
		clientSockets.remove(curClient);
		certificateList.remove(curClient);
		clientOut.remove(curClient);
		clientIn.remove(curClient);
		SystemMsgForNotify broadcast = new SystemMsgForNotify();
		broadcast.setReceiver("everyone");
		broadcast.setSender("server");
		broadcast.setType(1);
		broadcast.setMsg(new CaesarAlgorithm("10").encryptMsg("Client " + curClient + " is offline."));
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
		

	}
	
	/**
	 * Send client information list of all users to all online users
	 * @throws IOException
	 */
	private void sendClientInfoListToAllOnlineUser() throws IOException {
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
			ObjectOutputStream TMPOOS = clientOut.get(ite.next());
			TMPOOS.writeObject(clientInfoList);TMPOOS.flush();
		}
	}
	
	/**
	 * Send all group structure to all online users
	 * @throws IOException
	 */
	private void sendGroupInfoListToAllOnlineUser() throws IOException {
		for (ObjectOutputStream tmpoos : clientOut.values()) {
			GroupDao gdao = new GroupDao();
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
			tmpoos.writeObject(groupMemberList);tmpoos.flush();
		}
	}
	
	

	
	/**
	 * Reload client login information
	 */
	private void reloadClientLog() {
		
		Map<String,String> newCl = new HashMap<String, String>();
		
		UserDao udao = new UserDao();
		List<User> allUsers = udao.getAllUser();
		
		for (User user : allUsers) {
			newCl.put(user.getUsername(), user.getHashedPassword());
		}
		
		newCl.put("Admin", "123");
		ServerTheardPool.setClientLog(newCl);
		clientLog = newCl;
	}
	/**
	 * Send all online user list to all online users
	 * @throws IOException
	 */
	private void sendOnlineClientListToAllOnlineUser() throws IOException {
		
		List<String> list=new ArrayList<>();
		for (String string : curClientIndex.keySet()) {
			list.add(string);
		}
		
		for (ObjectOutputStream tmpoos : clientOut.values()) {
			tmpoos.writeObject(list);tmpoos.flush();
		}
	}

}
