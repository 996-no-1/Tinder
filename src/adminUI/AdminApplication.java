package adminUI;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Client.ClientInfo;
import Client.Message;
import Client.SystemMsgForNotify;
import ClientGUI.Envelope;

public class AdminApplication {
	
	static final int PORT = 2021;
	static final String HOST= "127.0.0.1";
	Socket adminSocket;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	UIAddAccount addAccountWindow;
	UIAddDepartment addDepartmentWindow;
	UIAdminHome adminHomeWindow;
	UIAdminLog adminLogWindow;
	UIModifyDepartment modifyDepartmentWindow;
	UIResetPwd resetPwdWindow;
	UIInfoPrompt infoPrompt;
	
	private Envelope envelope = null;
	
	private int revMsgFromServerState = 0;
	
	public AdminApplication() {
		
		try {
			adminSocket = new Socket(HOST, PORT);
			
			addAccountWindow = new UIAddAccount(this);
			addDepartmentWindow = new UIAddDepartment(this);
			adminHomeWindow = new UIAdminHome(this);
			adminLogWindow = new UIAdminLog(this);
			modifyDepartmentWindow = new UIModifyDepartment(this);
			resetPwdWindow = new UIResetPwd(this);
			infoPrompt = new UIInfoPrompt();
			
			oos = new ObjectOutputStream(adminSocket.getOutputStream());
			ois = new ObjectInputStream(adminSocket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	
	public void start() {
		receiveMsgFromView();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				adminLogWindow.frmAdminlog.setVisible(true);
			}
		});
	}
	
