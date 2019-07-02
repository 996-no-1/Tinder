package GUI;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Checkbox;
import java.awt.Color;
import javax.swing.JTree;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.tree.DefaultTreeModel;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.sun.prism.Texture;

import GUI.Envelope;
import GUI.ChatApplication;
import Client.FileSend;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JRadioButton;

public class ClientChatUI {

	JFrame frmChat;
	 JButton msgSendBtn;
	JButton fileBtn;
	private JLabel prompt;
	private JTextArea msgArea;
	private JLabel usernameLable;
	private ChatApplication chatApplication;
	private String msg = "";
	private JButton btnLogout;
	private JCheckBox chckbxSelectAll;
	private List<JCheckBox> clientcheckboxs;
	private JPanel panel = null;
	private JTree tree = null;
	private List<String> clinetList = new ArrayList<>();
	private JRadioButton rdbtnFile;
	private JRadioButton rdbtnNewRadioButton;
	private JLabel lblOnlineList;

	/**
	 * update username lable
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		if (usernameLable != null) {
			usernameLable.setVisible(false);
			frmChat.remove(usernameLable);
		}
		usernameLable = new JLabel(username);
		usernameLable.setBounds(387, 4, 72, 18);
		usernameLable.validate();
		usernameLable.repaint();
		frmChat.getContentPane().add(usernameLable);
	}

	/**
	 * update message area
	 * 
	 * @param newMsg
	 */
	public void updateMsg(String newMsg, String sender) {
		if (msgArea != null) {
			msgArea.setVisible(false);
			frmChat.remove(msgArea);
		}
		msgArea = new JTextArea();
		msgArea.setBackground(new Color(211, 211, 211));
		msgArea.setBounds(24, 439, 504, 286);

		msg += "\n" + sender + " >> " + newMsg;
		msgArea.setText(msg);
		msgArea.setEditable(false);
		msgArea.validate();
		msgArea.repaint();
		msgArea.setVisible(true);
		frmChat.getContentPane().add(msgArea);
		frmChat.validate();
		frmChat.repaint();

	}

