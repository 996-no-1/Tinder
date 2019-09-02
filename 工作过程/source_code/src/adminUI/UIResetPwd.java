package adminUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.Envelope;

import javax.swing.JButton;
/**
 * 管理员重设用户密码UI界面
 * @author 胡品爵
 * @version 1.0
 */
public class UIResetPwd {

	JFrame frmTinderReset;
	private JTextField pwd;
	private JLabel label;
	private JLabel lblNewPassword;
	private JButton CancenBtn;
	private JButton SubmitBtn;
	
	private AdminApplication adminApplication;
	public String username;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIResetPwd window = new UIResetPwd();
					window.frmTinderReset.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIResetPwd() {
		initialize();
	}
	
	public UIResetPwd(AdminApplication adminApplication) {
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
		
		frmTinderReset = new JFrame();
		frmTinderReset.setResizable(false);
		frmTinderReset.setTitle("Tinder Reset");
		frmTinderReset.setBounds(100, 100, 450, 300);
		frmTinderReset.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderReset.getContentPane().setLayout(null);
		
		label = new JLabel("New Department");
		label.setFont(new Font("SimSun-ExtB", Font.BOLD, 15));
		label.setBounds(14, 13, 188, 27);
		frmTinderReset.getContentPane().add(label);
		
		lblNewPassword = new JLabel("New Password");
		lblNewPassword.setBounds(53, 83, 116, 18);
		frmTinderReset.getContentPane().add(lblNewPassword);
		
		pwd = new JPasswordField();
		pwd.setColumns(10);
		pwd.setBounds(202, 80, 188, 24);
		frmTinderReset.getContentPane().add(pwd);
		
		CancenBtn = new JButton("Cancel");
		CancenBtn.setBounds(70, 173, 113, 27);
		frmTinderReset.getContentPane().add(CancenBtn);
		
		SubmitBtn = new JButton("Submit");
		SubmitBtn.setBounds(245, 173, 113, 27);
		frmTinderReset.getContentPane().add(SubmitBtn);
		addActionListener(CancenBtn);
		addActionListener(SubmitBtn);
		addLengthLimit(pwd);
	}
	
	
	/**
	 * Initialize the action listener of buttons
	 * @param bt
	 */
	private void addActionListener(JButton bt) {
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getSource().equals(SubmitBtn)) {
					Envelope envelope=new Envelope();
					envelope.setSourceName("UIResetPwd");
					String msg="ResetPasswd";
					List<Object> emsg=new ArrayList<>();
					emsg.add(msg);
					emsg.add(username);
					emsg.add(pwd.getText());
					envelope.setMsg(emsg);
					adminApplication.setEnvelope(envelope);
				}else if(arg0.getSource().equals(CancenBtn)) {
					frmTinderReset.setVisible(false);
					adminApplication.adminHomeWindow.frmTinderAdmin.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * Initialize length limitation of text field
	 * @param component
	 */
	public void addLengthLimit(JTextComponent component) {
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getSource().equals(pwd)) {
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
}
