package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import readwrite.ConfigAutoLogin;

@SuppressWarnings("serial")
public class SetDefaultLoginAccount extends JDialog implements ActionListener{
	private JComboBox<String> accountSelectCombo;
	private JButton sureButton,cancelButton;
	private ConfigAutoLogin config;//config���������˻���Ϣд���ļ��� ֻ��Ҫnew ����

	public SetDefaultLoginAccount(JFrame jframe,String[] accouts,ConfigAutoLogin config) {
		this.setLayout(new GridLayout(2, 1));
		this.config=config;
		accountSelectCombo=new JComboBox<String>(accouts);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("ȷ��");
		cancelButton=new JButton("ȡ��");
		sureButton.addActionListener(this);
		cancelButton.addActionListener(this);
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(jframe);
		this.setSize(120, 100);
		this.setResizable(false);
		this.setTitle("�Ե��˺�");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	
	public SetDefaultLoginAccount(String[] accouts,ConfigAutoLogin config) {
		this.setLayout(new GridLayout(2, 1));
		this.config=config;
		accountSelectCombo=new JComboBox<String>(accouts);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("ȷ��");
		cancelButton=new JButton("ȡ��");
		sureButton.addActionListener(this);
		cancelButton.addActionListener(this);
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setSize(120, 100);
		this.setResizable(false);
		this.setTitle("�Ե��˺�");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String a=e.getActionCommand();
		if(a.equals("ȷ��"))
		{	config.write1Name((String)accountSelectCombo.getSelectedItem());
			FlowAppMainFrame.autologin=1;//��ֹд���Զ���¼���˺�
			this.dispose();
		}
		if(a.equals("ȡ��"))
			this.dispose();
	}
}
