package ServerImp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.IREM;

import Client.FileSend;
import Client.Message;
import db.dao.GroupDao;
import db.entity.Group;

public class GroupChatFunc {
	
	private Map<String, ObjectOutputStream> oos = null;
	
	public GroupChatFunc(Map<String, ObjectOutputStream> oos){
		this.oos = oos;
	}

	public void processGroupChat(Message msg) throws IOException {
		
		String fromName = msg.getSender(),groupName = msg.getReceiver().split("\\[")[0];
		List<String> toList;
		
		if(fromName.equals("Admin")) {
			
		}else {
			toList = getToList(groupName);
			transferMsg(fromName,msg,toList);
		}
		
	}
	
	public void processGroupChat(FileSend fs,int type) throws IOException {
		
		if(type == 1){
			String fromName = fs.getFrom(),groupName = fs.getTo().split("\\[")[0];
			List<String> toList;
			
			if(fromName.equals("Admin")) {
				
			}else {
				toList = getToList(groupName);
				transferFile(fromName,fs,toList);
			}
		}else{
			oos.get(fs.getTo()).writeObject(fs);oos.get(fs.getTo()).flush();
		}
		
	}
	
	private void transferCreate(String fromName,String GroupName,String[] toList) {
		
	}
	
	private void transferMsg(String fromName,Message msg,List<String> toList) throws IOException {
		
		for (String ite : toList) {
			if(ite.equals(fromName)) continue;
			oos.get(ite).writeObject(msg);oos.get(ite).flush();
		}
		
	}
	
	private void transferFile(String fromName,FileSend fs,List<String> toList) throws IOException{
		
		for (String ite : toList) {
			if(ite.equals(fromName)) continue;
			oos.get(ite).writeObject(fs);oos.get(ite).flush();
		}
	}
	
	private List<String> getToList(String groupName) {
		List<String> toList = new ArrayList<String>();
		GroupDao gdao = new GroupDao();
		List<Group> groupList = gdao.getAllGroup();
		for (Group group : groupList) {
			if(groupName.equals(group.getGroupname())) {
				toList = group.getMemberUsernameList();
			}
		}
		
		return toList;
	}
	
	public List<List<String>> getTotalMemberGroup(){
		List<List<String>> res = new ArrayList<List<String>>();
		GroupDao gdao = new GroupDao();
		List<Group> groupList = gdao.getAllGroup();
		for (Group group : groupList) {
			
		}
		
		return res;
	}
	
}
