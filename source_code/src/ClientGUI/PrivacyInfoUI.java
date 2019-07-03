package ClientGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Client.ClientInfo;
import SecurityAlgorithm.MD5;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class PrivacyInfoUI {

	JFrame frmTinderPrivacy;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JComboBox genderComboBox;
	private JComboBox agecomboBox_1;
	private ChatApplication chatApplication;
	private JTextField noteField;
	private JFrame nextWindow = null;
	private ClientInfo ci = null;
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrivacyInfoUI window = new PrivacyInfoUI();
					window.frmTinderPrivacy.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * next window
	 * 
	 * @param next
	 */
	public void nextMove(JFrame next) {
		nextWindow = next;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Create the application.
	 */
	public PrivacyInfoUI() {
		initialize();
	}

	public PrivacyInfoUI(ChatApplication chatApplication) {
		initialize();
		this.chatApplication=chatApplication;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTinderPrivacy = new JFrame();
		frmTinderPrivacy.setTitle("Tinder Privacy");
		frmTinderPrivacy.setBounds(100, 100, 456, 438);
		frmTinderPrivacy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderPrivacy.getContentPane().setLayout(null);
		
		JLabel lblPrivacy = new JLabel("Privacy");
		lblPrivacy.setFont(lblPrivacy.getFont().deriveFont(lblPrivacy.getFont().getStyle() | Font.BOLD));
		lblPrivacy.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrivacy.setBounds(14, 13, 85, 27);
		frmTinderPrivacy.getContentPane().add(lblPrivacy);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setBounds(94, 74, 72, 18);
		frmTinderPrivacy.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setBounds(94, 208, 72, 18);
		frmTinderPrivacy.getContentPane().add(lblPassword);
		
		JLabel lblAge = new JLabel("age");
		lblAge.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAge.setBounds(94, 120, 64, 18);
		frmTinderPrivacy.getContentPane().add(lblAge);
		
		JLabel lblGender = new JLabel("gender");
		lblGender.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGender.setBounds(94, 163, 72, 18);
		frmTinderPrivacy.getContentPane().add(lblGender);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(180, 208, 118, 24);
		frmTinderPrivacy.getContentPane().add(passwordField);
		
		genderComboBox = new JComboBox();
		genderComboBox.setModel(new DefaultComboBoxModel(new String[] {"male", "famale"}));
		genderComboBox.setBounds(180, 160, 118, 24);
		frmTinderPrivacy.getContentPane().add(genderComboBox);
		
		agecomboBox_1 = new JComboBox();
		agecomboBox_1.setModel(new DefaultComboBoxModel(new String[] {"18", "19"}));
		agecomboBox_1.setBounds(180, 117, 118, 24);
		frmTinderPrivacy.getContentPane().add(agecomboBox_1);
		
		usernameField = new JTextField();
		usernameField.setEditable(false);
		usernameField.setBounds(180, 71, 118, 24);
		frmTinderPrivacy.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		JLabel lblNote = new JLabel("note");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setBounds(94, 262, 72, 18);
		frmTinderPrivacy.getContentPane().add(lblNote);
		
		noteField = new JTextField();
		noteField.setBounds(180, 259, 118, 24);
		frmTinderPrivacy.getContentPane().add(noteField);
		noteField.setColumns(10);
	}
	private void addActionListener(JButton saveButton) {
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if (event.equals(btnSubmit)) {
					
					Envelope emsg = new Envelope();
					emsg.setSourceName("");
					List<Object> msg = new ArrayList<>();
					
					String password = "";//password
					char[] words = passwordField.getPassword();
					for (char c : words) {
						password += c;
					}
					password = new MD5(password).processMD5();
					
					//gender
					String gender = "";
					gender = (String) genderComboBox.getSelectedItem();
					
					//age
					int age = 0;
					age = (int) agecomboBox_1.getSelectedItem();
					
					ci.setUsername(usernameField.getText().toString());
					ci.setMD5(password);
					ci.setGender(gender);
					ci.setAge(age);
					ci.setNote(noteField.getText());
					
					msg.add(ci);
					emsg.setMsg(msg);
					chatApplication.setEnvelope(emsg);
					
					JumpPage();
					
				} else if(event.equals(btnDiscard)){
					JumpPage();
				}

			}

		});
	}
	
	/**
	 * jump to next page
	 */
	private void JumpPage(){
		// TODO jump to next UI
	}
	
}
