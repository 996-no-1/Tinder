package adminUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.ChatApplication;
import ClientGUI.Envelope;
import SecurityAlgorithm.MD5;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;

public class UIAddAccount {

	JFrame frmTinderAdd;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton discardBtn;
	private JButton submitBtn;
	private JComboBox ageComboBox;
	private JComboBox genderComboBox;
	
	private AdminApplication adminApplication;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIAddAccount window = new UIAddAccount();
					window.frmTinderAdd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIAddAccount() {
		initialize();
	}
	
	public UIAddAccount(AdminApplication adminApplication) {
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
		
		frmTinderAdd = new JFrame();
		frmTinderAdd.setResizable(false);
		frmTinderAdd.setTitle("Tinder Add");
		frmTinderAdd.setBounds(100, 100, 451, 492);
		frmTinderAdd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderAdd.getContentPane().setLayout(null);
		
		JLabel lblAddNewAccount = new JLabel("Add New Account");
		lblAddNewAccount.setFont(lblAddNewAccount.getFont().deriveFont(lblAddNewAccount.getFont().getStyle() | Font.BOLD, lblAddNewAccount.getFont().getSize() + 2f));
		lblAddNewAccount.setBounds(14, 13, 150, 26);
		frmTinderAdd.getContentPane().add(lblAddNewAccount);
		
		discardBtn = new JButton("Discard");
		discardBtn.setBounds(69, 371, 113, 27);
		frmTinderAdd.getContentPane().add(discardBtn);
		
		submitBtn = new JButton("Submit");
		submitBtn.setBounds(273, 371, 113, 27);
		frmTinderAdd.getContentPane().add(submitBtn);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setBounds(51, 62, 72, 18);
		frmTinderAdd.getContentPane().add(lblUsername);
		
		JLabel lable1 = new JLabel("age");
		lable1.setHorizontalAlignment(SwingConstants.CENTER);
		lable1.setBounds(51, 130, 72, 18);
		frmTinderAdd.getContentPane().add(lable1);
		
		JLabel lblGender = new JLabel("gender");
		lblGender.setHorizontalAlignment(SwingConstants.CENTER);
		lblGender.setBounds(51, 212, 72, 18);
		frmTinderAdd.getContentPane().add(lblGender);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(51, 292, 72, 18);
		frmTinderAdd.getContentPane().add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(143, 59, 243, 24);
		textField.addCaretListener(new TextFieldInputListener());
		frmTinderAdd.getContentPane().add(textField);
		textField.setColumns(10);
		
		ageComboBox = new JComboBox();
		ageComboBox.setModel(new DefaultComboBoxModel(new String[] {"18", "19"}));
		ageComboBox.setBounds(145, 127, 241, 24);
		frmTinderAdd.getContentPane().add(ageComboBox);
		
		genderComboBox = new JComboBox();
		genderComboBox.setModel(new DefaultComboBoxModel(new String[] {"male", "famale"}));
		genderComboBox.setBounds(147, 209, 239, 24);
		frmTinderAdd.getContentPane().add(genderComboBox);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(147, 289, 239, 24);
		frmTinderAdd.getContentPane().add(passwordField);
		
		addActionListener(submitBtn);
		addActionListener(discardBtn);
		addLengthLimit(textField);
		addLengthLimit(passwordField);
	}
	
	private void addActionListener(JButton b) {
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(e.getSource().equals(submitBtn)) {
					
					if(textField.getText().length()==0 || passwordField.getPassword().length==0 || textField.getText().length()==1){//username or password is null
						JOptionPane.showMessageDialog(frmTinderAdd, " 请先填写完整信息 ", "Warning", 
								JOptionPane.ERROR_MESSAGE);
						
					} else {
						Envelope envelope = new Envelope();
						envelope.setSourceName("UIAddAccount");
						List<Object> smsg = new ArrayList<Object>();
						
						String username = "";
						String age = "";
						String gender = "";
						String pswd = "";
						
						username = textField.getText();
						age = (String) ageComboBox.getSelectedItem();
						
						System.err.println("Age: " + age);
						
						gender = (String) genderComboBox.getSelectedItem();
						char[] pawdtmp = passwordField.getPassword();
						for (char c : pawdtmp) {
							pswd += c;
						}
						MD5 md5 = new MD5(pswd);
						String password = md5.processMD5();
						smsg.add(username);
						smsg.add(age);
						smsg.add(gender);
						smsg.add(password);
						envelope.setMsg(smsg);
						adminApplication.setEnvelope(envelope);
					}
					
				} else if(e.getSource().equals(discardBtn)){
					frmTinderAdd.setVisible(false);
					adminApplication.adminHomeWindow.frmTinderAdmin.setVisible(true);
				}
				
				
				
			}
		});
	}
	
	public void addLengthLimit(JTextComponent component) {
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getSource().equals(textField)) {
                	String s = component.getText();
                    if(s.length() >= 10) {
                        e.consume();
                    }
                }else if(e.getSource().equals(passwordField)) {
                	String s = component.getText();
                    if(s.length() >= 20) {
                        e.consume();
                    }
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
	
	class TextFieldInputListener implements CaretListener {
		 
	    @Override
	    public void caretUpdate(CaretEvent e) {
	        JTextField textField = (JTextField) e.getSource();
	        String text = textField.getText();
	        if (text.length() == 0) {
	            return;
	        }
	        char ch = text.charAt(text.length() - 1);
	        if (ch == ' ') {
	            adminApplication.infoPrompt.setLabel("You can not enter space.");
	            adminApplication.infoPrompt.nextMove(frmTinderAdd);
	            adminApplication.infoPrompt.frmWarning.setVisible(true);
	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    textField.setText(text.substring(0, text.length() - 1));
	                }
	            });
	        }
	    }
	 
	}
	
}
