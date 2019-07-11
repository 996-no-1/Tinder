package ClientGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTree;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import Client.AudioPlay;
import ClientGUI.Envelope;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ClientHomeUI {

	JFrame frmTinderClient;
	private JTextArea systemTextArea;
	private JTree clientListTree;
	private JLabel usernameLable;
	private JButton infoModifyBtn;
	private JButton logoutBtn;
	private ChatApplication chatApplication;
	private List<String> curclientList;
	private List<List<String>> groupList;
	private JScrollPane scrollPane;
	private String systemMsg="";
	private JScrollPane scrollPane_1;
	private DefaultMutableTreeNode msglistNode=null;
	private Map<String,DefaultMutableTreeNode> newMsgList=new HashMap<>();
 	private DefaultTreeModel model;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientHomeUI window = new ClientHomeUI();
					window.frmTinderClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientHomeUI() {
		initialize();
	}

	public ClientHomeUI(ChatApplication chatApplication) {
		initialize();
		this.chatApplication=chatApplication;
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			UIManager.put("RootPane.setupButtonVisible", false);
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frmTinderClient = new JFrame();
		frmTinderClient.setResizable(false);
		frmTinderClient.setTitle("Tinder Client");
		frmTinderClient.setBounds(100, 100, 451, 777);
		frmTinderClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderClient.getContentPane().setLayout(null);
		
		logoutBtn = new JButton("Logout");
		logoutBtn.setFont(new Font("宋体", Font.PLAIN, 11));
		logoutBtn.setBounds(363, 13, 68, 27);
		frmTinderClient.getContentPane().add(logoutBtn);
		
		infoModifyBtn = new JButton("Privacy");
		infoModifyBtn.setFont(new Font("宋体", Font.PLAIN, 11));
		infoModifyBtn.setBounds(286, 13, 75, 27);
		frmTinderClient.getContentPane().add(infoModifyBtn);
		
		JLabel lblWelcome = new JLabel("Welcome");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(14, 16, 72, 18);
		frmTinderClient.getContentPane().add(lblWelcome);
		
		usernameLable = new JLabel("username");
		usernameLable.setBounds(89, 16, 72, 18);
		frmTinderClient.getContentPane().add(usernameLable);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 47, 417, 513);
		frmTinderClient.getContentPane().add(scrollPane);
		
		clientListTree = new JTree();
		
		clientListTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("colors");
						node_1.add(new DefaultMutableTreeNode("blue"));
						node_1.add(new DefaultMutableTreeNode("viole"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("sports");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("food");
						node_1.add(new DefaultMutableTreeNode("hot dogs"));
						node_1.add(new DefaultMutableTreeNode("pizza"));
						node_1.add(new DefaultMutableTreeNode("ravioli"));
						node_1.add(new DefaultMutableTreeNode("bananas"));
					add(node_1);
				}
			}
		));
		scrollPane.setViewportView(clientListTree);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 573, 417, 156);
		frmTinderClient.getContentPane().add(scrollPane_1);
		
		systemTextArea = new JTextArea();
		scrollPane_1.setViewportView(systemTextArea);
		addMouseListener(clientListTree);
	}

	public void updateMsg(String msg, String sender) {
		if (systemTextArea!=null) {
			scrollPane_1.remove(systemTextArea);
			systemTextArea=null;
		}
		systemTextArea = new JTextArea();
		systemMsg+="\n"+msg;
		systemTextArea.setText(systemMsg);
		scrollPane_1.setViewportView(systemTextArea);
		scrollPane.validate();
		scrollPane.repaint();
		frmTinderClient.validate();
		frmTinderClient.repaint();
	}
