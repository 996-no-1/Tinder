package ClientGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import Client.Certificate;
import Client.FileSend;
import ClientGUI.Envelope;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class ChatRoomUI {

	JFrame frmTinderChat;
	private JTextField msgField;
	private JTextField fileChoseField;
	private JTextArea msgTextFiled;
	private JLabel reveiverLable;
	private JButton selectFileBtn;
	private JButton sendFileBtn;
	private JButton sendMsgBtn;
	private JButton receiveMsgBtn;
	private JButton musicBtn;
	private JButton danceBtn;
	private JButton rapBtn;
	private JButton radioBtn;
	private JButton sendMovieBtn;
	private ChatApplication chatApplication;
	private String filePath="";
	String msg="";
	Certificate certificate;
	
	
	public void refreshMsgArea(String msg,String sender) {
		this.msg+="\n"+sender+" : "+msg;
		msgTextFiled.setText(this.msg);
		frmTinderChat.validate();
		frmTinderChat.repaint();
	}
	
	/**
	 * 设置接收方
	 * 
	 * @param receiver
	 */
	public void setReceiver(String receiver) {
		reveiverLable.setText(receiver);
		frmTinderChat.validate();
		frmTinderChat.repaint();
	}

	public void setFileField(String filePath) {
		this.filePath=filePath;
		fileChoseField.setText(filePath);
		fileChoseField.validate();
		fileChoseField.repaint();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomUI window = new ChatRoomUI();
					window.frmTinderChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatRoomUI() {
		initialize();
	}


	public ChatRoomUI(ChatApplication chatApplication2) {
		initialize();
		this.chatApplication=chatApplication2;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTinderChat = new JFrame();
		frmTinderChat.setResizable(false);
		frmTinderChat.setTitle("Tinder Chat");
		frmTinderChat.setBounds(100, 100, 622, 530);
		frmTinderChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderChat.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 51, 588, 312);
		frmTinderChat.getContentPane().add(scrollPane);
		
		msgTextFiled = new JTextArea();
		scrollPane.setViewportView(msgTextFiled);
		
		reveiverLable = new JLabel("Receiver");
		reveiverLable.setFont(reveiverLable.getFont().deriveFont(reveiverLable.getFont().getStyle() | Font.BOLD, reveiverLable.getFont().getSize() + 1f));
		reveiverLable.setBounds(14, 20, 206, 18);
		frmTinderChat.getContentPane().add(reveiverLable);
		
		rdbtnMessage = new JRadioButton("Message");
		rdbtnMessage.setSelected(true);
		rdbtnMessage.setBounds(14, 373, 93, 27);
		frmTinderChat.getContentPane().add(rdbtnMessage);
		
		rdbtnFile = new JRadioButton("File");
		rdbtnFile.setBounds(151, 373, 157, 27);
		frmTinderChat.getContentPane().add(rdbtnFile);
		
		messageLable = new JLabel("Message");
		messageLable.setBounds(27, 427, 56, 18);
		frmTinderChat.getContentPane().add(messageLable);
		
		msgField = new JTextField();
		msgField.setBounds(92, 424, 480, 24);
		frmTinderChat.getContentPane().add(msgField);
		msgField.setColumns(10);
		msgTextFiled.setEditable(false);
				
		fileChoseField = new JTextField();
		fileChoseField.setBounds(92, 424, 352, 24);
		frmTinderChat.getContentPane().add(fileChoseField);
		fileChoseField.setColumns(10);
		fileChoseField.setVisible(false);		
		
		selectFileBtn = new JButton("Select");
		selectFileBtn.setBounds(458, 423, 114, 27);
		frmTinderChat.getContentPane().add(selectFileBtn);
		selectFileBtn.setVisible(false);
		
		sendFileBtn = new JButton("Send");
		sendFileBtn.setBounds(459, 455, 113, 27);
		frmTinderChat.getContentPane().add(sendFileBtn);
		
		sendMsgBtn = new JButton("Send");
		sendMsgBtn.setBounds(459, 455, 113, 27);
		frmTinderChat.getContentPane().add(sendMsgBtn);
		sendFileBtn.setVisible(false);
		
		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(rdbtnMessage);
		buttonGroup.add(rdbtnFile);
		
		addActionLister(rdbtnFile);
		addActionLister(rdbtnMessage);
		addActionListener(selectFileBtn);
		addActionListener(sendFileBtn);
		addActionListener(sendMsgBtn);
		addListener(frmTinderChat);
	}
	
	public void addActionLister(JRadioButton jRadioButton) {
		jRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnFile.isSelected()) {
					messageLable.setVisible(false);
					msgField.setVisible(false);
					sendMsgBtn.setVisible(false);
					
					fileChoseField.setVisible(true);
					selectFileBtn.setVisible(true);
					sendFileBtn.setVisible(true);
				}else {
					fileChoseField.setVisible(false);
					selectFileBtn.setVisible(false);
					sendFileBtn.setVisible(false);
				}
				
			}
		});
	}
	
	
	public void addActionListener(JButton jButton) {
		jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object object=e.getSource();
				if (object.equals(selectFileBtn)) {
					//打开文件选择器
					File publicFile = new File("");
					JFileChooser jfc = new JFileChooser(publicFile);
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.showDialog(new JLabel(), "选择");
					setFileField(jfc.getSelectedFile().getPath());
				}else if (object.equals(sendFileBtn)) {
					if (chatApplication.index==1) {
						chatApplication.infoPromptwindow.setLabel("You have been blocked,please contact with administrator.");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.setVisible(true);
						return;
					}
					//发送文件
					if (filePath==null||filePath.equals("")) {
						chatApplication.infoPromptwindow.setLabel("Please select a file!");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						return;
					}
					Envelope envelope = new Envelope();
					envelope.setSourceName("ChatRoomUI");
					List<Object> objects = new ArrayList<>();
					List<FileSend> fileSends = new ArrayList<>();
					// read file to send
					File file=new File(filePath);
					//重置
					filePath="";
					setFileField(filePath);
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
						FileSend fileSend = new FileSend();
						fileSend.setFilename(file.getName());
						fileSend.setTo(reveiverLable.getText());
						fileSend.setFile(buffer);
						fileSends.add(fileSend);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					objects.add(fileSends);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					fileChoseField.setText("");
					refreshMsgArea(file.getName()+" send successfully! "+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), chatApplication.username);
				}else if (object.equals(sendMsgBtn)) {
					if (chatApplication.index==1) {
						chatApplication.infoPromptwindow.setLabel("You have been blocked,please contact with administrator.");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.setVisible(true);
						return;
					}
					//发送信息
					objects.add(reveiverLable.getText());
					objects.add(msg);
					objects.add(key);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					msgField.setText("");
					refreshMsgArea(msg+" "+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), chatApplication.username);
				}
				
			}
		});
		
	}
	
	public void addListener(JFrame jFrame) {
		jFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				chatApplication.chatRoomUIMap.remove(reveiverLable.getText());
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void setMsgArea(String msg2) {
		msgTextFiled.setText(msg2);
		frmTinderChat.validate();
		frmTinderChat.repaint();
	}
	
}
