package ServerTheardPool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Client.Certificate;
import ServerImp.ServerHandler;
import db.dao.UserDao;
import db.entity.User;

/**
 * This class implements a highly efficient way to handle many clients' requests at the same time.
 * 
 * @author  Wang Zhichao 2019/05/25
 * @version 2.0
 */


public class ServerTheardPool {
	ServerSocket serverSocket;
	private final int PORT=2021;//TCP Port
	ExecutorService executorService;//Thread pool
	final int POOL_SIZE=4;
	static Map<String, String> clientLog=new HashMap<>();
	static Map<String, Socket> clientList=new HashMap<>();//this obj is used to maintain the information between server and clients.
	static Map<String, Certificate> certificateList=new HashMap<>();//To store the client 's RSA public Key
	static Map<String, Integer> clientIndex=new HashMap<>();
	static Map<String, ObjectOutputStream> clientOut=new HashMap<>();
	static Map<String, ObjectInputStream> clientIn=new HashMap<>();
	/**
	 * constructor
	 * @throws IOException 
	 */
	public ServerTheardPool() throws IOException {
		//load client information form
		UserDao udao = new UserDao();
		List<User> allUsers = udao.getAllUser();
		
		for (User user : allUsers) {
			clientLog.put(user.getUsername(), user.getHashedPassword());
			
			System.err.println("Username: " + user.getUsername());
			System.err.println("Password: " + user.getHashedPassword());
		}
		
		
		serverSocket=new ServerSocket(PORT);
		executorService=Executors.newFixedThreadPool(POOL_SIZE);
		System.out.println("Server has started!");
	}
	/**
	 * to distribute the thread 
	 */
	public void start() {
		Socket socket=null;
		Integer count=1;
		while(true){
			try {
				socket = serverSocket.accept();// wait for client request
				  if (socket != null) {
				executorService.execute(new ServerHandler(socket, clientList,count,certificateList,clientLog,clientIndex,clientIn,clientOut));
				count++;
				  }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * the default action
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String []args) throws IOException {
		new ServerTheardPool().start();
	}
	
}
