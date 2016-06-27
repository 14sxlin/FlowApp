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

import readwrite.AccountManager;
import readwrite.Configure;
import readwrite.ResourcePath;
import readwrite.WebStatus;
import tool.MusicPlayerWithDialog;
import tool.MyLogger;
import tool.RequestSender;

@SuppressWarnings("serial")
public class ButtonAreaPanel extends JPanel implements ActionListener, ItemListener {
	private WebStatus ws;
	private JFrame parent;
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
	public ButtonAreaPanel(JFrame parent,AccountManager am,WebStatus ws) {
		this.am = am;
		this.ws = ws;
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
			if(!ws.isWebLost)
			{	
				String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
				if(temp!=null&&ws.loginStatus==WebStatus.OUT)
				{	
					params=am.accountMap.get(temp);
					try {
						new RequestSender().login(ResourcePath.SERVERPATH	, params);
						}catch (IOException e1) {
						JOptionPane.showMessageDialog(parent, "���͵�¼��Ϣʧ��");
					}
				}
				else JOptionPane.showMessageDialog(parent, "�����˳��ѵ�¼���˺�");
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
		timer=new Timer(1000, this);
		if(!ws.isWebLost)
			timer.start();
		else loginButton.setEnabled(false);
//System.out.println("loginState="+ReadStatus.loginStatus);
		if(ws.loginStatus==WebStatus.IN)
			loginButton.setEnabled(false);
		
//System.out.println("Class= "+this.getClass().getResource("").getPath());
//System.out.println("ClassLoad="+this.getClass().getClassLoader().getResource("").getPath());
//System.out.println("SystemLoad="+ClassLoader.getSystemResource("").getPath());
	}
	
	public void actionPerformed(ActionEvent e) {
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
		if(!ws.isWebLost&&(alarmhasSet)&&
				ws.usedAmount>=AlarmSettingDialog.alarmAmount)
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
				new AlarmSettingDialog(parent, ws.totalAmount);
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
		
		//��¼��ť�ļ��
		if(!ws.isWebLost)
		{	if(ws.loginStatus==1)
			{	loginButton.setEnabled(false);
			}
			else {
				loginButton.setEnabled(true);
			}
		}
		
		//�Զ���¼�ļ��,����ǳ���,�Զ����͵�¼��Ϣ
		if(!ws.isWebLost&&autoLoginChBox.isSelected()&&ws.loginStatus==0)
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
				new RequestSender().login(ResourcePath.SERVERPATH, params);
				}catch (IOException e1) {
					MyLogger.fatal(this.getClass(),e1.getMessage()+"���͵�¼��Ϣʧ��");
					JOptionPane.showMessageDialog(parent, "���͵�¼��Ϣʧ��");	
				}catch (NullPointerException e2) {
					autoLoginChBox.setSelected(false);
					MyLogger.info(this.getClass(),e2.getMessage()+" �������Ե��˺�");
				}
		}
		
	}

	public void itemStateChanged(ItemEvent e) {
		
		try {
			//�Զ���¼�Ĺ��ܼ�¼
			if(autoLoginChBox.isSelected())
			{	FlowAppMainFrame.autoLogin=true;//����Ϊ����flowdisplay�İ�ť������	,����ȫ����Ϣ		
				if(ws.userName!=null)
					Configure.WriteProperties("lastLogin", ws.userName);
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
