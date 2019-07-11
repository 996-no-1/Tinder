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

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.ChatApplication;
import ClientGUI.Envelope;
import SecurityAlgorithm.MD5;

/**
 * 添加用户的UI界面
 * @author 胡品爵
 * @version 1.0
 */
public class UIAddAccount {

	JFrame frmTinderAdd;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton discardBtn;
	
	private AdminApplication adminApplication;
	/**
	 * Launch the application.
	 */

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
	
	/**
	 * Initialize the action listener of buttons
	 * @param b
	 */
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
	
	/**
	 * Initialize length limitation of text field
	 * @param component
	 */
	
 
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
	
}
