package Server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import algorithm.RSA;
import bean.User;
import data.Certificate;
import data.ClientInfo;
import data.FileSaved;
import data.FileSend;
import data.Message;
import data.SystemMsgForNotify;
import database.FileDao;
import database.HistoryDao;
import database.UserDao;

public class ServerThread extends Thread {

	private Socket socket = null;					//Socket that connected to one client
	private ObjectInputStream receive = null;		//ObjectInputStream
	private ObjectOutputStream send = null;			//ObjectOutputStream
	private RSA cypherRSA = null;					//RSA information of server
	private String username = null;
	private int userType = 0;						//If user is new client,value is 0, otherwise,value is 1
	
	//Constructor method of ServerThread[initial the value of parameters: socket, cypherRSA, receive, send]
	public ServerThread(Socket socket,RSA cypherRSA) throws IOException {
		this.socket = socket;
		this.cypherRSA = cypherRSA;
		receive = new ObjectInputStream(socket.getInputStream());
		send = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public void run() {
		try {
			
			//Server send its RSA public key to client that is connect lately
			sendServerRSAPubKey();
			
			//Server handle login operation of client
			handleLogin();
			
			receiveClientRSAPubKey();
			
			sendClientList(send);
			
			informNewClient();
			
			while(true){
				handle();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				informOffClient();
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	//Server send its RSA public key to client that is connect lately
	private void sendServerRSAPubKey() throws IOException {
		Certificate certificate = new Certificate();
		certificate.setUsername("Server");
		certificate.setE(cypherRSA.getE());
		certificate.setN(cypherRSA.getN());
		send.writeObject(certificate);send.flush();
	}
	
	//Server send client the client list that includes all the online clients
	private void sendClientList(ObjectOutputStream s) throws IOException {
		s.writeObject(Server.getClientList());s.flush();
	}
	
	//Server inform all online clients except this that there is a new client enter chat room
	private void informNewClient() throws IOException {
		List<String> clientList = Server.getClientList();
		
		for(int i = 0;i < clientList.size();i++) {
			ObjectOutputStream tmp = Server.getServerThread(clientList.get(i)).getOOS();
			SystemMsgForNotify smfn = new SystemMsgForNotify();
			smfn.setSender("Server");
			smfn.setType(1);
			smfn.setMsg("New Client " + username + " enter chat room!");
			tmp.writeObject(smfn);tmp.flush();
			
			sendClientList(Server.getServerThread(clientList.get(i)).getOOS());
			
			System.err.println("010101");
		}
	}
	
	private void informOffClient() throws IOException {
		List<String> clientList = Server.getClientList();
		Server.deleteClientSocket(username);
		for(int i = 0;i < clientList.size();i++) {
			if(clientList.get(i).equals(username)) continue;
			ObjectOutputStream tmp = Server.getServerThread(clientList.get(i)).getOOS();
			SystemMsgForNotify smfn = new SystemMsgForNotify();
			smfn.setSender("Server");
			smfn.setType(1);
			smfn.setMsg("Offline Client " + username + " leave chat room!");
			tmp.writeObject(smfn);tmp.flush();
			sendClientList(Server.getServerThread(clientList.get(i)).getOOS());
		}
	}

	//Server forward the message from the client,which this thread connected,to the destination client
	private void forwardMessage(Message msg) throws ClassNotFoundException, IOException {
		System.err.println("55555");
		ObjectOutputStream tmp = Server.getServerThread(msg.getTo()).getOOS();
		tmp.writeObject(msg);tmp.flush();
		System.err.println(msg.getFrom());
		System.err.println(msg.getTo());
	}
	
	//Server forward the message from the client,which this thread connected,to the destination client
	private void forwardFile(FileSend fs) throws IOException {
		ObjectOutputStream tmp = Server.getServerThread(fs.getTo()).getOOS();
		tmp.writeObject(fs);tmp.flush();
		tmp.writeObject(Server.getClientCertificate(username));tmp.flush();
	}
	
	//Server receive the public key from the new client
	private void receiveClientRSAPubKey() throws ClassNotFoundException, IOException {
		Certificate certificate = (Certificate) receive.readObject();
		Server.addClientCertificate(username, certificate);
		
		User user = new User();
		user.setUsername(certificate.getUsername());
		user.setPublickey(tranBigIntegerToString(certificate.getE(), certificate.getN()));
		
		UserDao userdao = new UserDao();
		
		int flag = userdao.updatePublicKey(user);
		
		
		if(flag == 0) return;
	}
	
	//This method handle the object that received from client and through its instance to do corresponding operations
	private void handle() throws ClassNotFoundException, IOException {
		Object rev = receive.readObject();
		System.err.println("987987");
		if(rev instanceof Message) {
			forwardMessage((Message) rev);
		}else if(rev instanceof SystemMsgForNotify) {
			System.err.println("iiiiiii");
			if(((SystemMsgForNotify) rev).getType() == 2) {
				System.err.println(((SystemMsgForNotify) rev).getMsg());
				
				send.writeObject(Server.getClientCertificate(((SystemMsgForNotify) rev).getMsg()));
				send.flush();
				System.err.println(Server.getClientCertificate(((SystemMsgForNotify) rev).getMsg()) instanceof Certificate);
			}
		}else if(rev instanceof FileSend) {
			FileDao filedao = new FileDao();
			int flag = filedao.addFile((FileSend) rev);
			
			forwardFile((FileSend) rev);
		}else if(rev instanceof FileSaved) {
			HistoryDao hdao = new HistoryDao();
			hdao.uploadHistory((FileSaved) rev);
		}
			
	}
	
	//Server handle login operation of client
	private void handleLogin() throws ClassNotFoundException, IOException {
		while(true) {
			ClientInfo clientInfo = (ClientInfo) receive.readObject();
			
			
			User user = new User();
			user.setUsername(clientInfo.getUsername());
			user.setPassword(cypherRSA.decryptMsg(clientInfo.getPassword()));
			
			UserDao userdao = new UserDao();
			
			if(clientInfo.getType() == 0) {
				int flag = userdao.addUser(user);
				if(flag == -1||flag == 1) {
					
					SystemMsgForNotify smfn = new SystemMsgForNotify();
					smfn.setMsg("Please check your account!");
					smfn.setSender("Server");
					smfn.setType(0);
					send.writeObject(smfn);send.flush();
				}else {
					SystemMsgForNotify smfn = new SystemMsgForNotify();
					smfn.setMsg("Welcome to the chat room!");
					smfn.setSender("Server");
					smfn.setType(0);
					send.writeObject(smfn);send.flush();
					
					
					username = user.getUsername();
					Server.addClientSocket(username, socket);
					
					Server.addServerThreadList(username, this);
					
					break;
				}
			}else {
				User targetUser = userdao.getUser(user.getUsername());
				System.err.println(user.getUsername());
				if(targetUser == null) {
					

					SystemMsgForNotify smfn = new SystemMsgForNotify();
					smfn.setMsg("Please check your account!");
					smfn.setSender("Server");
					smfn.setType(0);
					send.writeObject(smfn);send.flush();
					
					
				}else if(targetUser.getPassword().equals(user.getPassword())){
					
					
					
					SystemMsgForNotify smfn = new SystemMsgForNotify();
					smfn.setMsg("Welcome to the chat room!");
					smfn.setSender("Server");
					smfn.setType(0);
					send.writeObject(smfn);send.flush();
					
					username = user.getUsername();
					Server.addClientSocket(username, socket);
					Server.addServerThreadList(username, this);
					
					
					break;
				}
			}
			
		}
	}
	
	//Transfer BigInteger into String
	private String tranBigIntegerToString(BigInteger E,BigInteger N) {
		return E.toString() + "-" + N.toString();
	}
	
	//Transfer String into BigInteger
	private BigInteger[] tranStringToBigInteger(String key) {
		BigInteger[] res = new BigInteger[2];
		
		StringBuffer tmp = new StringBuffer("");
		int i = 0;
		for(;key.charAt(i) != '-';i++) {
			tmp.append(key.charAt(i));
		}
		res[1] = new BigInteger(tmp.toString());
		i++;tmp = new StringBuffer("");
		for(;i < key.length();i++) {
			tmp.append(key.charAt(i));
		}
		res[0] = new BigInteger(tmp.toString());
		
		return res;
	}
	
	public ObjectOutputStream getOOS() {
		return send;
	}
	
}
