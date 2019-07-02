package UI;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ViewHistoryUI {

	JFrame frmViewHistoryPage;
	private JTextArea textArea;
	private JButton btnBack;
	private JLabel usernameLable;
	private JLabel otherclientLable;
	private ChatApplication chatApplication;

	/**
	 * Create the application.
	 */
	public ViewHistoryUI(ChatApplication chatApplication) {
		initialize();
		this.chatApplication=chatApplication;
	}

	public void setUsername(String username,String otherClient) {
		if (usernameLable != null) {
			usernameLable.setVisible(false);
			otherclientLable.setVisible(false);
			frmViewHistoryPage.remove(usernameLable);
			frmViewHistoryPage.remove(otherclientLable);
		}
		usernameLable = new JLabel(username);
		usernameLable.setBounds(347, 13, 72, 18);
		frmViewHistoryPage.getContentPane().add(usernameLable);
		
		otherclientLable = new JLabel(otherClient);
		otherclientLable.setBounds(173, 13, 88, 18);
		frmViewHistoryPage.getContentPane().add(otherclientLable);
		frmViewHistoryPage.validate();
		frmViewHistoryPage.repaint();
	}
	
	
	/**
	 * update message
	 * @param newMsg
	 */
	public void viewHistory(String sender) {
		if (textArea != null) {
			textArea.setVisible(false);
			frmViewHistoryPage.remove(textArea);
			textArea=null;
		}
		textArea = new JTextArea();
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(14, 42, 405, 402);
		try {
			BufferedReader br=new BufferedReader(new FileReader(new File(chatApplication.path+"/"+sender+".txt")));
			String text="";
			String cur="";
			while(!((cur=br.readLine())==null)){
				text+=cur+"\n";
			}
			textArea.setText(text);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		frmViewHistoryPage.getContentPane().add(textArea);
		frmViewHistoryPage.validate();
		frmViewHistoryPage.repaint();
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmViewHistoryPage = new JFrame();
		frmViewHistoryPage.setTitle("View History Page");
		frmViewHistoryPage.setBounds(100, 100, 452, 548);
		frmViewHistoryPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmViewHistoryPage.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(14, 42, 405, 402);
		frmViewHistoryPage.getContentPane().add(textArea);
		
		JLabel lblHistory = new JLabel("History");
		lblHistory.setBounds(14, 13, 72, 18);
		frmViewHistoryPage.getContentPane().add(lblHistory);
		
		btnBack = new JButton("Back");
		btnBack.setFont(btnBack.getFont().deriveFont(btnBack.getFont().getStyle() | Font.BOLD, btnBack.getFont().getSize() + 2f));
		btnBack.setBounds(307, 461, 113, 27);
		frmViewHistoryPage.getContentPane().add(btnBack);
		
		usernameLable = new JLabel("username");
		usernameLable.setBounds(347, 13, 72, 18);
		frmViewHistoryPage.getContentPane().add(usernameLable);
		
		otherclientLable = new JLabel("otherclient");
		otherclientLable.setBounds(173, 13, 88, 18);
		frmViewHistoryPage.getContentPane().add(otherclientLable);
		addActionListener(btnBack);
	}
	
	/**
	 * add listener
	 * @param saveButton
	 */
	    private void addActionListener(JButton saveButton) {
			// bind
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Object object=e.getSource();
					if (object.equals(btnBack)) {
						textArea.setText("");
						otherclientLable.setText(" ");
						frmViewHistoryPage.setVisible(false);
						chatApplication.clientChatwindow.frmChat.setVisible(true);
					}
					
					
					
				}
				});
			
	    }
	    
	
}
