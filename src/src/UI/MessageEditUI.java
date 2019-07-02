package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import data.Envelope;

import javax.swing.JTextField;
import javax.swing.JButton;
/**
 * Used to edit information
 */
public class MessageEditUI {

	JFrame frmMessageEdit;
	private JTextField keyField;
	private JLabel keyLable;
	private JButton sendBtn;
	private ChatApplication chatApplication;
	private JLabel toWhom;
	private TextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MessageEditUI window = new MessageEditUI();
					window.frmMessageEdit.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MessageEditUI() {
		initialize();
	}
	
	/**
	 * Create the application.
	 */
	public MessageEditUI(ChatApplication chatApplication) {
		this.chatApplication=chatApplication;
		initialize();
	}
	/**
	 * Set the current login user
	 * 
	 * @param username
	 */
	
	public void setUsername(String username) {
		if (toWhom != null) {
			toWhom.setVisible(false);
			frmMessageEdit.remove(toWhom);
			
			toWhom=null;
		}
		toWhom = new JLabel(username);
		toWhom.setBounds(385, 16, 72, 18);
		frmMessageEdit.getContentPane().add(toWhom);
		frmMessageEdit.validate();
		frmMessageEdit.repaint();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMessageEdit = new JFrame();
		frmMessageEdit.setTitle("Message Edit");
		frmMessageEdit.setBounds(100, 100, 503, 457);
		frmMessageEdit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMessageEdit.getContentPane().setLayout(null);
		
		JLabel lblEditYourMesssage = new JLabel("Edit Your Messsage");
		lblEditYourMesssage.setFont(lblEditYourMesssage.getFont().deriveFont(lblEditYourMesssage.getFont().getStyle() | Font.BOLD, lblEditYourMesssage.getFont().getSize() + 3f));
		lblEditYourMesssage.setBounds(14, 13, 180, 21);
		frmMessageEdit.getContentPane().add(lblEditYourMesssage);
		
		textArea = new TextArea();
		textArea.setBounds(111, 117, 302, 247);
		frmMessageEdit.getContentPane().add(textArea);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(33, 232, 72, 18);
		frmMessageEdit.getContentPane().add(lblMessage);
		
		keyLable = new JLabel("key");
		keyLable.setHorizontalAlignment(SwingConstants.CENTER);
		keyLable.setBounds(33, 72, 72, 18);
		frmMessageEdit.getContentPane().add(keyLable);
		
		keyField = new JTextField();
		keyField.setBounds(110, 69, 287, 24);
		frmMessageEdit.getContentPane().add(keyField);
		keyField.setColumns(10);
		
	 sendBtn = new JButton("Send");
	 sendBtn.setFont(sendBtn.getFont().deriveFont(sendBtn.getFont().getStyle() | Font.BOLD, sendBtn.getFont().getSize() + 2f));
		sendBtn.setBounds(358, 370, 113, 27);
		frmMessageEdit.getContentPane().add(sendBtn);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(358, 16, 72, 18);
		frmMessageEdit.getContentPane().add(lblTo);
		
		toWhom = new JLabel("username");
		toWhom.setBounds(385, 16, 72, 18);
		frmMessageEdit.getContentPane().add(toWhom);
		addActionListener(sendBtn);
		
	}
	
	/**
	 * Button click event
	 * @param saveButton
	 */
		private void addActionListener(JButton saveButton) {
			// Binding listener for the button
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Dialog
					Object event = e.getSource();
					if (event.equals(sendBtn)) {
						String key=keyField.getText();
						String msg=textArea.getText();
						Envelope envelope=new Envelope();
						envelope.setSourceName("MessageEditUI");
						List<Object> objects=new ArrayList<>();
						objects.add(toWhom.getText());
						objects.add(msg);
						objects.add(key);
						envelope.setMsg(objects);
						chatApplication.setEnvelope(envelope);
						frmMessageEdit.setVisible(false);
						chatApplication.infoPromptwindow.setLabel("Send sussessfully!");
						chatApplication.infoPromptwindow.nextMove(chatApplication.clientChatwindow.frmChat);
						chatApplication.infoPromptwindow.frmWarning.setVisible(true);
						textArea.setText("");
						keyField.setText("");
						
						//new append
						
						//new append
						
						File publicFile = new File("C:/Users/12284/Desktop/system" + "/"+chatApplication.username
						+"/" + toWhom.getText() + ".txt");
						Envelope envelope1 = new Envelope();
						envelope1.setSourceName("ClientChatUI");
						List<Object> objects1 = new ArrayList<>();
						objects1.add(toWhom.getText());
						objects1.add(publicFile);
						envelope1.setMsg(objects1);
						chatApplication.setEnvelope(envelope1);
						
						
					}
				}

			});
		}
	
}