	private void receiveMsgFromView() {
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					if(envelope != null) {
						if(envelope.getSourceName().equals("UIAddAccount")) {
							
							Message mes = new Message();
							mes.setSender("Admin");
							mes.setReceiver("Server");
							String username = (String) envelope.getMsg().get(0);
							String age = (String) envelope.getMsg().get(1);
							String gender = (String) envelope.getMsg().get(2);
							String pswd =  (String) envelope.getMsg().get(3);
							
							mes.setMsg("ADDACCOUNT" + ":" + username + "|" + age + "|" + gender + "|" + pswd);
							
							try {
								oos.writeObject(mes);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							
							
						}else if(envelope.getSourceName().equals("LOGOUTOUTOUT")) {
							Message mes = new Message();
							mes.setSender("Admin");
							mes.setReceiver("Server");
							mes.setMsg("LOGOUTOUTOUT");
							try {
								oos.writeObject(mes);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
						else if(envelope.getSourceName().equals("UIAddDepartment")) {
							Message sm = new Message();
							sm.setSender("Admin");
							sm.setReceiver("Server");
							StringBuffer a = new StringBuffer("GroupAdd");
							for (Object ite : envelope.getMsg()) {
								String tmp = (String) ite;
								a.append(".." + tmp);
							}
							sm.setMsg(a.toString());
							
							try {
								oos.writeObject(sm);
								oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}else if(envelope.getSourceName().equals("UIAdminHome")) {
							if(envelope.getMsg().get(0).equals("Begin Add Department")) {
								
								Message sm = new Message();
								sm.setSender("Admin");
								sm.setReceiver("Server");
								sm.setMsg("Begin Add Department");
								
								try {
									oos.writeObject(sm);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}else if(envelope.getMsg().get(0).equals("ModifyGroup")) {
								
								Message sm = new Message();
								sm.setSender("Admin");
								sm.setReceiver("Server");
								sm.setMsg("BeginModifyGroup.." + envelope.getMsg().get(1));
								
								
								
								try {
									oos.writeObject(sm);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else if(envelope.getMsg().get(0).equals("Block_Account")||
									envelope.getMsg().get(0).equals("Delete_Account")||
									envelope.getMsg().get(0).equals("Unlock_Account")) {
								Message sm=new Message();
								sm.setSender("Admin");
								sm.setReceiver("Server");
								StringBuffer sb=new StringBuffer();
								for(Object a:envelope.getMsg()) {
									sb.append((String)a);
									sb.append(" ");
								}
								sm.setMsg(sb.toString());
								try {
									oos.writeObject(sm);
									oos.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							else if(envelope.getMsg().get(0).equals("ToResetPwdPage")) {
								adminHomeWindow.frmTinderAdmin.setVisible(false);
								resetPwdWindow.frmTinderReset.setVisible(true);
								resetPwdWindow.username=(String)envelope.getMsg().get(1);
							}else if(envelope.getMsg().get(0).equals("Begin Delete Department")){
								Message sm = new Message();
								sm.setSender("Admin");
								sm.setReceiver("Server");
								String departmentNames= (String) envelope.getMsg().get(1);
								sm.setMsg("Begin Delete Department" + ":" + departmentNames);
								
								try {
									oos.writeObject(sm);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}else if(envelope.getSourceName().equals("UIAdminLog")) {
							ClientInfo alog = new ClientInfo();
							alog.setUsername("Admin");
							alog.setMD5((String) envelope.getMsg().get(0));
							
							if(revMsgFromServerState == 0) receiveMsgFromServer();
							
							try {
								oos.writeObject(alog);oos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}else if(envelope.getSourceName().equals("UIModifyDepartment")) {
							if(envelope.getMsg().get(0).toString().equals("Add")) {
								List<Object> msgFromEnvelope = envelope.getMsg();
								Message smsg = new Message();
								smsg.setSender("Admin");
								smsg.setReceiver("Server");
								String msg = "AddUserToGroup";
								
								for (int i = 1;i < msgFromEnvelope.size();i++) {
									msg += ".." + msgFromEnvelope.get(i);
								}
								
								smsg.setMsg(msg);
								
								try {
									oos.writeObject(smsg);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else if(envelope.getMsg().get(0).toString().equals("Discard")) {
								List<Object> msgFromEnvelope = envelope.getMsg();
								Message smsg = new Message();
								smsg.setSender("Admin");
								smsg.setReceiver("Server");
								String msg = "DiscardUserFromGroup";
								
								for (int i = 1;i < msgFromEnvelope.size();i++) {
									msg += ".." + msgFromEnvelope.get(i);
								}
								
								smsg.setMsg(msg);
								
								try {
									oos.writeObject(smsg);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else if(envelope.getMsg().get(0).toString().equals("Submit")) {
								Message smsg = new Message();
								smsg.setSender("Admin");
								smsg.setReceiver("Server");
								smsg.setMsg("Submit");
								
								try {
									oos.writeObject(smsg);
									oos.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								modifyDepartmentWindow.frmTinderModify.setVisible(false);
								adminHomeWindow.frmTinderAdmin.setVisible(true);
							}
						}else if(envelope.getSourceName().equals("UIResetPwd")) {
							if(envelope.getMsg().get(0).equals("ResetPasswd")) {
								Message sm = new Message();
								sm.setSender("Admin");
								sm.setReceiver("Server");
								StringBuffer sb=new StringBuffer();
								for(Object a:envelope.getMsg()) {
									sb.append((String)a);
									sb.append(" ");
								}
								sm.setMsg(sb.toString());
								try {
									oos.writeObject(sm);
									oos.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else if(envelope.getMsg().get(0).equals("CancerResetOperation")) {
								resetPwdWindow.frmTinderReset.setVisible(false);
								adminHomeWindow.frmTinderAdmin.setVisible(true);
							}
							
						}
						envelope = null;
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void receiveMsgFromServer() {
		
		revMsgFromServerState = 1;
		
		new Thread() {
			public void run() {
				while(true) {
					Object object = null;
					try {
						object = ois.readObject();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.err.println("GET Object");
					
					System.err.println(object.getClass().toString());
					
					if(object instanceof Message) {
						Message msg = (Message) object;
						if(msg.getMsg().toString().equals("New Group Add Success")) {
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							List<String> groupNameList = (ArrayList) object;
							
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get online user name list!");
							
							List<String> onlineUserNameList = (ArrayList) object;
							
							adminHomeWindow.setOnlineUserList(onlineUserNameList);
							
							
							adminHomeWindow.refreshViewList(groupNameList, 0);
							
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel((String) msg.getMsg());
							addDepartmentWindow.frmNewDepartment.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
							
						}else if(msg.getMsg().toString().equals("Group Name existed already!")){
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel((String) msg.getMsg());
							addDepartmentWindow.frmNewDepartment.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
						}else if(msg.getMsg().toString().equals("Logout Success")) {
							adminLogWindow.frmAdminlog.setVisible(true);
						}
						
						
						else if(msg.getMsg().toString().equals("GroupModifying")) {
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> extraGroupMemberNameList = (ArrayList) object;
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> insideGroupMemberNameList = (ArrayList) object;
							
							modifyDepartmentWindow.refreshExtraMemberList(extraGroupMemberNameList);
							modifyDepartmentWindow.refreshInsideMemberList(insideGroupMemberNameList);
							
							adminHomeWindow.frmTinderAdmin.setVisible(false);
							modifyDepartmentWindow.frmTinderModify.setVisible(true);
						}else if(msg.getMsg().toString().equals("Add User into Group Success!")) {
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> extraGroupMemberNameList = (ArrayList) object;
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> insideGroupMemberNameList = (ArrayList) object;
							
							modifyDepartmentWindow.refreshExtraMemberList(extraGroupMemberNameList);
							modifyDepartmentWindow.refreshInsideMemberList(insideGroupMemberNameList);
							
							infoPrompt.nextMove(modifyDepartmentWindow.frmTinderModify);
							infoPrompt.setLabel("Add User into Group Success!");
							modifyDepartmentWindow.frmTinderModify.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
						}else if(msg.getMsg().toString().equals("Discard User from Group Success!")) {
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> extraGroupMemberNameList = (ArrayList) object;
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> insideGroupMemberNameList = (ArrayList) object;
							
							modifyDepartmentWindow.refreshExtraMemberList(extraGroupMemberNameList);
							modifyDepartmentWindow.refreshInsideMemberList(insideGroupMemberNameList);
							
							infoPrompt.nextMove(modifyDepartmentWindow.frmTinderModify);
							infoPrompt.setLabel("Discard User from Group Success!");
							modifyDepartmentWindow.frmTinderModify.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
						}else if(msg.getMsg().toString().startsWith("ResetPasswdSuccessfully")) {
							String[] splits=msg.getMsg().toString().trim().split("\\s");
							List<String> refresh=new ArrayList<>();
							if(splits.length>1) {
								for(int i=1;i<splits.length;i++) {
									refresh.add(splits[i]);
								}
							}
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel("Reset password successful!");
							resetPwdWindow.frmTinderReset.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get online user name list!");
							
							List<String> onlineUserNameList = (ArrayList) object;
							
							adminHomeWindow.setOnlineUserList(onlineUserNameList);
							
							adminHomeWindow.refreshViewList(refresh, 1);
						}else if(msg.getMsg().toString().startsWith("BlockAccountComplete")) {
							String[] splits=msg.getMsg().toString().trim().split("\\s");
							List<String> refresh=new ArrayList<>();
							if(splits.length>1) {
								for(int i=1;i<splits.length;i++) {
									refresh.add(splits[i]);
								}
							}
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel("Block user successful!");
							adminHomeWindow.frmTinderAdmin.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get online user name list!");
							
							List<String> onlineUserNameList = (ArrayList) object;
							
							adminHomeWindow.setOnlineUserList(onlineUserNameList);
							
							adminHomeWindow.refreshViewList(refresh, 1);
						}else if(msg.getMsg().toString().startsWith("UnlockAccountComplete")) {
							String[] splits=msg.getMsg().toString().trim().split("\\s");
							List<String> refresh=new ArrayList<>();
							if(splits.length>1) {
								for(int i=1;i<splits.length;i++) {
									refresh.add(splits[i]);
								}
							}
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel("Unlock user successful!");
							adminHomeWindow.frmTinderAdmin.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get online user name list!");
							
							List<String> onlineUserNameList = (ArrayList) object;
							
							adminHomeWindow.setOnlineUserList(onlineUserNameList);
							
							adminHomeWindow.refreshViewList(refresh, 1);
						}else if(msg.getMsg().toString().startsWith("DeleteAccountComplete")) {
							String[] splits=msg.getMsg().toString().trim().split("\\s");
							List<String> refresh=new ArrayList<>();
							if(splits.length>1) {
								for(int i=1;i<splits.length;i++) {
									refresh.add(splits[i]);
								}
							}
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel("Delete user successful!");
							adminHomeWindow.frmTinderAdmin.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get online user name list!");
							
							List<String> onlineUserNameList = (ArrayList) object;
							
							adminHomeWindow.setOnlineUserList(onlineUserNameList);
							
							adminHomeWindow.refreshViewList(refresh, 1);
						}else if(msg.getMsg().toString().equals("Add User Account Success")){
							List<String> allUserName = new ArrayList<String>();
							try {
								allUserName = (List<String>) ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							adminHomeWindow.refreshViewList(allUserName, 1);
							
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel("Add User Account Success!");
							infoPrompt.frmWarning.setVisible(true);
							addAccountWindow.frmTinderAdd.setVisible(false);
						}else if(msg.getMsg().toString().equals("User Name has been used!")){
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel((String) msg.getMsg());
							addAccountWindow.frmTinderAdd.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
						}
						else if(msg.getMsg().toString().equals("Delete Department Success")){
							List<String> groupNameList = new ArrayList<String>();
							try {
								groupNameList = (List<String>) ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							adminHomeWindow.refreshViewList(groupNameList, 0);
							
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel((String) msg.getMsg());
							addDepartmentWindow.frmNewDepartment.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
						}
					}else if(object instanceof List<?>) {
						List<String> userNameList = (List<String>) object;
						
						addDepartmentWindow.refreshList(userNameList);
						adminHomeWindow.frmTinderAdmin.setVisible(false);
						addDepartmentWindow.frmNewDepartment.setVisible(true);
						
					}else if(object instanceof SystemMsgForNotify) {
						SystemMsgForNotify smfn = (SystemMsgForNotify) object;
						
						System.err.println("Get fileinfo");
						System.err.println(smfn.getMsg());
						
						if(smfn.getMsg().equals("please check your password!")) {
							infoPrompt.nextMove(adminLogWindow.frmAdminlog);
							infoPrompt.setLabel(smfn.getMsg());
							adminLogWindow.frmAdminlog.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
							
						}else if(smfn.getMsg().equals("Welcome to the chat room!")){
							
							System.err.println("In welcome");
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get group name list!");
							
							List<String> groupNameList = (ArrayList) object;
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get user name list!");
							
							List<String> userNameList = (ArrayList) object;
							
							try {
								object = ois.readObject();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.err.println("Get online user name list!");
							
							List<String> onlineUserNameList = (ArrayList) object;
							
							adminHomeWindow.setOnlineUserList(onlineUserNameList);
							
							
							adminHomeWindow.refreshViewList(groupNameList, 0);
							adminHomeWindow.refreshViewList(userNameList, 1);
							
							infoPrompt.nextMove(adminHomeWindow.frmTinderAdmin);
							infoPrompt.setLabel("Welcome to administrator room!");
							adminLogWindow.frmAdminlog.setVisible(false);
							infoPrompt.frmWarning.setVisible(true);
						}else if(smfn.getMsg().startsWith("Client Login!")) {
							String logUser = smfn.getMsg().split("\\s+")[2];
							adminHomeWindow.setLogState(logUser, 1);
						}else if(smfn.getMsg().startsWith("Client Logout!")) {
							String logUser = smfn.getMsg().split("\\s+")[2];
							adminHomeWindow.setLogState(logUser, 0);
						}
					}
						
					
					
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public static void main(String[] args) {
		AdminApplication adminApplication = new AdminApplication();
		adminApplication.start();
	}
}
