package ClientGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

import Client.Certificate;
import Client.FileSend;
import ClientGUI.Envelope;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class ChatRoomUI {

	JFrame frmTinderChat;
	private JTextField msgField;
	private JTextField fileChoseField;
	private JTextArea msgTextFiled;
	JLabel reveiverLable;
	private JRadioButton rdbtnMessage;
	private JRadioButton rdbtnFile;
	private JLabel messageLable;
	private JButton selectFileBtn;
	private JButton sendFileBtn;
	private JButton sendMsgBtn;
	private ChatApplication chatApplication;
	private String filePath = "";
	String msg = "";
	Certificate certificate;
	private JButton locationSend;
	private JButton openFolderBtn;
	private JLabel fileLable;
	private JLabel msgExceededLength;
	private JLabel lblNewLabel;
	private JLabel fileListLable;
	private List<String> fileList = new ArrayList<>();
	private FileListDisplayUI fileListUI;
	private JLabel recordlblL;
	private RecordPopUI recordPopwindow = null;

	public void refreshMsgArea(String msg, String sender) {
		this.msg += "\n" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "\n" + sender + " : " + msg;
		msgTextFiled.setText(this.msg);
		frmTinderChat.validate();
		frmTinderChat.repaint();
		readFileHistory();
	}

	/**
	 * 设置接受方标签
	 * 
	 * @param receiver
	 */
	public void setReceiver(String receiver) {
		reveiverLable.setText(receiver);
		frmTinderChat.validate();
		frmTinderChat.repaint();
	}

	public void setFileField(String filePath) {
		this.filePath = filePath;
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
		this.chatApplication = chatApplication2;
		fileListUI = new FileListDisplayUI(chatApplication2);
		fileListUI.frmFileList.setVisible(false);
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
		frmTinderChat = new JFrame();
		frmTinderChat.setResizable(false);
		frmTinderChat.setTitle("Tinder Chat");
		frmTinderChat.setBounds(100, 100, 630, 533);
		frmTinderChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderChat.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 51, 588, 312);
		frmTinderChat.getContentPane().add(scrollPane);

		msgTextFiled = new JTextArea();
		scrollPane.setViewportView(msgTextFiled);

		reveiverLable = new JLabel("Receiver");
		reveiverLable.setFont(reveiverLable.getFont().deriveFont(reveiverLable.getFont().getStyle() | Font.BOLD,
				reveiverLable.getFont().getSize() + 1f));
		reveiverLable.setBounds(14, 20, 206, 18);
		frmTinderChat.getContentPane().add(reveiverLable);

		rdbtnMessage = new JRadioButton("Message");
		rdbtnMessage.setSelected(true);
		rdbtnMessage.setBounds(14, 372, 90, 27);
		frmTinderChat.getContentPane().add(rdbtnMessage);

		rdbtnFile = new JRadioButton("File");
		rdbtnFile.setBounds(110, 372, 66, 27);
		frmTinderChat.getContentPane().add(rdbtnFile);

		messageLable = new JLabel("Message");
		messageLable.setHorizontalAlignment(SwingConstants.CENTER);
		messageLable.setBounds(27, 427, 56, 18);
		frmTinderChat.getContentPane().add(messageLable);

		msgField = new JTextField();
		msgField.setBounds(92, 424, 485, 24);
		frmTinderChat.getContentPane().add(msgField);
		msgField.setColumns(10);
		msgTextFiled.setEditable(false);

		fileChoseField = new JTextField();
		fileChoseField.setBounds(92, 424, 339, 24);
		frmTinderChat.getContentPane().add(fileChoseField);
		fileChoseField.setColumns(10);
		fileChoseField.setVisible(false);

		selectFileBtn = new JButton(" Select");
		selectFileBtn
				.setIcon(new ImageIcon("F:\\eclipse-java-mars-2-win32-x86_64\\workspace\\Tinner\\image\\wenjian.png"));
		selectFileBtn.setBounds(442, 423, 135, 27);
		frmTinderChat.getContentPane().add(selectFileBtn);
		selectFileBtn.setVisible(false);

		sendFileBtn = new JButton("Send");
		sendFileBtn.setBackground(Color.LIGHT_GRAY);
		sendFileBtn
				.setIcon(new ImageIcon("F:\\eclipse-java-mars-2-win32-x86_64\\workspace\\Tinner\\image\\fasong.png"));
		sendFileBtn.setBounds(442, 461, 135, 27);
		frmTinderChat.getContentPane().add(sendFileBtn);

		sendMsgBtn = new JButton("Send");
		sendMsgBtn.setIcon(new ImageIcon("F:\\eclipse-java-mars-2-win32-x86_64\\workspace\\Tinner\\image\\fasong.png"));
		sendMsgBtn.setBounds(442, 461, 135, 27);
		frmTinderChat.getContentPane().add(sendMsgBtn);
		sendFileBtn.setVisible(false);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnMessage);
		buttonGroup.add(rdbtnFile);

		addActionLister(rdbtnFile);
		addActionLister(rdbtnMessage);
		addActionListener(selectFileBtn);
		addActionListener(sendFileBtn);
		addActionListener(sendMsgBtn);

		locationSend = new JButton("Send Location");
		locationSend.setFont(new Font("锟斤拷锟斤拷", Font.PLAIN, 12));
		locationSend
				.setIcon(new ImageIcon("F:\\eclipse-java-mars-2-win32-x86_64\\workspace\\Tinner\\image\\didian.png"));
		locationSend.setBackground(Color.LIGHT_GRAY);
		locationSend.setBounds(442, 373, 135, 27);
		frmTinderChat.getContentPane().add(locationSend);
		addListener(frmTinderChat);
		addActionListener(locationSend);

		openFolderBtn = new JButton("Open Folder");
		openFolderBtn.setFont(new Font("锟斤拷锟斤拷", Font.PLAIN, 13));
		openFolderBtn.setHorizontalAlignment(SwingConstants.LEFT);
		openFolderBtn
				.setIcon(new ImageIcon("F:\\eclipse-java-mars-2-win32-x86_64\\workspace\\Tinner\\image\\Folder.png"));
		openFolderBtn.setBounds(297, 372, 134, 29);
		frmTinderChat.getContentPane().add(openFolderBtn);
		addActionListener(openFolderBtn);

		fileLable = new JLabel("File");
		fileLable.setHorizontalAlignment(SwingConstants.CENTER);
		fileLable.setBounds(14, 427, 72, 18);
		frmTinderChat.getContentPane().add(fileLable);
		fileLable.setVisible(false);
		addKeyListener(msgField);

		msgExceededLength = new JLabel("Exceeded Length");
		msgExceededLength.setForeground(Color.RED);
		msgExceededLength.setFont(new Font("宋体", Font.BOLD, 14));
		msgExceededLength.setBounds(92, 461, 128, 18);
		msgExceededLength.setVisible(false);
		frmTinderChat.getContentPane().add(msgExceededLength);

		fileListLable.setBounds(594, 13, 30, 25);
		frmTinderChat.getContentPane().add(fileListLable);
		addLableListener(fileListLable);

		recordlblL = new JLabel("Record");	}

	/**
	 * 设置单选按钮的点击事件，以切换功能选项
	 * 
	 * @param jRadioButton
	 */
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
					fileLable.setVisible(true);
				} else {
					messageLable.setVisible(true);
					msgField.setVisible(true);
					sendMsgBtn.setVisible(true);

					fileChoseField.setVisible(false);
					selectFileBtn.setVisible(false);
					sendFileBtn.setVisible(false);
					fileLable.setVisible(false);
				}

			}
		});
	}

	/**
	 * 设置不同按钮的点击事件
	 * 
	 * @param jButton
	 */
	public void addActionListener(JButton jButton) {
		jButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object object = e.getSource();
				if (object.equals(selectFileBtn)) {
					// 打开文本选择器
					File publicFile = new File("");
					JFileChooser jfc = new JFileChooser(publicFile);
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.showDialog(new JLabel(), "Choose");
					setFileField(jfc.getSelectedFile().getPath());
				} else if (object.equals(sendFileBtn)) {
					if (!chatApplication.clientHomeUI.curclientList.contains(reveiverLable.getText())) {
						chatApplication.infoPromptwindow.setLabel("Receiver has offline!");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						return;
					}
					if (chatApplication.index == 1) {
						chatApplication.infoPromptwindow.setLabel("You have been blocked,please contact with admin!");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						return;
					}
					// 检测文本域中是否合法
					if (filePath == null || filePath.equals("")) {
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
					File file = new File(filePath);

					// 重置
					filePath = "";
					setFileField(filePath);
					if (!file.exists()) {
						chatApplication.infoPromptwindow.setLabel("The file doesn't exsist!");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						return;
					}
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
					refreshMsgArea(file.getName() + " send successfully! ", chatApplication.username);
				} else if (object.equals(sendMsgBtn)) {
					if (!reveiverLable.getText().contains("group")
							&& !chatApplication.clientHomeUI.curclientList.contains(reveiverLable.getText())) {
						chatApplication.infoPromptwindow.setLabel("Receiver has offline!");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						return;
					}
					if (chatApplication.index == 1) {
						chatApplication.infoPromptwindow
								.setLabel("You have been blocked,please contact with administrator.");
						chatApplication.infoPromptwindow.nextMove(frmTinderChat);
						frmTinderChat.setVisible(false);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						return;
					}
					// 发送消息
					String key = chatApplication.username;
					String msg = msgField.getText();
					Envelope envelope = new Envelope();
					envelope.setSourceName("ChatRoomUI");
					List<Object> objects = new ArrayList<>();
					objects.add(reveiverLable.getText());
					objects.add(msg);
					objects.add(key);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					msgField.setText("");
					refreshMsgArea(msg + " ", chatApplication.username);
				}
			}
		});

	}

	/**
	 * 更新文本域
	 * 
	 * @param msg2
	 */
	public void setMsgArea(String msg2) {
		msgTextFiled.setText(msg2);
		readFileHistory();
		frmTinderChat.validate();
		frmTinderChat.repaint();

	}

	/**
	 * 用于更新文件列表页面
	 */
	public void readFileHistory() {
		File file = new File(chatApplication.path + "/" + reveiverLable.getText() + ".txt");
		List<String> fileList = new ArrayList<>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String lasttime = "";
			String temp = "";
			while (!((temp = bufferedReader.readLine()) == null)) {
				if (temp.contains("Got a file")) {
					String[] cur = temp.split(" ");
					String sender = cur[0];
					String filename = cur[5];
					fileList.add(sender + " " + filename + " " + lasttime);
				}
				lasttime = temp;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileListUI.refreshFileList(fileList);
	}

	public void addLableListener(JLabel jLabel) {
		jLabel.addMouseListener(new MouseListener() {
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
				Object object = e.getSource();
				if (object.equals(fileListLable)) {
					if (fileListUI.frmFileList.isVisible()) {
						fileListUI.frmFileList.setVisible(false);
					} else {
						fileListUI.frmFileList.setVisible(true);
						fileListUI.frmFileList.setLocationRelativeTo(frmTinderChat);
						fileListUI.frmFileList.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					}
				} else if (object.equals(recordlblL)) {
				}

			}
		});
	}

}
