package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JEditorPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import GUI.Envelope;

import javax.swing.JTextField;
import java.awt.Button;
import javax.swing.JButton;

public class MessageEditUI {

	JFrame frmMessageEdit;
	private JTextField keyField;
	private JLabel keyLable;
	private JComboBox<String> algorithmBox;
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
		this.chatApplication = chatApplication;
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

			toWhom = null;
		}
		toWhom = new JLabel(username);
		toWhom.setBounds(385, 16, 72, 18);
		frmMessageEdit.getContentPane().add(toWhom);
		frmMessageEdit.validate();
		frmMessageEdit.repaint();
	}

	public void setAlgorithmBox(String algorithm) {
		if (algorithmBox != null) {
			algorithmBox.setVisible(false);
			frmMessageEdit.remove(algorithmBox);

			algorithmBox = null;
		}
		algorithmBox = new JComboBox<String>();
		algorithmBox.addItem(algorithm);
		algorithmBox.setEditable(false);
		algorithmBox.setBounds(111, 82, 287, 24);
		algorithmBox.setVisible(true);
		frmMessageEdit.getContentPane().add(algorithmBox);
		frmMessageEdit.validate();
		frmMessageEdit.repaint();

	}

	/**
	 * set the visible of key field
	 * 
	 * @param visible
	 */
	public void setKeyFiled(boolean visible) {
		if (keyField != null) {
			keyField.setVisible(false);
			frmMessageEdit.remove(keyField);

			keyField = null;
		}
		keyField = new JTextField();
		keyField.setBounds(111, 129, 287, 24);
		frmMessageEdit.getContentPane().add(keyField);
		keyField.setColumns(10);
		keyField.setEditable(visible);
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
		lblEditYourMesssage.setFont(lblEditYourMesssage.getFont().deriveFont(
				lblEditYourMesssage.getFont().getStyle() | Font.BOLD, lblEditYourMesssage.getFont().getSize() + 3f));
		lblEditYourMesssage.setBounds(14, 13, 180, 21);
		frmMessageEdit.getContentPane().add(lblEditYourMesssage);

		textArea = new TextArea();
		textArea.setBounds(111, 174, 302, 190);
		frmMessageEdit.getContentPane().add(textArea);

		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(33, 246, 72, 18);
		frmMessageEdit.getContentPane().add(lblMessage);

		JLabel lblAlgorithm = new JLabel("Algorithm");
		lblAlgorithm.setBounds(33, 85, 72, 18);
		frmMessageEdit.getContentPane().add(lblAlgorithm);

		keyLable = new JLabel("key");
		keyLable.setHorizontalAlignment(SwingConstants.CENTER);
		keyLable.setBounds(33, 132, 72, 18);
		frmMessageEdit.getContentPane().add(keyLable);

		keyField = new JTextField();
		keyField.setBounds(111, 129, 287, 24);
		frmMessageEdit.getContentPane().add(keyField);
		keyField.setColumns(10);

		sendBtn = new JButton("Send");
		sendBtn.setBounds(358, 370, 113, 27);
		frmMessageEdit.getContentPane().add(sendBtn);
		addActionListener(sendBtn);

		JLabel lblTo = new JLabel("To");
		lblTo.setBounds(362, 16, 16, 18);
		frmMessageEdit.getContentPane().add(lblTo);
	}

	/**
	 * Button click event
	 * 
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
					String key = keyField.getText();
					String msg = textArea.getText();
					Envelope envelope = new Envelope();
					envelope.setSourceName("MessageEditUI");
					List<Object> objects = new ArrayList<>();
					objects.add(toWhom.getText());
					objects.add(msg);
					objects.add(key);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					frmMessageEdit.setVisible(false);
					chatApplication.infoPromptwindow.setLabel("Send sussessfully!");
					chatApplication.infoPromptwindow.nextMove(chatApplication.clientChatwindow.frmChat);
					chatApplication.infoPromptwindow.frmWarning.setVisible(true);
					keyField.setText("");
					textArea.setText("");
				}
			}

		});
	}
}
