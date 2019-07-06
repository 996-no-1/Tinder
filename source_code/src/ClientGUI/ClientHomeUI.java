package ClientGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import Client.ClientInfo;
import ClientGUI.Envelope;
import ClientGUI.ChatApplication;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.SystemColor;

public class ClientHomeUI {

	JFrame frmTinderClient;
	private JTextArea systemTextArea;
	private JTree clientListTree;
	private JLabel usernameLable;
	private JButton infoModifyBtn;
	private JButton logoutBtn;
	private ChatApplication chatApplication;
	 List<String> curclientList;
	List<List<String>> groupList;
	private Map<String,ClientInfo> clientInfoMap=new HashMap<>();
	private JScrollPane scrollPane;
	private String systemMsg = "";
	private JScrollPane scrollPane_1;
	private DefaultMutableTreeNode msglistNode = null;
	private Map<String, DefaultMutableTreeNode> newMsgList = new HashMap<>();
	private DefaultTreeModel model;
	private DisplayCardUI displayCardUI;


	/**
	 * @param clientInfos the clientInfos to set
	 */
	public void setClientInfos(List<ClientInfo> clientInfos) {
		clientInfoMap.clear();
		for (ClientInfo clientInfo : clientInfos) {
			clientInfoMap.put(clientInfo.getUsername(),clientInfo);
		}
		return;
	}

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
		this.chatApplication = chatApplication;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		usernameLable.setForeground(new Color(51, 51, 204));
		usernameLable.setBounds(89, 16, 72, 18);
		frmTinderClient.getContentPane().add(usernameLable);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 47, 417, 513);
		frmTinderClient.getContentPane().add(scrollPane);

		clientListTree = new JTree();

		clientListTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Loading.......") {
			private static final long serialVersionUID = 9L;

			{
			}
		}));
		scrollPane.setViewportView(clientListTree);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 573, 417, 156);
		frmTinderClient.getContentPane().add(scrollPane_1);

		systemTextArea = new JTextArea();
		systemTextArea.setFont(systemTextArea.getFont().deriveFont(systemTextArea.getFont().getStyle() | Font.BOLD,
				systemTextArea.getFont().getSize() + 2f));
		systemTextArea.setForeground(new Color(204, 0, 51));
		systemTextArea.setBackground(SystemColor.scrollbar);
		scrollPane_1.setViewportView(systemTextArea);

		addMouseListener(clientListTree);
		addListener(logoutBtn);
		addListener(infoModifyBtn);
	}

	public void updateMsg(String msg, String sender) {
		if (systemTextArea != null) {
			scrollPane_1.remove(systemTextArea);
			systemTextArea = null;
		}
		systemTextArea = new JTextArea();
		systemMsg += msg + "\n";
		systemTextArea.setText(systemMsg);
		systemTextArea.setEditable(false);
		systemTextArea.setFont(systemTextArea.getFont().deriveFont(systemTextArea.getFont().getStyle() | Font.BOLD,
				systemTextArea.getFont().getSize() + 1f));
		systemTextArea.setForeground(new Color(204, 0, 51));
		systemTextArea.setBackground(SystemColor.white);
		scrollPane_1.setViewportView(systemTextArea);
		scrollPane.validate();
		scrollPane.repaint();
		frmTinderClient.validate();
		frmTinderClient.repaint();
	}

	/**
	 * 更新新消息列表
	 * 
	 * @param sender
	 * @param type
	 *            0表示添加，1表示删除
	 */
	public void refreshMsgList(String sender, int type) {
		if (type == 0) {
			// 添加消息列表
			if (!newMsgList.containsKey(sender)) {
				System.err.println("不在列表");
				DefaultMutableTreeNode cur = new DefaultMutableTreeNode(sender);
				newMsgList.put(sender, cur);
				msglistNode.insert(cur, msglistNode.getChildCount());
				groupList.get(0).add(sender);
			} else {
				System.err.println("在列表");
				DefaultMutableTreeNode cur = newMsgList.get(sender);
				msglistNode.remove(cur);
				msglistNode.insert(cur, msglistNode.getChildCount());
			}
		} else {
			// 移出消息列表
			DefaultMutableTreeNode cur = newMsgList.get(sender);
			newMsgList.remove(sender);
			DefaultMutableTreeNode temp = cur;
			for (int i = 0; i < msglistNode.getChildCount(); i++) {
				if (msglistNode.getChildAt(i).toString().equals(cur.toString())) {
					temp = (DefaultMutableTreeNode) msglistNode.getChildAt(i);
					break;
				}
			}
			msglistNode.remove(temp);
			groupList.get(0).remove(sender);
		}
		this.model.reload(msglistNode);
		System.err.println("消息数量："+msglistNode.getChildCount());
		clientListTree.setModel(model);
		try {
			Thread.sleep(200);
			refreshTree(curclientList, groupList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void refreshTree(List<String> clientList, List<List<String>> groups) {
		System.err.println("刷新tree");
		curclientList = clientList;
		groupList = groups;
		for (String list : clientList) {
			System.err.println(list);
		}
		// 重置客户列表
		if (clientListTree != null) {
			scrollPane.remove(clientListTree);
			clientListTree = null;
		}
		for (String client : clientList) {
			for (List<String> groupList : groups) {
				if (groupList.contains(client)) {
					groupList.remove(client);
					groupList.add(client);
				}
			}
		}
		DefaultTreeModel model = null;
		clientListTree = new JTree();
		clientListTree.setCellRenderer(new NodeRenderer());
		clientListTree.setModel(model = new DefaultTreeModel(new DefaultMutableTreeNode("Tinder List") {
			private static final long serialVersionUID = 1L;
			{
				if (msglistNode == null) {
					DefaultMutableTreeNode msglist = new DefaultMutableTreeNode("New Message");
					add(new DefaultMutableTreeNode(msglist));
					msglistNode = msglist;
				} else {
					add(msglistNode);
					msglistNode.removeAllChildren();
					for (DefaultMutableTreeNode node : newMsgList.values()) {
						msglistNode.add(node);
					}
				}
				for (List<String> groupList : groups) {
					DefaultMutableTreeNode curNode;
					curNode = new DefaultMutableTreeNode(groupList.get(0));
					curNode.add(new DefaultMutableTreeNode(groupList.get(0) + "[group]"));
					for (int i = groupList.size() - 1; i >= 1; i--) {
						curNode.add(new DefaultMutableTreeNode(groupList.get(i)));
					}
					add(curNode);
				}
			}
		}));
		this.model = model;
		scrollPane.setViewportView(clientListTree);
		clientListTree.setVisible(true);
		scrollPane.validate();
		scrollPane.repaint();
		frmTinderClient.validate();
		frmTinderClient.repaint();
		addMouseListener(clientListTree);
		addFrameListener(frmTinderClient);

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

	class NodeRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -8012134487683561483L;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			MutableTreeNode node = (MutableTreeNode) value;
			if (!node.toString().contains("New Message")&&!node.toString().contains("group")&&node.getChildCount() == 0 && !(curclientList.contains(node.toString())||node.getParent().toString().contains("New Message"))) // 条件满足
			{
				setForeground(Color.LIGHT_GRAY);
				setFont(systemTextArea.getFont().deriveFont(systemTextArea.getFont().getStyle() | Font.BOLD,
						systemTextArea.getFont().getSize() + 1f));
			} else {
				setForeground(Color.BLACK);
				setFont(systemTextArea.getFont().deriveFont(systemTextArea.getFont().getStyle() | Font.BOLD,
						systemTextArea.getFont().getSize() + 1f));
			}
			if (node.getParent() != null && node.getParent().toString().contains("New Message")) {
				this.setIcon(new ImageIcon("image/xiaoxi.png"));
			} else if (node.toString().contains("New Message")) {
				this.setIcon(new ImageIcon("image/xiaoxitixing.png"));
			} else {
				// 非消息成员
				Boolean flag = true;
				for (List<String> group : groupList) {
					// 组
					if (node.toString().contains(group.get(0))) {
						this.setIcon(new ImageIcon("image/qunzu.png"));
						flag = false;
					}
				}
				if (flag) {
					// 普通成员
					this.setIcon(new ImageIcon("image/geren.png"));
				}
			}
			return this;
		}
	}


	public void addMouseListener(JTree jTree) {
		jTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (displayCardUI!=null) {
					displayCardUI.frame.dispose();
				displayCardUI=null;
				}
				if (e.isMetaDown()&&!jTree.isSelectionEmpty()) {
					DefaultMutableTreeNode cur = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
						displayCardUI=new DisplayCardUI();
						displayCardUI.setLocation(jTree);
						ClientInfo clientInfo=clientInfoMap.get(cur.toString());
						String status="online";
						if (!curclientList.contains(cur.toString())) {
							status="offline";
						}
						displayCardUI.setContent(clientInfo.getUsername(), String.valueOf(clientInfo.getAge()), clientInfo.getGender(), clientInfo.getNote(), status);
				}
				else if(e.getClickCount() == 2) {
					if (newMsgList.containsKey(cur.toString())) {
						refreshMsgList(cur.toString(), 1);
						newMsgList.remove(cur.toString());
					}
					ChatRoomUI chatRoomUI = new ChatRoomUI(chatApplication);
					chatRoomUI.setReceiver(cur.toString());
					try {
						File file = new File(chatApplication.path + "/" + cur.toString() + ".txt");
						if (!file.exists())
							file.createNewFile();
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						String temp = "";
						String cur1 = "";
						while ((cur1 = bufferedReader.readLine()) != null) {
							temp += cur1 + "\n";
						}
						bufferedReader.close();
						chatRoomUI.setMsgArea(temp);
						chatRoomUI.msg = temp;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					chatApplication.chatRoomUIMap.put(cur.toString(), chatRoomUI);
					chatRoomUI.frmTinderChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					chatRoomUI.frmTinderChat.setLocationRelativeTo(frmTinderClient);
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

	public void addListener(JButton jButton) {
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object object = e.getSource();
				if (object.equals(logoutBtn)) {
					try {
						chatApplication.tcpSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					frmTinderClient.setVisible(false);
					chatApplication = null;
					ChatApplication chatApplication = new ChatApplication();
					chatApplication.start();
				} else if (object.equals(infoModifyBtn)) {
					ClientInfo client=clientInfoMap.get(chatApplication.username);
					chatApplication.privacyInfoUI.setContent(chatApplication.username
							, client.getGender(), String.valueOf(client.getAge()), client.getNote(), chatApplication.password);
					chatApplication.privacyInfoUI.setNextMove(frmTinderClient);
					frmTinderClient.setVisible(false);
					chatApplication.privacyInfoUI.frmTinderPrivacy.setVisible(true);
				}
			}
		});
	}
	public void addFrameListener(JFrame frame) {
		frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {	
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (displayCardUI!=null) {
					displayCardUI.frame.dispose();
					displayCardUI=null;
				}
			}
		});
	}

}
