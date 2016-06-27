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
	//记录是不是初始化的时候的激发的自动选择
	public 	int i=0;
	public ButtonAreaPanel(JFrame parent,AccountManager am,WebStatus ws) {
		this.am = am;
		this.ws = ws;
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
			if(!ws.isWebLost)
			{	
				String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
				if(temp!=null&&ws.loginStatus==WebStatus.OUT)
				{	
					params=am.accountMap.get(temp);
					try {
						new RequestSender().login(ResourcePath.SERVERPATH	, params);
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
		
		//初始化自动登录按钮
		if(FlowAppMainFrame.autoLogin)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//初始化自动切换按钮
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//流量提醒的检查
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
			music.showControlPanel(parent, "流量警告");
		}
		
		
		//设置提醒,设置之后会变成取消提醒字样
		if(action.equals("设置提醒"))
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
		{	alarmButton.setText("取消提醒");
			alarmButton.setActionCommand("取消提醒");
		}
		if(action.equals("取消提醒")||!alarmhasSet)
		{	alarmButton.setText("设置提醒");
			alarmhasSet=false;
			alarmButton.setActionCommand("设置提醒");
		}
		
		//登录按钮的检查
		if(!ws.isWebLost)
		{	if(ws.loginStatus==1)
			{	loginButton.setEnabled(false);
			}
			else {
				loginButton.setEnabled(true);
			}
		}
		
		//自动登录的检查,如果登出了,自动发送登录信息
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
					MyLogger.fatal(this.getClass(),e1.getMessage()+"发送登录消息失败");
					JOptionPane.showMessageDialog(parent, "发送登录信息失败");	
				}catch (NullPointerException e2) {
					autoLoginChBox.setSelected(false);
					MyLogger.info(this.getClass(),e2.getMessage()+" 不设置自登账号");
				}
		}
		
	}

	public void itemStateChanged(ItemEvent e) {
		
		try {
			//自动登录的功能记录
			if(autoLoginChBox.isSelected())
			{	FlowAppMainFrame.autoLogin=true;//这是为了让flowdisplay的按钮不可用	,传递全局信息		
				if(ws.userName!=null)
					Configure.WriteProperties("lastLogin", ws.userName);
				Configure.WriteProperties("autoLogin", "true");
				
				//写入是否自动切换的资料 不要在这里记录文件,因为初始化的时候会先有未选中的状态
				if(autoSelectChBox.isSelected())
				{	
					FlowAppMainFrame.autoSelect = true;
					Configure.WriteProperties("autoSelect", "true");
				}// if autoSelect is Selected
				else {		
					//没有选中自动切换的话就写入 n
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
