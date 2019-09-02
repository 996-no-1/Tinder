package adminUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.Envelope;

import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
/**
 * 添加群组的UI界面
 * @author 胡品爵
 * @version 1.0
 */
public class UIAddDepartment {

	JFrame frmNewDepartment;
	private JTextField departmentName;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JButton cancelButton;
	private JButton submitButton;
	private JLabel lblNewLabel;
	private JPanel panel_1;
	private JLabel lblNewDepartment;
	private JList list;
	private JScrollPane scrollPane;
	
	private AdminApplication adminApplication;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIAddDepartment window = new UIAddDepartment();
					
					List<String> a = new ArrayList<String>();
					a.add("admin");
					a.add("user");
					a.add("fuck");
					
					window.refreshList(a);
					window.frmNewDepartment.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIAddDepartment() {
		initialize();
	}
	
	public UIAddDepartment(AdminApplication adminApplication) {
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
		
		frmNewDepartment = new JFrame();
		frmNewDepartment.setResizable(false);
		frmNewDepartment.setTitle("Tinder Add");
		frmNewDepartment.setBounds(100, 100, 372, 553);
		frmNewDepartment.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNewDepartment.getContentPane().setLayout(null);
		
		lblNewLabel_1 = new JLabel("Group Name");
		lblNewLabel_1.setBounds(51, 71, 72, 18);
		panel.add(lblNewLabel_1);
		
		departmentName = new JTextField();
		departmentName.setBounds(167, 68, 150, 24);
		departmentName.addCaretListener(new TextFieldInputListener());
		panel.add(departmentName);
		departmentName.setColumns(10);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(29, 466, 113, 27);
		panel.add(cancelButton);
		
		submitButton = new JButton("Submit");
		submitButton.setBounds(204, 466, 113, 27);
		panel.add(submitButton);
		
		lblNewLabel = new JLabel("Client Name");
		lblNewLabel.setBounds(29, 111, 106, 24);
		panel.add(lblNewLabel);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(14, 139, 326, 314);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 306, 262);
		panel_1.add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
		
		lblNewDepartment = new JLabel("New Department");
		lblNewDepartment.setFont(new Font("SimSun-ExtB", Font.BOLD, 15));
		lblNewDepartment.setBounds(14, 13, 188, 27);
		panel.add(lblNewDepartment);
		
		
		addActionListener(cancelButton);
		addActionListener(submitButton);
		addLengthLimit(departmentName);
	}
	
	/**
	 * Initialize the action listener of buttons
	 * @param b
	 */
	private void addActionListener(JButton addButton) {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(submitButton)) {
					Envelope envelope = new Envelope();
					envelope.setSourceName("UIAddDepartment");
					String groupName = departmentName.getText().toString();
					List<Object> selectedUsers = new ArrayList<Object>();
					selectedUsers.add(groupName);
					for (Object object : list.getSelectedValuesList()) {
						selectedUsers.add((String) object);
					}
					
					envelope.setMsg(selectedUsers);
					adminApplication.setEnvelope(envelope);
				}
			}
		});
	}
	
	/**
	 * Refresh display list
	 * @param userNameList
	 */
	@SuppressWarnings("unchecked")
	public void refreshList(List<String> userNameList) {
		if(list != null) {
			scrollPane.remove(list);
			list = null;
		}
		
		list = new JList<String>();
		String[] userNameListString = new String[userNameList.size()];
		list.setListData( userNameList.toArray(userNameListString));
		
		scrollPane.setViewportView(list);
		scrollPane.validate();
		scrollPane.repaint();
		frmNewDepartment.validate();
		frmNewDepartment.repaint();
	}
	
	/**
	 * Initialize length limitation of text field
	 * @param component
	 */
	public void addLengthLimit(JTextComponent component) {
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getSource().equals(departmentName)) {
                	String s = component.getText();
                    if(s.length() >= 10) {
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
	
	/**
	 * Tool class for 'addLengthLimit' method
	 * @author 15360
	 *
	 */
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
	            adminApplication.infoPrompt.nextMove(frmNewDepartment);
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