/**
 * 更新新消息列表
 * @param sender
 * @param type 0表示添加，1表示删除
 */
	public void refreshMsgList(String sender,int type) {
		System.err.println("b");
		System.err.println(msglistNode.getChildCount());
		System.err.println("A");
		if (type==0) {
			//添加消息列表
			if (!newMsgList.containsKey(sender)) {
				DefaultMutableTreeNode cur=new DefaultMutableTreeNode(sender);
				newMsgList.put(sender, cur);
				msglistNode.add(cur);
				groupList.get(0).add(sender);
			}else {
				DefaultMutableTreeNode cur=newMsgList.get(sender);
				msglistNode.remove(cur);
				msglistNode.add(cur);
			}
		}else {
			//移出消息列表
			DefaultMutableTreeNode cur=newMsgList.get(sender);
			newMsgList.remove(sender);
			DefaultMutableTreeNode temp=cur;
			for (int i=0;i<msglistNode.getChildCount();i++) {
				if (msglistNode.getChildAt(i).toString().equals(cur.toString())) {
					temp=(DefaultMutableTreeNode) msglistNode.getChildAt(i);
					break;
				}
			}
			msglistNode.remove(temp);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.model.reload(msglistNode);
		clientListTree.validate();
		clientListTree.repaint();
		scrollPane.validate();
		scrollPane.repaint();
		frmTinderClient.validate();
		frmTinderClient.repaint();
	}
	
	public void refreshTree(List<String> clientList, List<List<String>> groups) {
		curclientList=clientList;
		groupList=groups;
		for (String list : clientList) {
			System.err.println(list);
		}
		//重置客户列表
		if (clientListTree!=null) {
			scrollPane.remove(clientListTree);
			clientListTree=null;
		}
		for (String client : clientList) {
			for (List<String> groupList : groups) {
				if (groupList.contains(client)) {
					groupList.remove(client);
					groupList.add(client);
				}
			}
		}
		DefaultTreeModel model=null;
		clientListTree = new JTree();
		clientListTree.setCellRenderer(new NodeRenderer());
		clientListTree.setModel(model=new DefaultTreeModel(
			new DefaultMutableTreeNode("Tinder List") {
				private static final long serialVersionUID = 1L;
				{
					if (msglistNode==null) {
						DefaultMutableTreeNode msglist=new DefaultMutableTreeNode("New Message");
						add(new DefaultMutableTreeNode(msglist));
						msglistNode=msglist;
					}else {
						add(msglistNode);
						msglistNode.removeAllChildren();
						for (DefaultMutableTreeNode node : newMsgList.values()) {
							msglistNode.add(node);
						}
					}
					for (List<String> groupList : groups) {
						DefaultMutableTreeNode curNode;
						curNode=new DefaultMutableTreeNode(groupList.get(0));
						curNode.add(new DefaultMutableTreeNode(groupList.get(0)+"[group]"));
						for (int i = groupList.size()-1; i >=1; i--) {
							curNode.add(new DefaultMutableTreeNode(groupList.get(i)));
						}
						add(curNode);
					}
				}
			}
		));
		this.model=model;
		scrollPane.setViewportView(clientListTree);
		clientListTree.setVisible(true);
		scrollPane.validate();
		scrollPane.repaint();
		frmTinderClient.validate();
		frmTinderClient.repaint();
		addMouseListener(clientListTree);
		
	}
	
	
	class NodeRenderer extends DefaultTreeCellRenderer
    {
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value,
                                               selected, expanded,
                                               leaf, row, hasFocus);
            MutableTreeNode node = (MutableTreeNode)value;
            if(!curclientList.contains(node.toString())) //条件满足
            {
                setForeground(Color.GRAY);
            }
            	if (node.getParent()!=null&&node.getParent().toString().contains("New Message")) {
            		this.setIcon(new ImageIcon("image/xiaoxi.png"));
				}else if (node.toString().contains("New Message")) {
					this.setIcon(new ImageIcon("image/xiaoxitixing.png"));
				}else {
					//非消息成员
					Boolean flag=true;
					for (List<String> group : groupList) {
						//组
						if (node.toString().contains(group.get(0))) {
							this.setIcon(new ImageIcon("image/qunzu.png"));
							flag=false;
						}
					}
					if (flag) {
						//普通成员
						this.setIcon(new ImageIcon("image/geren.png"));
					}
				}
            return this;
        }
    }
	
	
	/**
	 * 设置用户名
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		frmTinderClient.remove(usernameLable);
		usernameLable = new JLabel(username);
		usernameLable.setBounds(89, 16, 72, 18);
		frmTinderClient.getContentPane().add(usernameLable);
		frmTinderClient.validate();
		frmTinderClient.repaint();
	}
	
	
	
	public void addMouseListener(JTree jTree) {
		jTree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount()==2) {
					DefaultMutableTreeNode cur=(DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
					if (cur.getChildCount()!=0||cur.toString().contains("New Message")||chatApplication.chatRoomUIMap.containsKey(cur.toString())) {
						return;
					}
					if (newMsgList.containsKey(cur.toString())) {
						refreshMsgList(cur.toString(), 1);
						newMsgList.remove(cur.toString());
					}
					ChatRoomUI chatRoomUI=new ChatRoomUI(chatApplication);
					chatRoomUI.setReceiver(cur.toString());
					try {
						File file=new File(chatApplication.path+"/"+cur.toString()+".txt");
						if(!file.exists())file.createNewFile();
						BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
						String temp="";
						String cur1="";
						while((cur1=bufferedReader.readLine())!=null){
							temp+=cur1+"\n";
						}
						chatRoomUI.setMsgArea(temp);
						chatRoomUI.msg=temp;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					
					chatApplication.chatRoomUIMap.put(cur.toString(), chatRoomUI);
					chatRoomUI.frmTinderChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					chatRoomUI.frmTinderChat.setVisible(true);
					Envelope envelope = new Envelope();
					envelope.setSourceName("ClientHomeUI");
					List<Object> objects = new ArrayList<>();
					objects.add(cur.toString());
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
				}
				
			}
		});
	}
	
	
	
}
