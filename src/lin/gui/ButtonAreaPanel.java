package lin.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import lin.readwrite.AccountManager;
import lin.readwrite.ConfigAutoLogin;
import lin.readwrite.ConfigAutoSelect;
import lin.readwrite.ReadStatus;
import lin.readwrite.ResourcePath;
import lin.tool.PlayMusic;
import lin.tool.SendLoginRequest;

@SuppressWarnings("serial")
public class ButtonAreaPanel extends JPanel implements ActionListener, ItemListener {

	private JFrame parent;
	public  JComboBox<String> accountSelectCombo;
	public  static JButton loginButton;
	public  JButton addUser;
	public  JCheckBox autoLoginChBox;
	public  JCheckBox autoSelectChBox;
	private JButton alarmButton;
	public AddDialog accountDialog;
	public static boolean autoLogin=false;
	public static boolean hasDefault;
	public static boolean alarmhasSet=false;
	public String params;
	public Timer timer;
	public PlayMusic music;
	public SetDefaultLoginAccount setDefaultLoginAccount;
	private AccountManager am;
	//��¼�ǲ��ǳ�ʼ����ʱ��ļ������Զ�ѡ��
	public 	int i=0;
	public ButtonAreaPanel(JFrame parent,AccountManager am) {
		this.am = am;
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
			if(!ReadStatus.WebLost)
			{	
				String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
				if(temp!=null&&ReadStatus.loginStatus==ReadStatus.OUT)
				{	
					params=am.accountMap.get(temp);
					try {
						new SendLoginRequest().login(ResourcePath.SERVERPATH	, params);
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
		if(!ReadStatus.WebLost)
			timer.start();
		else loginButton.setEnabled(false);
//System.out.println("loginState="+ReadStatus.loginStatus);
		if(ReadStatus.loginStatus==ReadStatus.IN)
			loginButton.setEnabled(false);
		
//System.out.println("Class= "+this.getClass().getResource("").getPath());
//System.out.println("ClassLoad="+this.getClass().getClassLoader().getResource("").getPath());
//System.out.println("SystemLoad="+ClassLoader.getSystemResource("").getPath());
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String action=e.getActionCommand();
		if(action==null)
			action="none";
//System.out.println("Set?  "+alarmhasSet+"\nAmount="+Integer.parseInt(ReadStatus.subNum(ReadStatus.usedAmount)));
		
		//��ʼ���Զ���¼��ť
		if(FlowAppMainFrame.autologin>0)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//��ʼ���Զ��л���ť
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//�������ѵļ��
		if(!ReadStatus.WebLost&&(alarmhasSet)&&(Integer.parseInt(ReadStatus.subNum(ReadStatus.usedAmount))
				>=AlarmSettingDialog.alarmAmount))
		{
//System.out.println("provoke!");
			music=new PlayMusic(AlarmSettingDialog.musicPath,true);
			music.play();
			music.showControlPanel(null, "��������");
		}
		
		
		//��������,����֮�����ȡ����������
		if(action.equals("��������"))//���������getAccom..�ᱨ�� Ϊë��?��Ϊʱ����ִ�е�ʱ��û�м����¼�
		{	FlowAppMainFrame.inside=true;
			try {
				new AlarmSettingDialog(ReadStatus.subNum(ReadStatus.totalAmount));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		if(!ReadStatus.WebLost)
		{	if(ReadStatus.loginStatus==1)
			{	loginButton.setEnabled(false);
	//			accountSelectCombo.setEditable(false);
			}
			else {
				loginButton.setEnabled(true);
	//			accountSelectCombo.setEditable(true);
			}
		}
		
		//�Զ���¼�ļ��,����ǳ���,�Զ����͵�¼��Ϣ
		if(!ReadStatus.WebLost&&autoLoginChBox.isSelected()&&ReadStatus.loginStatus==0)
		{
			String defaultAccount = null;
			try {
				defaultAccount=new ConfigAutoLogin().readName();
//System.out.println("defaultAccount=   "+defaultAccount);
				if(defaultAccount==null)
					setDefaultLoginAccount=new SetDefaultLoginAccount((String[])am.usernameList.toArray(), new ConfigAutoLogin());
				params=am.accountMap.get(defaultAccount);
				try {
					new SendLoginRequest().login(ResourcePath.SERVERPATH	, params);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(parent, "���͵�¼��Ϣʧ��");	}
				} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();}
		}
		
	}

	public void itemStateChanged(ItemEvent e) {
		
		//�Զ���¼�Ĺ��ܼ�¼
		if(autoLoginChBox.isSelected())
		{	autoLogin=true;//����Ϊ����flowdisplay�İ�ť������	,����ȫ����Ϣ		
			if(ReadStatus.loginStatus==1)
				try {
					if(FlowAppMainFrame.autologin<=0)
					{	new ConfigAutoLogin().write2Name(ReadStatus.userName);
						FlowAppMainFrame.autologin=2;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			//д���Ƿ��Զ��л������� ��Ҫ�������¼�ļ�,��Ϊ��ʼ����ʱ�������δѡ�е�״̬
//			autoSelectChBox.setSelected(true);
			if(autoSelectChBox.isSelected())
			{	
				if (i!=0) {
					FlowAppMainFrame.autoSelect = true;
					//�½�һ����¼���ļ�
					new ConfigAutoSelect().writeY();
//System.out.println("select");
				}
				i++;

			}
			else {		
				if (i!=0) {
					//û��ѡ���Զ��л��Ļ���д�� n
//System.out.println("didn't select");
					new ConfigAutoSelect().writeN();
					FlowAppMainFrame.autoSelect = false;
					autoSelectChBox.setSelected(false);
				}
				i++;
			}
//System.out.println(autoLogin);
		}
		else 	
			{
				autoLogin=false;	
				FlowAppMainFrame.autologin=-1;
			}
	}
}
