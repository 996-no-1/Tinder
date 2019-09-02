package ClientGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.Envelope;

import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ClientLogUI {

	JFrame frmLogPage;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton signInBtn;
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
		this.chatApplication = chatApplication;
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
			e.printStackTrace();
		}
		frmLogPage = new JFrame();
		frmLogPage.setTitle("Log Page");
		frmLogPage.setBackground(SystemColor.activeCaptionText);
		frmLogPage.getContentPane().setBackground(SystemColor.menu);
		frmLogPage.setBounds(100, 100, 450, 330);
		frmLogPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogPage.getContentPane().setLayout(null);

		JLabel lblClient = new JLabel("Client");
		lblClient.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 20));
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

		ButtonGroup mode = new ButtonGroup();

		signInBtn = new JButton("Sign In");
		signInBtn.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 15));
		signInBtn.setForeground(new Color(0, 0, 0));
		signInBtn.setBounds(321, 238, 97, 27);
		frmLogPage.getContentPane().add(signInBtn);

		choseBtn = new JComboBox();
		choseBtn.setModel(new DefaultComboBoxModel(new String[] { "Caesar", "DES", "PlayFair", "3DES" }));
		choseBtn.setSelectedIndex(0);
		choseBtn.setBounds(165, 161, 137, 24);
		frmLogPage.getContentPane().add(choseBtn);

		JLabel lblEncryptionMode = new JLabel("Encryption Mode");
		lblEncryptionMode.setBounds(39, 164, 120, 18);
		frmLogPage.getContentPane().add(lblEncryptionMode);
		addActionListener(signInBtn);
	}
	
	/**
	 * add listener
	 * 
	 * @param saveButton
	 */
	private void addActionListener(JButton saveButton) {
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if (event.equals(signInBtn)) {
					Envelope emsg = new Envelope();
					emsg.setSourceName("ClientLogUI");
					List<Object> msg = new ArrayList<>();
					String password = "";
					char[] words = passwordField.getPassword();
					for (char c : words) {
						password += c;
					}
					msg.add(usernameField.getText().toString());
					msg.add(password);
					emsg.setMsg(msg);
					chatApplication.setEnvelope(emsg);
					chatApplication.setAsMode("RSA");
					chatApplication.setSMode((String) choseBtn.getSelectedItem());
				}

			}

		});
	}
	
	

}
