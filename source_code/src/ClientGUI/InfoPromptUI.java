package ClientGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Window;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
/**
 * alert Ui
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class InfoPromptUI extends JDialog {

	JFrame frmWarning;
	private JButton btnAccept;
	private JLabel info;
	private JFrame nextWindow = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InfoPromptUI window = new InfoPromptUI();
					window.frmWarning.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InfoPromptUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWarning = new JFrame();
		frmWarning.setResizable(false);
		frmWarning.setTitle("Warning");
		frmWarning.setType(Type.POPUP);
		frmWarning.setBounds(100, 100, 446, 248);
		frmWarning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWarning.getContentPane().setLayout(null);

		btnAccept = new JButton("Accept");
		btnAccept.setBounds(151, 142, 113, 27);
		frmWarning.getContentPane().add(btnAccept);

		JLabel lblWarning = new JLabel("Warning!");
		lblWarning.setForeground(new Color(255, 0, 0));
		lblWarning.setFont(lblWarning.getFont().deriveFont(lblWarning.getFont().getStyle() | Font.BOLD,
				lblWarning.getFont().getSize() + 1f));
		lblWarning.setBounds(174, 51, 72, 18);
		frmWarning.getContentPane().add(lblWarning);

		info = new JLabel("Please check your account!");
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setBounds(14, 99, 404, 18);
		frmWarning.getContentPane().add(info);

		addActionListener(btnAccept);
	}

	/**
	 * modify content in alert window
	 * 
	 * @param content
	 */
	public void setLabel(String content) {
		info.setText(content);
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
	 * add listener
	 * 
	 * @param saveButton
	 */
	private void addActionListener(JButton saveButton) {
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object event = e.getSource();
				if (event.equals(btnAccept)) {
					frmWarning.setVisible(false);
					nextWindow.setVisible(true);
					nextWindow = null;
				}

			}

		});
	}

}
