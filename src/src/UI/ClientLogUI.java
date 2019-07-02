package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import data.Envelope;

import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
/**
 * Login and register ui
 *
 */
public class ClientLogUI {

	JFrame frmLogPage;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton submitBtn;
	private JRadioButton signInSelect;
	private JRadioButton signupSelect;
	private ChatApplication chatApplication;
	private JComboBox choseBtn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogUI window = new ClientLogUI();
					window.frmLogPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientLogUI() {
		initialize();
	}

	/**
	 * Create the application.
	 */
	public ClientLogUI(ChatApplication chatApplication) {
		this.chatApplication=chatApplication;
		System.err.println("h");
		initialize();
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
		frmLogPage = new JFrame();
		frmLogPage.setResizable(false);
		frmLogPage.setTitle("Log Page");
		frmLogPage.setBackground(SystemColor.activeCaptionText);
		frmLogPage.getContentPane().setBackground(SystemColor.menu);
		frmLogPage.setBounds(100, 100, 450, 330);
		frmLogPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogPage.getContentPane().setLayout(null);
		
		JLabel lblClient = new JLabel("Client");
		lblClient.setFont(new Font("微软雅黑", Font.BOLD, 20));
		lblClient.setForeground(UIManager.getColor("Button.disabledForeground"));
		lblClient.setBounds(176, 26, 73, 18);
		frmLogPage.getContentPane().add(lblClient);
		
		usernameField = new JTextField();
		usernameField.setBackground(UIManager.getColor("Button.light"));
		usernameField.setBounds(165, 74, 137, 24);
		frmLogPage.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(UIManager.getColor("Button.light"));
		passwordField.setBounds(165, 116, 137, 24);
		frmLogPage.getContentPane().add(passwordField);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setBounds(75, 77, 72, 18);
		frmLogPage.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setBounds(75, 119, 72, 18);
		frmLogPage.getContentPane().add(lblPassword);
		
		ButtonGroup mode=new ButtonGroup();
		signupSelect = new JRadioButton("Sign Up");
		signupSelect.setSelected(true);
		signupSelect.setBounds(239, 204, 91, 27);
		frmLogPage.getContentPane().add(signupSelect);
		
		signInSelect = new JRadioButton("Sigh In");
		signInSelect.setSelected(true);
		signInSelect.setBounds(123, 204, 97, 27);
		frmLogPage.getContentPane().add(signInSelect);
		mode.add(signInSelect);
		mode.add(signupSelect);
		
		submitBtn = new JButton("Submit");
		submitBtn.setFont(new Font("微软雅黑", Font.BOLD, 15));
		submitBtn.setForeground(new Color(0, 0, 0));
		submitBtn.setBounds(321, 238, 97, 27);
		frmLogPage.getContentPane().add(submitBtn);
		addActionListener(submitBtn);
		
		 choseBtn = new JComboBox();
		choseBtn.setModel(new DefaultComboBoxModel(new String[] {"AES"}));
		choseBtn.setSelectedIndex(0);
		choseBtn.setBounds(165, 161, 137, 24);
		frmLogPage.getContentPane().add(choseBtn);
		
		JLabel lblEncryptionMode = new JLabel("Encryption Mode");
		lblEncryptionMode.setBounds(39, 164, 120, 18);
		frmLogPage.getContentPane().add(lblEncryptionMode);
	}
	
	
	/**
	 * click event
	 * @param saveButton
	 */
		private void addActionListener(JButton saveButton) {
			// bind
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Object event = e.getSource();

					if (event.equals(submitBtn)) {
						Envelope emsg=new Envelope();
						emsg.setSourceName("ClientLogUI");
						List<Object> msg=new ArrayList<>();
						String password="";
						char[] words=passwordField.getPassword();
						for (char c : words) {
							password+=c;
						}
						msg.add(usernameField.getText().toString());
						msg.add(password);
						//设置当前用户登录形式
						if (signupSelect.isSelected()) {
							msg.add(0);
						}else {
							msg.add(1);
						}emsg.setMsg(msg);
						//投放信件
						chatApplication.setEnvelope(emsg);
					}

				}

			});
		}
}
