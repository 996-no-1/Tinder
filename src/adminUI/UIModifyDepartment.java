package adminUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import ClientGUI.Envelope;

import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class UIModifyDepartment {

	JFrame frmTinderModify;
	
	private AdminApplication adminApplication;
	private JScrollPane scrollPane1;
	private JList extraMemberList;
	private JList insideMemberList;
	private JLabel departmentNameLable;
	private JPanel panel;
	private JLabel lblInsideGroupMember;
	private JLabel lblAddGroupMember;
	private JScrollPane scrollPane2;
	private JButton DiscardButton;
	private JButton AddButton;
	private JButton SubmitButton;
	
	private List<String> extraMemberDataList;
	private List<String> insideMemberDataList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIModifyDepartment window = new UIModifyDepartment();
					window.frmTinderModify.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIModifyDepartment() {
		initialize();
	}
	
	public UIModifyDepartment(AdminApplication adminApplication) {
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
		
		frmTinderModify = new JFrame();
		frmTinderModify.setResizable(false);
		frmTinderModify.setTitle("Tinder Modify");
		frmTinderModify.setBounds(100, 100, 457, 598);
		frmTinderModify.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTinderModify.getContentPane().setLayout(null);
		
		departmentNameLable = new JLabel("Department Name");
		departmentNameLable.setFont(departmentNameLable.getFont().deriveFont(departmentNameLable.getFont().getStyle() | Font.BOLD, departmentNameLable.getFont().getSize() + 1f));
		departmentNameLable.setBounds(14, 13, 145, 27);
		frmTinderModify.getContentPane().add(departmentNameLable);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(14, 75, 411, 388);
		frmTinderModify.getContentPane().add(panel);
		panel.setLayout(null);
		
		lblInsideGroupMember = new JLabel("Inside Group Member");
		lblInsideGroupMember.setBounds(10, 195, 146, 26);
		panel.add(lblInsideGroupMember);
		
		lblAddGroupMember = new JLabel("Extra Group Member");
		lblAddGroupMember.setBounds(0, 0, 128, 26);
		panel.add(lblAddGroupMember);
		
		scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(10, 36, 391, 153);
		panel.add(scrollPane1);
		
		extraMemberList = new JList();
		scrollPane1.setViewportView(extraMemberList);
		
		scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(10, 230, 391, 148);
		panel.add(scrollPane2);
		
		insideMemberList = new JList();
		scrollPane2.setViewportView(insideMemberList);
		
		DiscardButton = new JButton("Discard");
		DiscardButton.setBounds(14, 508, 93, 23);
		frmTinderModify.getContentPane().add(DiscardButton);
		
		AddButton = new JButton("Add");
		AddButton.setBounds(332, 508, 93, 23);
		frmTinderModify.getContentPane().add(AddButton);
		
		SubmitButton = new JButton("Back");
		SubmitButton.setBounds(172, 508, 93, 23);
		frmTinderModify.getContentPane().add(SubmitButton);
		
		addActionListener(AddButton);
		addActionListener(DiscardButton);
		addActionListener(SubmitButton);
		
	}
	
	private void addActionListener(JButton bt) {
		bt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource().equals(SubmitButton)) {
					Envelope envelope = new Envelope();
					envelope.setSourceName("UIModifyDepartment");
					List<Object> msg = new ArrayList<>();
					msg.add("Submit");
					envelope.setMsg(msg);
					adminApplication.setEnvelope(envelope);
				}else if(e.getSource().equals(AddButton)) {
					
					if(extraMemberList.getSelectedValuesList().size() > 0) {
						List<Object> selectedValueList = extraMemberList.getSelectedValuesList();
						Envelope envelope = new Envelope();
						envelope.setSourceName("UIModifyDepartment");
						
						List<Object> smsg = new ArrayList<>();
						smsg.add("Add");
						for (Object obj : selectedValueList) {
							smsg.add(obj);
						}
						
						envelope.setMsg(smsg);
						
						adminApplication.setEnvelope(envelope);
					}else {
						
						adminApplication.infoPrompt.nextMove(frmTinderModify);
						adminApplication.infoPrompt.setLabel("You have not choose any user!");
						frmTinderModify.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}
				}else if(e.getSource().equals(DiscardButton)) {
					
					if(insideMemberList.getSelectedValuesList().size() > 0) {
						List<Object> selectedValueList = insideMemberList.getSelectedValuesList();
						Envelope envelope = new Envelope();
						envelope.setSourceName("UIModifyDepartment");
						
						List<Object> smsg = new ArrayList<>();
						smsg.add("Discard");
						for (Object obj : selectedValueList) {
							smsg.add(obj);
						}
						
						envelope.setMsg(smsg);
						
						adminApplication.setEnvelope(envelope);
					}else {
						adminApplication.infoPrompt.nextMove(frmTinderModify);
						adminApplication.infoPrompt.setLabel("You have not choose any user!");
						frmTinderModify.setVisible(false);
						adminApplication.infoPrompt.frmWarning.setVisible(true);
					}
				}
			}
			
		});
	}
	//insideMemberList
	public void refreshInsideMemberList(List<String> insideMemberDataList) {
		this.insideMemberDataList = insideMemberDataList;
		
		if(insideMemberList != null) {
			scrollPane2.remove(insideMemberList);
			insideMemberList = null;
		}
		
		insideMemberList = new JList();
		String[] dataListTmp = new String[insideMemberDataList.size()];
		insideMemberList.setListData(insideMemberDataList.toArray(dataListTmp));
		
		scrollPane2.setViewportView(insideMemberList);
		scrollPane2.validate();
		scrollPane2.repaint();
		frmTinderModify.validate();
		frmTinderModify.repaint();
	}
	
	public void refreshExtraMemberList(List<String> extraMemberDataList) {
		this.extraMemberDataList = extraMemberDataList;
		
		if(extraMemberList != null) {
			scrollPane1.remove(extraMemberList);
			extraMemberList = null;
		}
		
		extraMemberList = new JList();
		String[] dataListTmp = new String[extraMemberDataList.size()];
		extraMemberList.setListData(extraMemberDataList.toArray(dataListTmp));
		
		scrollPane1.setViewportView(extraMemberList);
		scrollPane1.validate();
		scrollPane1.repaint();
		frmTinderModify.validate();
		frmTinderModify.repaint();
	}
}
