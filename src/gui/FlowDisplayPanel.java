package gui;

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

import readwrite.ResourcePath;
import readwrite.UseInfo;
import tool.MyLogger;
import tool.RequestSender;
import tool.TimerControl;

@SuppressWarnings("serial")
public class FlowDisplayPanel extends JPanel implements ActionListener {
	public JTextField usedText,totalText,remainText,nameText;//������ʾ��������ֵ
	public JLabel statusLabel;
	public JButton logoutButton;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	public  Timer timer;
//	private WebStatus ws;
	//������ʾҪ��Ҫ����ģʽ,����ʱ��false
    public FlowDisplayPanel(boolean simplify) {
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
    	this.add(new JLabel("M"),constraints);
    	
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
    	this.add(new JLabel("M"),constraints);
    	
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
    	this.add(new JLabel("M"),constraints);
    	
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
    	
    	if (!ButtonAreaPanel.isWebLost) { 
			timer = new Timer(TimerControl.FAST_MODE, this);
			FlowAppMainFrame.timeControl.addTimer(timer);
//			if (UseInfo.isLogin)
//				timer.setDelay(TimerControl.FAST_MODE);
			timer.start();
		}
    	else
		{
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("�Ѷ���");
			logoutButton.setEnabled(false);			
		}
	}
    
    //������ʾ������
    public void setTexts()
    {	
    	if(!ButtonAreaPanel.isWebLost)
    	{
    		this.nameText.setText(UseInfo.userName);
			this.usedText.setText(""+UseInfo.used);
			this.totalText.setText(""+UseInfo.total);
			this.remainText.setText(""+UseInfo.remain);
			this.setLoginStatus();
    	}
    	
    }
    
    //����״̬��ǩ��ʾ������ �����ѵ�¼,δ��¼,�û������������,����������,�Ѷ���
    public void setLoginStatus()
    {
    	if(!ButtonAreaPanel.isWebLost)	
		{	
    		if(UseInfo.isLogin)
			{	statusLabel.setForeground(Color.green);
				statusLabel.setText("�ѵ�¼");
				logoutButton.setEnabled(true);
			}
			else 
			{
				statusLabel.setForeground(Color.red);
				statusLabel.setText("δ��¼");
				logoutButton.setEnabled(false);
			}
//			else {
//				statusLabel.setForeground(Color.blue);
//				statusLabel.setText("�û������������");
//				logoutButton.setEnabled(false);
//			}
			if(UseInfo.useOut)
			{
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("����������");
				logoutButton.setEnabled(false);
			}
			if(FlowAppMainFrame.autoLogin)
			{	
				logoutButton.setEnabled(false);
			}
			else {
				logoutButton.setEnabled(true);
				FlowAppMainFrame.autoLogin=false;
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
		this.setLoginStatus();
//		//�Զ��л��˺ŵ�¼
//		if(ws.useOut&&FlowAppMainFrame.autoSelect)
//		{
//			//��¼���˼����˺�
//			int time=0;
//			
//			//����ǵ�¼��,�����˳�����Ϣ
//			if(ws.loginStatus==1)
//				try {
//					new RequestSender().logout(ResourcePath.SERVERPATH);
//				} catch (IOException e1) {
//					MyLogger.fatal(getClass(), e1.getMessage());
//					e1.printStackTrace();
//				}
//			//�ñ���˺ŵ�¼
//			while(time<am.usernameList.size())		
//			{
//				String key=am.usernameList.get(time);
//				try {
//					new RequestSender().login(ResourcePath.SERVERPATH, 
//							am.accountMap.get(key));
//				} catch (IOException e1) {
//					MyLogger.fatal(getClass(), e1.getMessage());
//					e1.printStackTrace();
//				}
//				time++;
//				//��������˶��Ҹ��˺�����û����Ļ�,���˳�
//				if(ws.loginStatus==1&&!ws.useOut)
//					break;
//			}
//			
//			//���ȫ���˺�������,����û����,�������û������������
//			if(time==am.usernameList.size()&&ws.loginStatus==0)
//			{		JOptionPane.showMessageDialog(this, "�Ѿ�û�п��õ��˺���");
//					FlowAppMainFrame.autoSelect=false;}
//			
//			//���ȫ���˺�������,����û����
//			if(time==am.usernameList.size()&&ws.useOut)
//			{	JOptionPane.showMessageDialog(this, "��ӵ�е��˺�����ȫ��������");
//				FlowAppMainFrame.autoSelect=false;
//			}
//			
//		}
		
		//���¾�����������
		if(FlowAppMainFrame.simplifyDialog!=null)
			FlowAppMainFrame.simplifyDialog.setTexts();
		this.setTexts();
		if(e.getSource()==logoutButton)
			try {
				RequestSender.logout(ResourcePath.SERVERPATH);					
				this.setTexts();
				ButtonAreaPanel.loginButton.setEnabled(true);			
			} catch (IOException e1) {
				MyLogger.fatal(getClass(), e1.getMessage()+" �����˳���Ϣʧ��");
				JOptionPane.showMessageDialog(null, "�����˳���Ϣʧ��");
			}
		
		//����˳���¼�İ�ť
		if(UseInfo.isLogin)
			logoutButton.setEnabled(true);
	}
}
