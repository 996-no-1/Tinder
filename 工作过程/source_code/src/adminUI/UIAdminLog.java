package adminUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPasswordField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.Envelope;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
/**
 * 管理员登录UI界面
 * @author 胡品爵
 * @version 1.0
 */
public class UIAdminLog {

	JFrame frmAdminlog;
	private JPasswordField passwordField;
	private JButton logInBtn;
	private AdminApplication adminApplication;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIAdminLog window = new UIAdminLog();
					window.frmAdminlog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIAdminLog() {
		initialize();
	}
	
	public UIAdminLog(AdminApplication adminApplication) {
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
		
		frmAdminlog = new JFrame();
		frmAdminlog.setResizable(false);
		frmAdminlog.setTitle("Tinder Admin Log");
		frmAdminlog.setBounds(100, 100, 450, 300);
		frmAdminlog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdminlog.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Admin");
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(lblNewLabel.getFont().getStyle() | Font.BOLD, lblNewLabel.getFont().getSize() + 2f));
		lblNewLabel.setBounds(180, 25, 50, 31);
		frmAdminlog.getContentPane().add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("宋体", Font.PLAIN, 16));
		lblPassword.setBounds(63, 112, 85, 34);
		frmAdminlog.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(193, 115, 168, 31);
		frmAdminlog.getContentPane().add(passwordField);
		
		addActionListener(logInBtn);
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
				if(e.getSource().equals(logInBtn)) {
					Envelope envelope = new Envelope();
					envelope.setSourceName("UIAdminLog");
					List<Object> smsg = new ArrayList<Object>();
					String pswd = "";
					char[] tmp = passwordField.getPassword();
					for (char c : tmp) {
						pswd += c;
					}
					smsg.add(pswd);
					envelope.setMsg(smsg);
					adminApplication.setEnvelope(envelope);
					
					System.err.println("Btn Log press");
				}
			}
		});
	}

}
