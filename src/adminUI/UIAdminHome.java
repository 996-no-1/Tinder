package adminUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.Envelope;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class UIAdminHome {

	JFrame frmTinderAdmin;
	private JButton addDepartmentBtn;
	private JButton ModifyDepartmentBtn;
	private JButton deleteDepartmentBtn;
	private JRadioButton clientRadio;
	private JRadioButton departmentRadio;
	private JButton logoutBtn;
	private JPanel panel;
	private JLabel label;
	private JButton blockAccountBtn;
	private JButton deleteAccountBtn;
	private JButton btnAddAccount;
	private JButton resetPasswordBtn;
	private ButtonGroup buttonGroup;
	private JButton unlockAccountBtn;
	
	private AdminApplication adminApplication;
	private JList<String> list;
	private List<String> onlineUserList;
	
	List<String> userList;
	List<String> groupList;
	private JScrollPane scrollPane;
	private JList list_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIAdminHome window = new UIAdminHome();
					
					String[] defaultList = {"asdasd","4864151","123reqrqer"};
					for (String dd : defaultList) {
						window.userList.add(dd);
					}
					String[] defaultList2 = {"trtrtrt","vbvbvb","asdadasd"};
					for (String dd : defaultList2) {
						window.groupList.add(dd);
					}
					
					
					window.refreshViewList(window.userList,1);
					
					window.frmTinderAdmin.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIAdminHome() {
		initialize();
		onlineUserList = new ArrayList<>();
	}
	
	public UIAdminHome(AdminApplication adminApplication) {
		initialize();
		this.adminApplication = adminApplication;
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
			e.printStackTrace();
		}
		
		frmTinderAdmin = new JFrame();
		frmTinderAdmin.setResizable(false);
		frmTinderAdmin.setTitle("Tinder Admin");
		frmTinderAdmin.setBounds(100, 100, 472, 628);
		frmTinderAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderAdmin.getContentPane().setLayout(null);
		
		JLabel lblAdminPanel = new JLabel("Admin Panel");
		lblAdminPanel.setFont(lblAdminPanel.getFont().deriveFont(lblAdminPanel.getFont().getStyle() | Font.BOLD, lblAdminPanel.getFont().getSize() + 3f));
		lblAdminPanel.setBounds(14, 13, 110, 26);
		frmTinderAdmin.getContentPane().add(lblAdminPanel);
		
		addDepartmentBtn = new JButton("Add Department");
		addDepartmentBtn.setBounds(155, 520, 156, 27);
		frmTinderAdmin.getContentPane().add(addDepartmentBtn);
		
		ModifyDepartmentBtn = new JButton("Modify Info");
		ModifyDepartmentBtn.setBounds(51, 445, 156, 27);
		frmTinderAdmin.getContentPane().add(ModifyDepartmentBtn);
		
		deleteDepartmentBtn = new JButton("Delete");
		deleteDepartmentBtn.setBounds(259, 445, 156, 27);
		frmTinderAdmin.getContentPane().add(deleteDepartmentBtn);
		
		clientRadio = new JRadioButton("Client");
		clientRadio.setSelected(true);
		clientRadio.setBounds(105, 372, 77, 27);
		frmTinderAdmin.getContentPane().add(clientRadio);
		
		departmentRadio = new JRadioButton("Department");
		departmentRadio.setBounds(259, 372, 110, 27);
		frmTinderAdmin.getContentPane().add(departmentRadio);
		
		logoutBtn = new JButton("Logout");
		logoutBtn.setFont(new Font("宋体", Font.BOLD, 13));
		logoutBtn.setBounds(359, 13, 81, 26);
		frmTinderAdmin.getContentPane().add(logoutBtn);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBounds(14, 52, 426, 298);
		frmTinderAdmin.getContentPane().add(panel);
		
		label = new JLabel("Online List");
		label.setBounds(0, 0, 88, 18);
		panel.add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 38, 410, 250);
		panel.add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
		
		userList = new ArrayList<String>();
		groupList = new ArrayList<String>();
		
		blockAccountBtn = new JButton("Block Account");
		blockAccountBtn.setBounds(51, 445, 156, 27);
		frmTinderAdmin.getContentPane().add(blockAccountBtn);
		
		unlockAccountBtn = new JButton("Unlock Account");
		unlockAccountBtn.setBounds(260, 447, 155, 23);
		frmTinderAdmin.getContentPane().add(unlockAccountBtn);
		
		deleteAccountBtn = new JButton("Delete Account");
		deleteAccountBtn.setBounds(51, 498, 159, 27);
		frmTinderAdmin.getContentPane().add(deleteAccountBtn);
		
		btnAddAccount = new JButton("Add Account");
		btnAddAccount.setBounds(259, 498, 156, 27);
		frmTinderAdmin.getContentPane().add(btnAddAccount);
		
		resetPasswordBtn = new JButton("Reset Password");
		resetPasswordBtn.setBounds(155, 546, 156, 27);
		frmTinderAdmin.getContentPane().add(resetPasswordBtn);
		
		ModifyDepartmentBtn.setVisible(false);
		addDepartmentBtn.setVisible(false);
		deleteDepartmentBtn.setVisible(false);
		
		buttonGroup=new ButtonGroup();
		buttonGroup.add(departmentRadio);
		buttonGroup.add(clientRadio);
		
		addActionListener(clientRadio);
		addActionListener(departmentRadio);
		
		addActionListener(btnAddAccount);
		addActionListener(deleteAccountBtn);
		addActionListener(blockAccountBtn);
		addActionListener(unlockAccountBtn);
		addActionListener(resetPasswordBtn);
		addActionListener(logoutBtn);
	}
	
	
	/**
	 * add listener
	 * 
	 * @param chckbxNewCheckBox2
	 */
	private void addActionListener(JRadioButton chckbxNewCheckBox2) {
		// TODO Auto-generated method stub
		chckbxNewCheckBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if (event.equals(clientRadio)) {
					//TODO 不知道数据类型实现方式
					btnAddAccount.setEnabled(true);
					btnAddAccount.setVisible(true);
					deleteAccountBtn.setEnabled(true);
					deleteAccountBtn.setVisible(true);
					blockAccountBtn.setEnabled(true);
					blockAccountBtn.setVisible(true);
					unlockAccountBtn.setEnabled(true);
					unlockAccountBtn.setVisible(true);
					resetPasswordBtn.setEnabled(true);
					resetPasswordBtn.setVisible(true);
					
					deleteDepartmentBtn.setEnabled(false);
					deleteDepartmentBtn.setVisible(false);
					addDepartmentBtn.setEnabled(false);
					addDepartmentBtn.setVisible(false);
					ModifyDepartmentBtn.setEnabled(false);
					ModifyDepartmentBtn.setVisible(false);
					
					//添加监听器
					
					addActionListener(btnAddAccount);
					addActionListener(deleteAccountBtn);
					addActionListener(blockAccountBtn);
					addActionListener(resetPasswordBtn);
					
					refreshViewList(userList,1);
				} else if (event.equals(departmentRadio)) {
					//TODO
//					refreshBox(clientList);
					btnAddAccount.setEnabled(false);
					btnAddAccount.setVisible(false);
					deleteAccountBtn.setEnabled(false);
					deleteAccountBtn.setVisible(false);
					blockAccountBtn.setEnabled(false);
					blockAccountBtn.setVisible(false);
					unlockAccountBtn.setEnabled(false);
					unlockAccountBtn.setVisible(false);
					resetPasswordBtn.setEnabled(false);
					resetPasswordBtn.setVisible(false);
					
					deleteDepartmentBtn.setEnabled(true);
					deleteDepartmentBtn.setVisible(true);
					addDepartmentBtn.setEnabled(true);
					addDepartmentBtn.setVisible(true);
					ModifyDepartmentBtn.setEnabled(true);
					ModifyDepartmentBtn.setVisible(true);
					addActionListener(deleteDepartmentBtn);
					addActionListener(addDepartmentBtn);
					addActionListener(ModifyDepartmentBtn);
					
					refreshViewList(groupList,0);
				}
			}
		});
	}
	
	private void addActionListener(JButton bt) {
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getSource().equals(btnAddAccount)) {
					frmTinderAdmin.setVisible(false);
					adminApplication.addAccountWindow.frmTinderAdd.setVisible(true);

				}else if(arg0.getSource().equals(deleteAccountBtn)) {
					
					if(list.getSelectedValuesList().size()==0) {
						adminApplication.infoPrompt.nextMove(frmTinderAdmin);
						adminApplication.infoPrompt.setLabel("You have not choose any user!");
						frmTinderAdmin.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}else {
						
						List<String> selectedUser = list.getSelectedValuesList();
						int flag = 1;
						for (String string : selectedUser) {
							if(string.startsWith("Online")) {
								flag = 0;
								break;
							}
						}
						
						if(flag == 1) {
							Envelope envelope=new Envelope();
							envelope.setSourceName("UIAdminHome");
							String msg="Delete_Account";
							List<Object> emsg=new ArrayList<>();
							emsg.add(msg);
							for(String e:list.getSelectedValuesList()) {
								emsg.add(processSelectedValue(e));
							}
							envelope.setMsg(emsg);
							adminApplication.setEnvelope(envelope);
						}else {
							adminApplication.infoPrompt.nextMove(frmTinderAdmin);
							adminApplication.infoPrompt.setLabel("You can not delete online user!");
							frmTinderAdmin.setVisible(false);
							adminApplication.infoPrompt.frmWarning.setVisible(true);
						}
					}
				}else if(arg0.getSource().equals(blockAccountBtn)) {
					
					if(list.getSelectedValuesList().size()==0) {
						adminApplication.infoPrompt.nextMove(frmTinderAdmin);
						adminApplication.infoPrompt.setLabel("You have not choose any user!");
						frmTinderAdmin.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}else {
						Envelope envelope=new Envelope();
						envelope.setSourceName("UIAdminHome");
						String msg="Block_Account";
						List<Object> emsg=new ArrayList<>();
						emsg.add(msg);
						for(String e:list.getSelectedValuesList()) {
							emsg.add(processSelectedValue(e));
						}
						envelope.setMsg(emsg);
						adminApplication.setEnvelope(envelope);
					}
				}else if(arg0.getSource().equals(unlockAccountBtn)){
					if(list.getSelectedValuesList().size()==0) {
						adminApplication.infoPrompt.nextMove(frmTinderAdmin);
						adminApplication.infoPrompt.setLabel("You have not choose any user!");
						frmTinderAdmin.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}else {
						Envelope envelope=new Envelope();
						envelope.setSourceName("UIAdminHome");
						String msg="Unlock_Account";
						List<Object> emsg=new ArrayList<>();
						emsg.add(msg);
						for(String e:list.getSelectedValuesList()) {
							emsg.add(processSelectedValue(e));
						}
						envelope.setMsg(emsg);
						adminApplication.setEnvelope(envelope);
					}
				}else if(arg0.getSource().equals(resetPasswordBtn)) {
					Envelope envelope=new Envelope();
					envelope.setSourceName("UIAdminHome");
					String msg="ToResetPwdPage";
					List<Object> emsg=new ArrayList<>();
					emsg.add(msg);
					if(list.getSelectedValuesList().size()>2) {
						JOptionPane.showMessageDialog(panel, "选择人数不得超过一人", "错误提示",JOptionPane.WARNING_MESSAGE);
					}else if(list.getSelectedValuesList().size()==0) {
						adminApplication.infoPrompt.nextMove(frmTinderAdmin);
						adminApplication.infoPrompt.setLabel("You have not choose any user!");
						frmTinderAdmin.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}else{
						emsg.add(processSelectedValue(list.getSelectedValuesList().get(0)));
						envelope.setMsg(emsg);
						adminApplication.setEnvelope(envelope);
					}
				}else if(arg0.getSource().equals(deleteDepartmentBtn)) {
					
					List<String> l= list.getSelectedValuesList();
					if(l.isEmpty()){
						JOptionPane.showMessageDialog(frmTinderAdmin, " 请先选择需要删除的组 ", "Warning", 
								JOptionPane.ERROR_MESSAGE);					
					} else {
						String str = "";
						for(String a : l){
							str += a + ",";
						}
						
						str = str.substring(0,str.length() - 1);
						
						Envelope envelope = new Envelope();
						envelope.setSourceName("UIAdminHome");
						String msg = "Begin Delete Department";
						List<Object> emsg = new ArrayList<>();
						emsg.add(msg);
						emsg.add(str);
						envelope.setMsg(emsg);
						adminApplication.setEnvelope(envelope);
					}		

				}else if(arg0.getSource().equals(addDepartmentBtn)) {
					Envelope envelope = new Envelope();
					envelope.setSourceName("UIAdminHome");
					String msg = "Begin Add Department";
					List<Object> emsg = new ArrayList<>();
					emsg.add(msg);
					envelope.setMsg(emsg);
					adminApplication.setEnvelope(envelope);
				}else if(arg0.getSource().equals(ModifyDepartmentBtn)) {
					
					if(list.getSelectedValuesList().size() > 0) {
						Envelope envelope  = new Envelope();
						envelope.setSourceName("UIAdminHome");
						String msg = "ModifyGroup";
						List<Object> emsg = new ArrayList<>();
						emsg.add(msg);
						emsg.add(list.getSelectedValue());
						
						System.err.println("Selected Group: " + list.getSelectedValue());
						
						envelope.setMsg(emsg);
						adminApplication.setEnvelope(envelope);
					}else {
						adminApplication.infoPrompt.nextMove(frmTinderAdmin);
						adminApplication.infoPrompt.setLabel("You have not choose any group!");
						frmTinderAdmin.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}
				}else if(arg0.getSource().equals(logoutBtn)) {
					try {
						adminApplication.adminSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					frmTinderAdmin.setVisible(false);
					adminApplication = null;
					adminApplication = new AdminApplication();
					adminApplication.start();
				}
			}
		});
	}
	
	public void modeifyUserList(String name,int instructionType) {
		if(instructionType == 1) {
			userList.add(name);
		}else {
			userList.remove(name);
		}
	}
	
	public void modeifyGroupList(String name,int instructionType) {
		if(instructionType == 1) {
			groupList.add(name);
		}else {
			groupList.remove(name);
		}
	}
	
	public void refreshViewList(List<String> dataList,int type) {
		
		if(type == 1) {
			userList = dataList;
			clientRadio.setSelected(true);
			departmentRadio.setSelected(false);
		}else {
			groupList = dataList;
			clientRadio.setSelected(false);
			departmentRadio.setSelected(true);
		}
		
		if(list != null) {
			scrollPane.remove(list);
			list = null;
		}
		list = new JList();
		String[] dataListString = new String[dataList.size()];
		
		if(type == 1) {
			int cnt = 0;
			Here: for (String string : dataList) {
				for (String string2 : onlineUserList) {
					if(string.equals(string2)) {
						dataListString[cnt] = "Online:  " + string;
						cnt++;
						continue Here;
					}
				}
				dataListString[cnt] = "Offline: " + string;
				cnt++;
			}
			list.setListData(dataListString);
		}else {
			list.setListData(dataList.toArray(dataListString));
		}
		
		scrollPane.setViewportView(list);
		scrollPane.validate();
		scrollPane.repaint();
		frmTinderAdmin.validate();
		frmTinderAdmin.repaint();
	}
	
	public void setOnlineUserList(List<String> onlineUserList) {
		this.onlineUserList = onlineUserList;
		System.err.println(onlineUserList.size());
	}
	
	private List<String> processSelectedValueList(List<Object> ini){
		List<String> res = new ArrayList<>();
		
		for (Object object : ini) {
			res.add(object.toString().substring(9));
		}
		
		
		return res;
	}
	
	private String processSelectedValue(String ini) {
		return ini.substring(9);
	}
	
	public void setLogState(String logUser,int type) {
		if(type == 1) {
			onlineUserList.add(logUser);
		}else {
			onlineUserList.remove(logUser);
		}
		System.err.println("logUser: " + logUser);
		refreshViewList(userList,1);
	}
	
	public static void addLengthLimit(JTextComponent component) {
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String s = component.getText();
                if(s.length() >= 7) {
                    e.consume();
                }
            }
 
            @Override
            public void keyPressed(KeyEvent e) {
                //do nothing
            }
 
            @Override
            public void keyReleased(KeyEvent e) {
                //do nothing
            }
        });
	}
}
