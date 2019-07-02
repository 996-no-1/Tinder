package UI;

import java.awt.EventQueue;

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

import java.awt.Color;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.tree.DefaultTreeModel;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import data.Envelope;
import data.FileSend;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClientChatUI {

	JFrame frmChat;
	private JButton msgSendBtn;
	private JButton fileBtn;
	private JLabel prompt; 
	private JTextArea msgArea;
	private JTree tree;
	private JLabel usernameLable;
	private ChatApplication chatApplication;
	private String msg="";
	private JButton saveRecordButton;
	private JButton viewHistoryBtn;
	private JButton btnLogout;
	/**
	 * update message
	 * @param newMsg
	 */
	public void updateMsg(String newMsg,String sender) {
		if (msgArea != null) {
			msgArea.setVisible(false);
			frmChat.remove(msgArea);
			msgArea=null;
		}
		 msgArea = new JTextArea();
		 msgArea.setFont(msgArea.getFont().deriveFont(msgArea.getFont().getStyle() | Font.BOLD, msgArea.getFont().getSize() + 1f));
		msgArea.setBackground(new Color(211, 211, 211));
		msgArea.setBounds(24, 439, 504, 286);
		
		msg+="\n"+sender+">>"+newMsg;
		msgArea.setText(msg);
		msgArea.setVisible(true);
		msgArea.setEditable(false);
		frmChat.getContentPane().add(msgArea);
		frmChat.validate();
		frmChat.repaint();
	}
	/**
	 * refresh
	 * @param clientList
	 */
	public void refreshTree(List<String> clientList) {
		if (tree != null) {
			tree.setVisible(false);
			frmChat.remove(tree);
			tree=null;
		}
		tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			 	new DefaultMutableTreeNode("Client On-line") {
					private static final long serialVersionUID = 1L;
					{
			 			DefaultMutableTreeNode node_1;
			 			for (String client : clientList) {
							node_1=new DefaultMutableTreeNode(client);
							add(node_1);
						}
			 		}
			 	}
			 ));
			tree.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 17));
			tree.setBounds(24, 49, 224, 298);
			frmChat.getContentPane().add(tree);
			frmChat.validate();
			frmChat.repaint();
	}
	public void setUsername(String username) {
		if (usernameLable != null) {
			usernameLable.setVisible(false);
			frmChat.remove(usernameLable);
		}
		usernameLable = new JLabel(username);
		usernameLable.setBounds(392, 2, 72, 20);
		frmChat.getContentPane().add(usernameLable);
		frmChat.validate();
		frmChat.repaint();
	}
	
	/**
	 * Create the application.
	 */
	public ClientChatUI(ChatApplication chatApplication) {
		this.chatApplication=chatApplication;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmChat.setResizable(false);
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
		
		
		prompt = new JLabel("Select receiver first!");
		prompt.setHorizontalAlignment(SwingConstants.CENTER);
		prompt.setFont(prompt.getFont().deriveFont(prompt.getFont().getStyle() | Font.BOLD, prompt.getFont().getSize() + 2f));
		prompt.setForeground(new Color(255, 0, 0));
		prompt.setBounds(257, 87, 285, 27);
		frmChat.getContentPane().add(prompt);
		
		 fileBtn = new JButton("Send File");
		fileBtn.setBounds(307, 142, 160, 27);
		frmChat.getContentPane().add(fileBtn);
		
		 msgSendBtn = new JButton("Send Message");
		msgSendBtn.setBounds(307, 203, 160, 27);
		frmChat.getContentPane().add(msgSendBtn);
		
		saveRecordButton = new JButton("Save Record");
		saveRecordButton.setEnabled(false);
		saveRecordButton.setBounds(307, 320, 160, 27);
		saveRecordButton.setVisible(false);
		frmChat.getContentPane().add(saveRecordButton);
		
		JLabel lblWelcome = new JLabel("Welcome");
		lblWelcome.setBackground(Color.WHITE);
		lblWelcome.setBounds(332, 3, 60, 18);
		frmChat.getContentPane().add(lblWelcome);
		
		viewHistoryBtn = new JButton("View History");
		viewHistoryBtn.setBounds(307, 264, 160, 27);
		frmChat.getContentPane().add(viewHistoryBtn);
		
		addActionListener(saveRecordButton);
		addActionListener(fileBtn);
		addActionListener(msgSendBtn);
		addActionListener(viewHistoryBtn);
		
		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("ËÎÌå", Font.BOLD, 11));
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBounds(465, 2, 75, 23);
		btnLogout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		frmChat.getContentPane().add(btnLogout);
		addActionListener(btnLogout);
		
	}