	/**
	 * refresh client tree
	 * 
	 * @param clientList
	 */
	public void refreshTree(List<String> clientList) {
		this.clinetList = clientList;
		frmChat.remove(panel);
		if (tree != null) {
			tree.setVisible(false);
			frmChat.remove(tree);
			tree = null;
		}
		tree = new JTree();
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Client On-line") {
			private static final long serialVersionUID = 1L;
			{
				DefaultMutableTreeNode node_1;
				for (String client : clientList) {
					node_1 = new DefaultMutableTreeNode(client);
					add(node_1);
				}
			}
		}));
		tree.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 17));
		tree.setBounds(24, 49, 224, 298);
		tree.validate();
		tree.repaint();
		frmChat.getContentPane().add(tree);
		frmChat.validate();
		frmChat.repaint();
	}

	/**
	 * refresh clinet's check boxes
	 * 
	 * @param clientList
	 */
	public void refreshBox(List<String> clientList) {
		this.clinetList = clientList;
		if (tree != null) {
			tree.setVisible(false);
			frmChat.remove(tree);
			tree = null;

		}
		if (clientcheckboxs!=null) {
			for (JCheckBox jCheckBox : clientcheckboxs) {
			panel.remove(jCheckBox);
		}
		}
		
		clientcheckboxs=new ArrayList<>();
		frmChat.add(panel);
		
		clientcheckboxs = new ArrayList<>();
		int count = 0;
		for (String client : clientList) {
			if (client.equals(chatApplication.username))
				continue;
			JCheckBox jCheckBox = new JCheckBox(client);
			jCheckBox.setBounds(20, 27 + count * 30, 133, 27);
			panel.add(jCheckBox);
			clientcheckboxs.add(jCheckBox);
			count++;
		}
		panel.validate();
		panel.repaint();
		frmChat.validate();
		frmChat.repaint();
	}

	/**
	 * Create the application.
	 */
	public ClientChatUI(ChatApplication chatApplication) {
		this.chatApplication = chatApplication;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmChat.setTitle("91 Chat");
		frmChat.setBounds(100, 100, 560, 797);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Message Zone");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(14, 379, 131, 23);
		frmChat.getContentPane().add(lblNewLabel);

		JLabel lblSender = new JLabel("Sender");
		lblSender.setBounds(24, 408, 60, 18);
		frmChat.getContentPane().add(lblSender);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(153, 408, 72, 18);
		frmChat.getContentPane().add(lblMessage);

		JLabel lblClientPanel = new JLabel("Client Panel");
		lblClientPanel.setHorizontalAlignment(SwingConstants.LEFT);
		lblClientPanel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 17));
		lblClientPanel.setBounds(14, 13, 131, 23);
		frmChat.getContentPane().add(lblClientPanel);

		prompt = new JLabel("Select a receiver!");
		prompt.setHorizontalAlignment(SwingConstants.CENTER);
		prompt.setFont(
				prompt.getFont().deriveFont(prompt.getFont().getStyle() | Font.BOLD, prompt.getFont().getSize() + 2f));
		prompt.setForeground(new Color(255, 0, 0));
		prompt.setBounds(250, 72, 278, 18);
		frmChat.getContentPane().add(prompt);

		fileBtn = new JButton("Send File");
		fileBtn.setBounds(322, 203, 160, 27);
		frmChat.getContentPane().add(fileBtn);

		msgSendBtn = new JButton("Send Message");
		msgSendBtn.setBounds(322, 203, 160, 27);
		frmChat.getContentPane().add(msgSendBtn);
		msgSendBtn.setEnabled(false);
		msgSendBtn.setVisible(false);
		JLabel lblWelcome = new JLabel("welcome");
		lblWelcome.setBounds(322, 4, 60, 18);
		frmChat.getContentPane().add(lblWelcome);

		usernameLable = new JLabel("username");
		usernameLable.setBounds(387, 4, 72, 18);
		frmChat.getContentPane().add(usernameLable);
		addActionListener(fileBtn);
		addActionListener(msgSendBtn);

		btnLogout = new JButton("Logout");
		btnLogout.setVerticalAlignment(SwingConstants.TOP);
		btnLogout.setFont(new Font("ËÎÌå", Font.BOLD, 12));
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBounds(461, 0, 81, 27);
		btnLogout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		frmChat.getContentPane().add(btnLogout);
		addActionListener(btnLogout);

		panel = new JPanel();
		panel.setBackground(SystemColor.window);
		panel.setBounds(14, 49, 271, 317);
		frmChat.getContentPane().add(panel);
		panel.setLayout(null);

		lblOnlineList = new JLabel("Online List");
		lblOnlineList.setBounds(0, 0, 88, 18);
		panel.add(lblOnlineList);

		chckbxSelectAll = new JCheckBox("Select All");
		chckbxSelectAll.setBounds(138, -4, 133, 27);
		panel.add(chckbxSelectAll);
		addActionListener(chckbxSelectAll);

		rdbtnFile = new JRadioButton("File");
		rdbtnFile.setSelected(true);
		rdbtnFile.setBounds(322, 126, 72, 27);
		frmChat.getContentPane().add(rdbtnFile);

		rdbtnNewRadioButton = new JRadioButton("Message");
		rdbtnNewRadioButton.setBounds(403, 126, 94, 27);
		frmChat.getContentPane().add(rdbtnNewRadioButton);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnFile);
		buttonGroup.add(rdbtnNewRadioButton);
		addActionListener(rdbtnFile);
		addActionListener(rdbtnNewRadioButton);
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
				if (event.equals(rdbtnNewRadioButton)) {
					refreshTree(clinetList);
					fileBtn.setEnabled(false);
					fileBtn.setVisible(false);
					msgSendBtn.setEnabled(true);
					msgSendBtn.setVisible(true);
					addActionListener(msgSendBtn);
				} else if (event.equals(rdbtnFile)) {
					refreshBox(clinetList);
					fileBtn.setEnabled(true);
					fileBtn.setVisible(true);
					msgSendBtn.setEnabled(false);
					msgSendBtn.setVisible(false);
					addActionListener(fileBtn);
				}
			}
		});
	}

	/**
	 * add listener
	 * 
	 * @param chckbxNewCheckBox2
	 */
	private void addActionListener(JCheckBox chckbxNewCheckBox2) {
		// TODO Auto-generated method stub
		chckbxNewCheckBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean flag;
				if (chckbxSelectAll.isSelected()) {
					flag = true;
					System.err.println("true");
				} else {
					flag = false;
					System.err.println("false");
				}
				for (JCheckBox checkbox : clientcheckboxs) {
					checkbox.setSelected(flag);
				}
			}
		});

	}

	/**
	 * add listener
	 * 
	 * @param saveButton
	 */
	private void addActionListener(JButton saveButton) {
		// bind
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if (event.equals(fileBtn)) {
					prompt.setText("Select a receiver!");
					String note = null;
					List<String> clients = new ArrayList<>();
					
					File publicFile = new File("C:/Users/12284/Desktop/system/public");
					JFileChooser jfc = new JFileChooser(publicFile);
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.showDialog(new JLabel(), "Ñ¡Ôñ");
					File file = jfc.getSelectedFile();
					for (JCheckBox jCheckBox : clientcheckboxs) {
						if (!jCheckBox.isSelected()) {
							continue;
						}
						note = jCheckBox.getText();
						if (note == null) {
							prompt.setText("The choice is invalid");
							prompt.setVisible(true);
							prompt.setForeground(Color.GREEN);
							return;
						}
						clients.add(note);
					}
					Envelope envelope = new Envelope();
					envelope.setSourceName("ClientChatUI");
					List<Object> objects = new ArrayList<>();
					List<FileSend> fileSends = new ArrayList<>();
					// read file to send
					try {
						byte[] buffer = null;
						FileInputStream fis = new FileInputStream(file);
						ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
						byte[] b = new byte[1000];
						int n;
						while ((n = fis.read(b)) != -1) {
							bos.write(b, 0, n);
						}
						fis.close();
						bos.close();
						buffer = bos.toByteArray();
						for (String to : clients) {
							FileSend fileSend = new FileSend();
							fileSend.setFilename(file.getName());
							fileSend.setTo(to);
							fileSend.setFile(buffer);
							fileSends.add(fileSend);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					objects.add(fileSends);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					prompt.setText("Select a receiver!");
				} else if (event.equals(msgSendBtn)) {
					prompt.setText("Select a receiver!");
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (note == null || note.toString().equals(usernameLable.getText())) {
						prompt.setText("The choice is invalid");
						prompt.setVisible(true);
						prompt.setForeground(Color.GREEN);
						return;
					}
					Envelope envelope = new Envelope();
					envelope.setSourceName("ClientChatUI");
					List<Object> objects = new ArrayList<>();
					objects.add(note.toString());
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					frmChat.setVisible(false);
					chatApplication.messageEditwindow.setUsername(note.toString());
					chatApplication.messageEditwindow.frmMessageEdit.setVisible(true);
					prompt.setText("Select a receiver!");
				} else if (event.equals(btnLogout)) {
					try {
						chatApplication.tcpSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					frmChat.setVisible(false);
					chatApplication = null;
					ChatApplication chatApplication = new ChatApplication();
					chatApplication.start();
				}
			}

		});
	}
}
