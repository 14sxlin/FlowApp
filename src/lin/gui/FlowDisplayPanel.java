package lin.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import lin.readwrite.AccountManager;
import lin.readwrite.ConfigAutoLogin;
import lin.readwrite.ReadStatus;
import lin.readwrite.ResourcePath;
import lin.tool.SendLoginRequest;

@SuppressWarnings("serial")
public class FlowDisplayPanel extends JPanel implements ActionListener {
	/*
	 * �ǵ��˳���ʱ���ʱ��ص�
	 */
	public JTextField usedText,totalText,remainText,nameText;//������ʾ��������ֵ
	public JLabel statusLabel;
	public JButton logoutButton;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	public  Timer timer;
	private ReadStatus readStatus;//�������ɾ��
	private AccountManager am;
	//������ʾҪ��Ҫ����ģʽ,����ʱ��false
    public FlowDisplayPanel(boolean simplify,AccountManager am) {
    	this.am = am;
    	//����չʾ��
    	this.setBorder(new TitledBorder("����"));
    	gridbag=new GridBagLayout();
    	this.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("�û���:"), constraints);    	
    	constraints=new GridBagConstraints(1, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	nameText=new JTextField();
    	nameText.setEditable(false);
    	this.add(nameText, constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("��ʹ������:"),constraints);    	
    	constraints=new GridBagConstraints(1, 2, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	usedText=new JTextField();
    	usedText.setForeground(Color.blue);
    	usedText.setEditable(false);
    	this.add(usedText, constraints);    	
    	constraints=new GridBagConstraints(2, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("Byte"),constraints);
    	
    	constraints=new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("������:"), constraints);    	
    	constraints=new GridBagConstraints(1, 1, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	totalText=new JTextField();
    	totalText.setForeground(Color.ORANGE);
    	totalText.setEditable(false);
    	this.add(totalText, constraints);    	
    	constraints=new GridBagConstraints(2, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("Byte"),constraints);
    	
    	constraints=new GridBagConstraints(0, 3, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	this.add(new JLabel("ʣ������:"), constraints);
    	
    	constraints=new GridBagConstraints(1, 3, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	remainText=new JTextField();
    	remainText.setForeground(Color.red);
    	remainText.setEditable(false);
    	this.add(remainText, constraints);
    	
    	constraints=new GridBagConstraints(2, 3, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	this.add(new JLabel("Byte"),constraints);
    	
    	constraints=new GridBagConstraints(0, 4, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	statusLabel=new JLabel("δ��¼",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	this.add(statusLabel, constraints);
    	
    	constraints=new GridBagConstraints(0, 5, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	logoutButton=new JButton("�˳���¼");
    	logoutButton.addActionListener(this);
    	logoutButton.setEnabled(false);
    	this.add(logoutButton, constraints);
    	
    	if (!ReadStatus.WebLost) {
			//��ȡ��ҳ����Ϣ
			try {
				readStatus = new ReadStatus();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "��ʼ��readStatusʧ��");
				//			if(timer!=null)
				//				timer.stop();
			}
			timer = new Timer(1000, this);
			if (ReadStatus.loginStatus == 1)
				timer.setDelay(readStatus.timer.getDelay());
			timer.start();
		}else
		{
//System.out.println("weblost");
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("�Ѷ���");
			logoutButton.setEnabled(false);			
		}
	}
    
    //������ʾ������
    public void setTexts(ReadStatus state)
    {	if(!ReadStatus.WebLost)
    	{
    		this.nameText.setText(state.userName);
    		this.usedText.setText(state.usedAmount);
    		this.totalText.setText(state.totalAmount);
    		this.remainText.setText(state.remainAmount);
    		this.setLoginStatus(state.loginStatus,state.useOut);
    	}
    }
    public void setTexts(String userName,String usedAmount,String totalAmount,
    		String remainAmount,int loginStatus,boolean useOut)
    {	if(!ReadStatus.WebLost)
    {
    	this.nameText.setText(userName);
    	this.usedText.setText(usedAmount);
    	this.totalText.setText(totalAmount);
    	this.remainText.setText(remainAmount);
    	this.setLoginStatus(loginStatus,useOut);
    }
    }
    
    //����״̬��ǩ��ʾ������ �����ѵ�¼,δ��¼,�û������������,����������,�Ѷ���
    public void setLoginStatus(int loginstatus,boolean useOut)
    {
    	if(!ReadStatus.WebLost)	
		{	if(loginstatus==1)
			{	statusLabel.setForeground(Color.green);
				statusLabel.setText("�ѵ�¼");
				logoutButton.setEnabled(true);
			}
			else if(loginstatus==0)
			{
				statusLabel.setForeground(Color.red);
				statusLabel.setText("δ��¼");
				logoutButton.setEnabled(false);
			}else {
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("�û������������");
				logoutButton.setEnabled(false);
			}
			if(useOut)
			{
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("����������");
				logoutButton.setEnabled(false);
			}
			if(ButtonAreaPanel.autoLogin)
			{	logoutButton.setEnabled(false);
			}
			else {	logoutButton.setEnabled(true);
						ButtonAreaPanel.autoLogin=false;
			}
		}
		else {
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("�Ѷ���");
			logoutButton.setEnabled(false);
			ButtonAreaPanel.loginButton.setEnabled(false);
			
		}
    }
   
	public void actionPerformed(ActionEvent e) {
		
		//������������
		this.setLoginStatus(ReadStatus.loginStatus, ReadStatus.useOut);
		//�Զ��л��˺ŵ�¼
		if(ReadStatus.useOut&&FlowAppMainFrame.autoSelect)
		{
			//��¼���˼����˺�
			int time=0;
			
			//����ǵ�¼��,�����˳�����Ϣ
			if(ReadStatus.loginStatus==1)
				try {
					new SendLoginRequest().logout(ResourcePath.SERVERPATH);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//�ñ���˺ŵ�¼
			while(time<am.usernameList.size())		
			{
				String key=am.usernameList.get(time);
				try {
					new SendLoginRequest().login(ResourcePath.SERVERPATH, 
							am.accountMap.get(key));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				time++;
				//��������˶��Ҹ��˺�����û����Ļ�,���˳�
				if(ReadStatus.loginStatus==1&&!ReadStatus.useOut)
					break;
			}
			
			//���ȫ���˺�������,����û����,�������û������������
			if(time==am.usernameList.size()&&ReadStatus.loginStatus==0)
			{		JOptionPane.showMessageDialog(this, "�Ѿ�û�п��õ��˺���");
					FlowAppMainFrame.autoSelect=false;}
			
			//���ȫ���˺�������,����û����
			if(time==am.usernameList.size()&&ReadStatus.useOut)
			{	JOptionPane.showMessageDialog(this, "��ӵ�е��˺�����ȫ��������");
				FlowAppMainFrame.autoSelect=false;
			}
			
		}
		
		//���¾�����������
		if(FlowAppMainFrame.simplifyDialog!=null)
			FlowAppMainFrame.simplifyDialog.setTexts(ReadStatus.userName,ReadStatus.usedAmount, 
					ReadStatus.remainAmount,ReadStatus.loginStatus);
		this.setTexts(ReadStatus.userName,ReadStatus.usedAmount, 
				ReadStatus.totalAmount, ReadStatus.remainAmount,ReadStatus.loginStatus, ReadStatus.useOut);
		if(e.getSource()==logoutButton)
			try {
				if(FlowAppMainFrame.autologin!=1&&FlowAppMainFrame.autologin!=2)
				{	new ConfigAutoLogin().write_1Name(ReadStatus.userName);
					FlowAppMainFrame.autologin=-1;
				}
				new SendLoginRequest().logout(ResourcePath.SERVERPATH);					
				this.setTexts(ReadStatus.userName,ReadStatus.usedAmount, 
						ReadStatus.totalAmount, ReadStatus.remainAmount,ReadStatus.loginStatus,ReadStatus.useOut);
				ButtonAreaPanel.loginButton.setEnabled(true);			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "�����˳���Ϣʧ��");
			}
		
		//����˳���¼�İ�ť
		if(ReadStatus.loginStatus==0)
			logoutButton.setEnabled(false);
	}
}
