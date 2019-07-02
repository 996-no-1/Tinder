package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import algorithm.RSA;
import data.Certificate;

public class Server {

	private static HashMap<String,Socket> clientSocketList;
	private static HashMap<String,Certificate> clientCertificateList;
	private static HashMap<String,ServerThread> serverThreadList;
	
	public static void main(String[] args) {
		try {
			serverThreadList = new HashMap<String,ServerThread>();
			clientSocketList = new HashMap<String,Socket>();
			clientCertificateList = new HashMap<String,Certificate>();
			RSA cypherRSA = new RSA();
			cypherRSA.getRSAKey();
			ServerSocket ss = new ServerSocket(2200);
			while(true) {
				Socket receive = ss.accept();
				ServerThread st = new ServerThread(receive,cypherRSA);
				st.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Socket getClientSocket(String username) {
		return clientSocketList.get(username);
	}
	
	public static Certificate getClientCertificate(String username) {
		return clientCertificateList.get(username);
	}
	
	public static ServerThread getServerThread(String username) {
		return serverThreadList.get(username);
	}
	
	public static void addClientSocket(String username,Socket socket) {
		clientSocketList.put(username, socket);
	}
	
	public static void addClientCertificate(String username,Certificate ctf) {
		clientCertificateList.put(username, ctf);
	}
	
	public static void addServerThreadList(String username,ServerThread st) {
		serverThreadList.put(username, st);
	}
	
	public static void deleteClientSocket(String username) {
		clientSocketList.remove(username);
	}
	
	public static List<String> getClientList(){
		List<String> CL = new ArrayList<String>();
		
		@SuppressWarnings("rawtypes")
		Iterator it = (Iterator) clientSocketList.keySet().iterator();
		while(it.hasNext()) {
			String aa = it.next().toString();
			CL.add(aa);
		}
		
		return CL;
	}
}
