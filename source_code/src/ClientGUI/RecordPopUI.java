package ClientGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import Client.FileSend;
import Client.Recorder;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Color;

public class RecordPopUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton cancelButton;
	private JButton okButton;
	private RecordPopUI recordPopUI;
	private ChatRoomUI nextMove;
	private ChatApplication chatApplication;
	Recorder recorder;
	/**
	 * Launch the application.
	 */
	public void initiat() {
		try {
			try {
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				UIManager.put("RootPane.setupButtonVisible", false);
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RecordPopUI() {
		initiat();
		setBounds(100, 100, 223, 187);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setIcon(new ImageIcon("F:\\eclipse-java-mars-2-win32-x86_64\\workspace\\Tinner\\image\\bigluyin.png"));
			lblNewLabel.setBounds(24, 0, 148, 110);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblRecording = new JLabel("Recording....");
			lblRecording.setForeground(new Color(255, 0, 0));
			lblRecording.setHorizontalAlignment(SwingConstants.CENTER);
			lblRecording.setBounds(28, 0, 177, 18);
			contentPanel.add(lblRecording);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				okButton = new JButton("Send");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		addButtonListener(cancelButton);
		addButtonListener(okButton);
		
	}
	/**
	 * 
	 * 设置下一个窗口跳转
	 * @param jFrame
	 * @param recordPopUI
	 */
	public void nextmove(ChatRoomUI jFrame,RecordPopUI recordPopUI,ChatApplication chatApplication) {
		this.nextMove=jFrame;
		this.recordPopUI=recordPopUI;
		this.chatApplication=chatApplication;
		//开始录音
		recorder=new Recorder();
		recorder.start("./temp.wav");
	}
	
	

	public void addButtonListener(JButton jButton) {
		jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object object=e.getSource();
				if (object.equals(cancelButton)) {
					recordPopUI.setVisible(false);
					nextMove.frmTinderChat.setVisible(true);
					//关闭录音
					recorder.stop();
					//删除录音文件
					File file=new File("./Template.wav");
					file.delete();
				}else if (object.equals(okButton)) {
					recordPopUI.setVisible(false);
					
					//关闭录音
					recorder.stop();
					
					
					//发送录音文件
					Envelope envelope = new Envelope();
					envelope.setSourceName("ChatRoomUI");
					List<Object> objects = new ArrayList<>();
					List<FileSend> fileSends = new ArrayList<>();
					// read file to send
					File file = new File("./temp.wav");
					try {
						byte[] buffer = null;
						FileInputStream fis = new FileInputStream(file);
						ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
						byte[] b = new byte[1000];
						int n;
						while ((n = fis.read(b)) != -1) {
							bos.write(b, 0, n);
						}
						fis.close();
						bos.close();
						buffer = bos.toByteArray();
						FileSend fileSend = new FileSend();
						fileSend.setFilename(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+".wav");
						fileSend.setTo(nextMove.reveiverLable.getText());
						fileSend.setFile(buffer);
						fileSends.add(fileSend);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					objects.add(fileSends);
					envelope.setMsg(objects);
					chatApplication.setEnvelope(envelope);
					nextMove.refreshMsgArea(file.getName() + " send successfully! ", chatApplication.username);
					
					//删除录音文件
					file.delete();

					nextMove.frmTinderChat.setVisible(true);
				}
				
			}
		});
	}
	
}
