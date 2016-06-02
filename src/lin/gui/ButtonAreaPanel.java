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
	//记录是不是初始化的时候的激发的自动选择
	public 	int i=0;
	public ButtonAreaPanel(JFrame parent,AccountManager am) {
		this.am = am;
		this.parent = parent;
		this.setLayout(new GridLayout(3, 2));
		alarmButton=new JButton("设置提醒");
		alarmButton.setActionCommand("设置提醒");
		alarmButton.addActionListener(this);
		
		addUser=new JButton("添加账户");
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
		
		loginButton=new JButton("登录");
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
						JOptionPane.showMessageDialog(parent, "发送登录信息失败");
					}
				}
				else JOptionPane.showMessageDialog(parent, "请先退出已登录的账号");
			}
		});
		
		autoLoginChBox=new JCheckBox("自动登录");
		autoSelectChBox=new JCheckBox("自动切换");
		autoLoginChBox.addItemListener(this);
		autoSelectChBox.addItemListener(this);
		accountSelectCombo=new JComboBox<String>(am.usernames);
		accountSelectCombo.addActionListener(this);
		this.setBorder(new TitledBorder("功能区"));
		this.setLayout(new GridLayout(3, 2));
		this.add(alarmButton);
		this.add(addUser);
		this.add(accountSelectCombo);
		this.add(loginButton);
		this.add(autoLoginChBox);
		this.add(autoSelectChBox);
		
		//启动时间器
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
		
		//初始化自动登录按钮
		if(FlowAppMainFrame.autologin>0)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//初始化自动切换按钮
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//流量提醒的检查
		if(!ReadStatus.WebLost&&(alarmhasSet)&&(Integer.parseInt(ReadStatus.subNum(ReadStatus.usedAmount))
				>=AlarmSettingDialog.alarmAmount))
		{
//System.out.println("provoke!");
			music=new PlayMusic(AlarmSettingDialog.musicPath,true);
			music.play();
			music.showControlPanel(null, "流量警告");
		}
		
		
		//设置提醒,设置之后会变成取消提醒字样
		if(action.equals("设置提醒"))//这里如果用getAccom..会报错 为毛啊?因为时间器执行的时候没有激发事件
		{	FlowAppMainFrame.inside=true;
			try {
				new AlarmSettingDialog(ReadStatus.subNum(ReadStatus.totalAmount));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
		if(alarmhasSet)
		{	alarmButton.setText("取消提醒");
			alarmButton.setActionCommand("取消提醒");
		}
		if(action.equals("取消提醒")||!alarmhasSet)
		{	alarmButton.setText("设置提醒");
			alarmhasSet=false;
			alarmButton.setActionCommand("设置提醒");
		}
		
		//登录按钮的检查
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
		
		//自动登录的检查,如果登出了,自动发送登录信息
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
						JOptionPane.showMessageDialog(parent, "发送登录信息失败");	}
				} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();}
		}
		
	}

	public void itemStateChanged(ItemEvent e) {
		
		//自动登录的功能记录
		if(autoLoginChBox.isSelected())
		{	autoLogin=true;//这是为了让flowdisplay的按钮不可用	,传递全局信息		
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
			
			//写入是否自动切换的资料 不要在这里记录文件,因为初始化的时候会先有未选中的状态
//			autoSelectChBox.setSelected(true);
			if(autoSelectChBox.isSelected())
			{	
				if (i!=0) {
					FlowAppMainFrame.autoSelect = true;
					//新建一个记录的文件
					new ConfigAutoSelect().writeY();
//System.out.println("select");
				}
				i++;

			}
			else {		
				if (i!=0) {
					//没有选中自动切换的话就写入 n
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