/**
 * add listener
 * @param saveButton
 */
    private void addActionListener(JButton saveButton) {
		// bind
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if (event.equals(fileBtn)) {
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (note == null||note.toString().equals(usernameLable.getText())) {
						prompt.setText("The choice is invalid");
						prompt.setVisible(true);
						prompt.setForeground(Color.GREEN);
						return;
					}
					File publicFile = new File("C:/Users/12284/Desktop/system/public");
					JFileChooser jfc = new JFileChooser(publicFile);
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.showDialog(new JLabel(), "Ñ¡Ôñ");
					File file = jfc.getSelectedFile();
					Envelope envelope = new Envelope();
					envelope.setSourceName("ClientChatUI");
					List<Object> objects = new ArrayList<>();
					FileSend fileSend = new FileSend();
					fileSend.setFilename(file.getName());
					fileSend.setTo(note.toString());
					// read
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
						fileSend.setFile(buffer);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					objects.add(fileSend);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					frmChat.setVisible(false);
					chatApplication.infoPromptwindow.setLabel("Send successfully!");
					chatApplication.infoPromptwindow.nextMove(frmChat);
					chatApplication.infoPromptwindow.frmWarning.setVisible(true);
				} else if (event.equals(saveRecordButton)) {
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (note == null||note.toString().equals(usernameLable.getText())) {
						prompt.setText("The choice is invalid!");
						prompt.setVisible(true);
						prompt.setForeground(Color.GREEN);
						return;
					}
					File publicFile = new File("C:/Users/12284/Desktop/system" + "/"+usernameLable.getText()
					+"/" + note.toString() + ".txt");
					Envelope envelope = new Envelope();
					envelope.setSourceName("ClientChatUI");
					List<Object> objects = new ArrayList<>();
					objects.add(note.toString());
					objects.add(publicFile);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					frmChat.setVisible(false);
					chatApplication.infoPromptwindow.setLabel("Save successfully!");
					chatApplication.infoPromptwindow.nextMove(frmChat);
					chatApplication.infoPromptwindow.frmWarning.setVisible(true);
				}else if (event.equals(msgSendBtn)) {
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (note == null||note.toString().equals(usernameLable.getText())) {
						prompt.setText("The choice is invalid!");
						prompt.setVisible(true);
						prompt.setForeground(Color.GREEN);
						return;
					}
					frmChat.setVisible(false);
					chatApplication.messageEditwindow.setUsername(note.toString());
					chatApplication.messageEditwindow.frmMessageEdit.setVisible(true);
				}else if (event.equals(viewHistoryBtn)) {
					DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (note == null||note.toString().equals(usernameLable.getText())) {
						prompt.setText("The choice is invalid!");
						prompt.setVisible(true);
						prompt.setForeground(Color.GREEN);
						return;
					}
					chatApplication.viewHistorywindow.setUsername(chatApplication.username, note.toString());
					chatApplication.viewHistorywindow.viewHistory(note.toString());
					frmChat.setVisible(false);
					chatApplication.viewHistorywindow.frmViewHistoryPage.setVisible(true);
				}else if (event.equals(btnLogout)) {
					try {
						chatApplication.tcpSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frmChat.setVisible(false);
					chatApplication=null;
					ChatApplication chatApplication = new ChatApplication();
					chatApplication.start();
				}
			}

		});
	}
}
