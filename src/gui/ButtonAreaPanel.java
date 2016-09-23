package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import org.apache.http.client.ClientProtocolException;

import readwrite.AccountManager;
import readwrite.Configure;
import readwrite.ResourcePath;
import readwrite.UseInfo;
import tool.MusicPlayerWithDialog;
import tool.MyLogger;
import tool.ParamsAdapter;
import tool.RequestSender;
import tool.TimerControl;

@SuppressWarnings("serial")
public class ButtonAreaPanel extends JPanel implements ActionListener, ItemListener {
//	private WebStatus ws;
	public static boolean isWebLost = false;
	private JFrame parent;
	private String lastParam;
	public  JComboBox<String> accountSelectCombo;
	public  static JButton loginButton;
	public  JButton addUser;
	public  JCheckBox autoLoginChBox;
	public  JCheckBox autoSelectChBox;
	private JButton alarmButton;
	public AddDialog accountDialog;
	public static boolean alarmhasSet=false;
	public String params;
	public Timer timer;
	public MusicPlayerWithDialog music;
	public SetDefaultLoginAccount setDefaultLoginAccount;
	private AccountManager am;
	//��¼�ǲ��ǳ�ʼ����ʱ��ļ������Զ�ѡ��
	public 	int i=0;
	public ButtonAreaPanel(JFrame parent,AccountManager am) {
		this.am = am;
//		this.ws = ws;
		this.parent = parent;
		this.setLayout(new GridLayout(3, 2));
		alarmButton=new JButton("��������");
		alarmButton.setActionCommand("��������");
		alarmButton.addActionListener(this);
		
		addUser=new JButton("����˻�");
		addUser.addActionListener(e->{
			FlowAppMainFrame.inside=true;
			try {
				accountSelectCombo.removeAllItems();
				new AddDialog(parent,am);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			if(accountSelectCombo!=null)
				accountSelectCombo.removeAllItems();
			for(String item:am.usernameList)
				accountSelectCombo.addItem(item);
		});
		
		loginButton=new JButton("��¼");
		loginButton.addActionListener(e->{
			String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
			params=am.accountMap.get(temp);
			try {
				if(RequestSender.login(ResourcePath.SERVERPATH, 
						ParamsAdapter.String2List(params)).contains("false"))
				{
					JOptionPane.showMessageDialog(parent, "���͵�¼��Ϣʧ��,�����˳��ѵ�¼���˺�");
				};
			}catch (IOException e1) {
				JOptionPane.showMessageDialog(parent, "���͵�¼��Ϣʧ��");
			}
		});
		
		autoLoginChBox=new JCheckBox("�Զ���¼");
		autoSelectChBox=new JCheckBox("�Զ��л�");
		autoLoginChBox.addItemListener(this);
		autoSelectChBox.addItemListener(this);
		accountSelectCombo=new JComboBox<String>(am.usernames);
		accountSelectCombo.addActionListener(this);
		this.setBorder(new TitledBorder("������"));
		this.setLayout(new GridLayout(3, 2));
		this.add(alarmButton);
		this.add(addUser);
		this.add(accountSelectCombo);
		this.add(loginButton);
		this.add(autoLoginChBox);
		this.add(autoSelectChBox);
		
		//����ʱ����
		timer=new Timer(TimerControl.FAST_MODE, this);
		FlowAppMainFrame.timeControl.addTimer(timer);
		if(!isWebLost)
			timer.start();
		else loginButton.setEnabled(false);
//System.out.println("loginState="+ReadStatus.loginStatus);
		if(UseInfo.isLogin)
			loginButton.setEnabled(false);
		
//System.out.println("Class= "+this.getClass().getResource("").getPath());
//System.out.println("ClassLoad="+this.getClass().getClassLoader().getResource("").getPath());
//System.out.println("SystemLoad="+ClassLoader.getSystemResource("").getPath());
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			UseInfo.Refresh();
		} catch (ClientProtocolException e3) {
			//TODO
			e3.printStackTrace();
			isWebLost=true;
		} catch (IOException e3) {
			//TODO
			e3.printStackTrace();
			isWebLost=true;
		}
		String action=e.getActionCommand();
		if(action==null)
			action="none";
		
		//��ʼ���Զ���¼��ť
		if(FlowAppMainFrame.autoLogin)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//��ʼ���Զ��л���ť
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//�������ѵļ��
		if(alarmhasSet&&
				UseInfo.used>=AlarmSettingDialog.alarmAmount)
		{
			try {
				if(AlarmSettingDialog.indexOfMusic>=0&&AlarmSettingDialog.indexOfMusic<=8)
				{
					music = new MusicPlayerWithDialog(getClass().getResourceAsStream("/sounds/"+AlarmSettingDialog.musicName)
							,true,ButtonAreaPanel.alarmhasSet);
				}
				else {
					music=new MusicPlayerWithDialog(new File(AlarmSettingDialog.musicName),true,ButtonAreaPanel.alarmhasSet);
				}
			} catch (IOException e1) {
				MyLogger.fatal(ButtonAreaPanel.class,e1.getMessage());
				e1.printStackTrace();
			}
			music.play();
			music.showControlPanel(parent, "��������");
		}
		
		
		//��������,����֮�����ȡ����������
		if(action.equals("��������"))
		{	FlowAppMainFrame.inside=true;
			try {
				new AlarmSettingDialog(parent, UseInfo.total);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				MyLogger.setLogger(this.getClass());
				MyLogger.fatal(e1.getMessage());
			}			
		}
		if(alarmhasSet)
		{	alarmButton.setText("ȡ������");
			alarmButton.setActionCommand("ȡ������");
		}
		if(action.equals("ȡ������")||!alarmhasSet)
		{	alarmButton.setText("��������");
			alarmhasSet=false;
			alarmButton.setActionCommand("��������");
		}
		
//		//��¼��ť�ļ��
		if(!isWebLost)
		{	if(UseInfo.isLogin)
			{	loginButton.setEnabled(false);
			}
			else {
				loginButton.setEnabled(true);
			}
		}
		
		//�Զ���¼�ļ��,����ǳ���,�Զ����͵�¼��Ϣ
		if(autoLoginChBox.isSelected()&&UseInfo.isLogin)
		{
			String defaultAccount = null;
			defaultAccount=Configure.GetValueByKey("defaultUser");
			if(defaultAccount==null||defaultAccount.trim().equals(""))
				defaultAccount=Configure.GetValueByKey("lastLogin");
			if(defaultAccount==null||defaultAccount.trim().equals(""))
			{	
				MyLogger.debug(getClass(), "in button panel");
				setDefaultLoginAccount=new SetDefaultLoginAccount(this,am.usernames);
				defaultAccount=Configure.GetValueByKey("defaultUser");
			}
			try {
				params=am.accountMap.get(defaultAccount);
				if(!params.equals(lastParam))
				{
					System.out.println("use "+params +"to login");
					new RequestSender();
					RequestSender.login(ResourcePath.SERVERPATH, ParamsAdapter.String2List(params));
					lastParam = params;
				}
				
			}catch (IOException e1) {
					MyLogger.fatal(this.getClass(),e1.getMessage()+"���͵�¼��Ϣʧ��");
					JOptionPane.showMessageDialog(parent, "���͵�¼��Ϣʧ��");	
				}catch (NullPointerException e2) {
					autoLoginChBox.setSelected(false);
					MyLogger.info(this.getClass(),e2.getMessage()+" �������Ե��˺�");
				} 
		}
		
		//�Զ��л� 
		if(!isWebLost&&autoSelectChBox.isSelected()&&UseInfo.isLogin)
		{
			int count = 0;
			int itemCount = accountSelectCombo.getItemCount();
			while(UseInfo.useOut&&count<=itemCount)
			{
				count++;
				int index = am.usernameList.indexOf(UseInfo.userName);
				index = (index+1)%itemCount;
				String name = am.usernameList.get(index);
				String params = am.accountMap.get(name);
				System.out.println("itemcount = "+itemCount);
				System.out.println("index = "+index);
				System.out.println("count  = "+count);
				System.out.println("params = "+params);
				
				try {
					new RequestSender();
					RequestSender.logout(ResourcePath.SERVERPATH);
					if(!params.equals(lastParam))
					{
						Thread.sleep(500);
						System.out.println("use "+params +"to login");
						new RequestSender();
						RequestSender.login(ResourcePath.SERVERPATH, ParamsAdapter.String2List(params));
						lastParam = params;
//					Thread.sleep(500);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} //end if
		
	}

	public void itemStateChanged(ItemEvent e) {
		
		try {
			//�Զ���¼�Ĺ��ܼ�¼
			if(autoLoginChBox.isSelected())
			{	FlowAppMainFrame.autoLogin=true;//����Ϊ����flowdisplay�İ�ť������	,����ȫ����Ϣ		
				if(!UseInfo.userName.equals(""))
					Configure.WriteProperties("lastLogin", UseInfo.userName);
				Configure.WriteProperties("autoLogin", "true");
				
				//д���Ƿ��Զ��л������� ��Ҫ�������¼�ļ�,��Ϊ��ʼ����ʱ�������δѡ�е�״̬
				if(autoSelectChBox.isSelected())
				{	
					FlowAppMainFrame.autoSelect = true;
					Configure.WriteProperties("autoSelect", "true");
				}// if autoSelect is Selected
				else {		
					//û��ѡ���Զ��л��Ļ���д�� n
					Configure.WriteProperties("autoSelect", "false");
					FlowAppMainFrame.autoSelect = false;
					autoSelectChBox.setSelected(false);
				}
			}
			else 	//if autoLogin is Seleted
				{
					FlowAppMainFrame.autoLogin=false;	
					Configure.WriteProperties("autoLogin", "false");
				}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
