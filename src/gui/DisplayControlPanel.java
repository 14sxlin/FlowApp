package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import org.apache.http.client.ClientProtocolException;

import readwrite.AccountManager;
import readwrite.Configure;
import readwrite.ResourcePath;
import readwrite.UseInfo;
import tool.MyLogger;
import tool.ParamsAdapter;
import tool.RequestSender;
import tool.TimerControl;

@SuppressWarnings("serial")
public class DisplayControlPanel extends JPanel implements ActionListener, ItemListener {

	
	/*公共部分*/
	public static boolean isWebLost = false;
	public static boolean alarmhasSet=false;
	
	private AccountManager am;
	public int init=0;		//记录是不是初始化的时候的激发的自动选择
	public Timer timer;
	
	/*button 面板*/
	private JPanel btnPanel;
	private JFrame parent;
	private String lastParam;
	public  JComboBox<String> accountSelectCombo;
	public  JButton loginButton;
	public  JButton addUser;
	public  JCheckBox autoLoginChBox;
	public  JCheckBox autoSelectChBox;
	private JButton alarmButton;
	public AddDialog accountDialog;
	public String params;
	
	public MusicPlayerWithDialog music;
	public SetDefaultLoginAccount setDefaultLoginAccount;
	
	
	/*Flow display*/
	private JPanel flowDisplayPanel;
	public JTextField usedText,totalText,remainText,nameText;//用来显示流量的数值
	public JLabel statusLabel;
	public JButton logoutButton;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	
	
	public DisplayControlPanel(JFrame parent,AccountManager am) {
		this.parent = parent;
		this.am = am;
		timer = new Timer(TimerControl.FAST_MODE, this);
		timer.start();
		initGUI();
		setTexts();
	}
	
	private void initGUI(){
		
		/*btnPanel*/
		btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(3, 2));
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
			String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
			params=am.accountMap.get(temp);
			try {
				String info = RequestSender.login(ResourcePath.SERVERPATH, 
						ParamsAdapter.String2List(params));
				MyLogger.info(DisplayControlPanel.class, "try login : "+info);
				if(info.contains("false"))
				{
					if(info.contains("密码错误"))
						JOptionPane.showMessageDialog(parent, "用户名或密码错误");
					else{
						String msg = "";
						int s = info.indexOf("msg:'");
						int end = info.indexOf("'",s);
						JOptionPane.showMessageDialog(parent, msg.substring(s, end));
					}
					
				}else{
					UseInfo.Refresh();
				};
			}catch (IOException e1) {
				JOptionPane.showMessageDialog(parent, "发送登录信息失败");
			}
		});
		
		autoLoginChBox=new JCheckBox("自动登录");
		autoSelectChBox=new JCheckBox("自动切换");
		autoSelectChBox.setEnabled(false);
		autoLoginChBox.addItemListener(this);
		autoSelectChBox.addItemListener(this);
		if(am!=null)
			accountSelectCombo=new JComboBox<String>(am.usernames);
		else
			accountSelectCombo = new JComboBox<>();
		accountSelectCombo.addActionListener(this);
		btnPanel.setBorder(new TitledBorder("功能区"));
		btnPanel.setLayout(new GridLayout(3, 2));
		btnPanel.add(alarmButton);
		btnPanel.add(addUser);
		btnPanel.add(accountSelectCombo);
		btnPanel.add(loginButton);
		btnPanel.add(autoLoginChBox);
		btnPanel.add(autoSelectChBox);
		
		
		/*FlowDisplayPanel*/
		flowDisplayPanel = new JPanel();
    	flowDisplayPanel.setBorder(new TitledBorder("流量"));
    	gridbag=new GridBagLayout();
    	flowDisplayPanel.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	flowDisplayPanel.add(new JLabel("用户名:"), constraints);    	
    	constraints=new GridBagConstraints(1, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	nameText=new JTextField();
    	nameText.setEditable(false);
    	flowDisplayPanel.add(nameText, constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	flowDisplayPanel.add(new JLabel("已使用流量:"),constraints);    	
    	constraints=new GridBagConstraints(1, 2, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	usedText=new JTextField();
    	usedText.setForeground(Color.blue);
    	usedText.setEditable(false);
    	flowDisplayPanel.add(usedText, constraints);    	
    	constraints=new GridBagConstraints(2, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	flowDisplayPanel.add(new JLabel("M"),constraints);
    	
    	constraints=new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	flowDisplayPanel.add(new JLabel("总流量:"), constraints);    	
    	constraints=new GridBagConstraints(1, 1, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	totalText=new JTextField();
    	totalText.setForeground(Color.ORANGE);
    	totalText.setEditable(false);
    	flowDisplayPanel.add(totalText, constraints);    	
    	constraints=new GridBagConstraints(2, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	flowDisplayPanel.add(new JLabel("M"),constraints);
    	
    	constraints=new GridBagConstraints(0, 3, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	flowDisplayPanel.add(new JLabel("剩余流量:"), constraints);
    	
    	constraints=new GridBagConstraints(1, 3, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	remainText=new JTextField();
    	remainText.setForeground(Color.red);
    	remainText.setEditable(false);
    	flowDisplayPanel.add(remainText, constraints);
    	
    	constraints=new GridBagConstraints(2, 3, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	flowDisplayPanel.add(new JLabel("M"),constraints);
    	
    	constraints=new GridBagConstraints(0, 4, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	statusLabel=new JLabel("未登录",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	flowDisplayPanel.add(statusLabel, constraints);
    	
    	constraints=new GridBagConstraints(0, 5, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	logoutButton=new JButton("退出登录");
    	logoutButton.addActionListener(this);
    	logoutButton.setEnabled(false);
    	flowDisplayPanel.add(logoutButton, constraints);
    	
    	
    	this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, flowDisplayPanel, btnPanel));
//    	Dimension dimension = getToolkit().getScreenSize();
//    	this.setSize((int)(dimension.getWidth()/7), (int)(dimension.getHeight()/5));
	}

	private void setTexts()
    {	
    	if(!isWebLost)
    	{
    		this.nameText.setText(UseInfo.userName);
			this.usedText.setText(""+UseInfo.used);
			this.totalText.setText(""+UseInfo.total);
			this.remainText.setText(""+UseInfo.remain);
			this.setLoginStatus();
    	}
    	
    }
	
    private void setLoginStatus()
    {
    	if(!isWebLost)	
		{	
    		if(UseInfo.isLogin)
			{	statusLabel.setForeground(Color.green);
				statusLabel.setText("已登录");
				logoutButton.setEnabled(true);
			}
			else 
			{
				statusLabel.setForeground(Color.red);
				statusLabel.setText("未登录");
				logoutButton.setEnabled(false);
			}
//			else {
//				statusLabel.setForeground(Color.blue);
//				statusLabel.setText("用户名或密码错误");
//				logoutButton.setEnabled(false);
//			}
			if(UseInfo.useOut)
			{
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("流量已用完");
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
			statusLabel.setText("已断网");
			logoutButton.setEnabled(false);
			loginButton.setEnabled(false);
			
		}
    }
   
	@Override
	public void actionPerformed(ActionEvent e) {
		
		this.setTexts();
		
		/*flow panel*/
		//更新精简面板的数据
		if(FlowAppMainFrame.simplifyDialog!=null)
			FlowAppMainFrame.simplifyDialog.setTexts();
		
		
		if(e.getSource()==logoutButton)
			try {
				RequestSender.logout(ResourcePath.SERVERPATH);	
				UseInfo.Refresh();
				this.setTexts();
			} catch (IOException e1) {
				MyLogger.fatal(getClass(), e1.getMessage()+" 发送退出消息失败");
				JOptionPane.showMessageDialog(null, "发送退出信息失败");
			}
		
		/*button panel*/
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
		
		//初始化自动登录按钮
		if(FlowAppMainFrame.autoLogin)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//初始化自动切换按钮
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//流量提醒的检查
		if(alarmhasSet&&
				UseInfo.used>=AlarmSettingDialog.alarmAmount)
		{
			try {
				if(AlarmSettingDialog.indexOfMusic>=0&&AlarmSettingDialog.indexOfMusic<=8)
				{
					music = new MusicPlayerWithDialog(getClass().getResourceAsStream("/sounds/"+AlarmSettingDialog.musicName)
							,true,alarmhasSet);
				}
				else {
					music=new MusicPlayerWithDialog(new File(AlarmSettingDialog.musicName),true,alarmhasSet);
				}
			} catch (IOException e1) {
				MyLogger.fatal(DisplayControlPanel.class,e1.getMessage());
				e1.printStackTrace();
			}
			music.play();
			music.showControlPanel(parent, "流量警告");
		}
		
		
		//设置提醒,设置之后会变成取消提醒字样
		if(action.equals("设置提醒"))
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
		{	alarmButton.setText("取消提醒");
			alarmButton.setActionCommand("取消提醒");
		}
		if(action.equals("取消提醒")||!alarmhasSet)
		{	alarmButton.setText("设置提醒");
			alarmhasSet=false;
			alarmButton.setActionCommand("设置提醒");
		}
		
		
		//自动登录的检查,如果登出了,自动发送登录信息
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
					MyLogger.fatal(this.getClass(),e1.getMessage()+"发送登录消息失败");
					JOptionPane.showMessageDialog(parent, "发送登录信息失败");	
				}catch (NullPointerException e2) {
					autoLoginChBox.setSelected(false);
					MyLogger.info(this.getClass(),e2.getMessage()+" 不设置自登账号");
				} 
		}
		
		//TODO 自动切换
		
		if(UseInfo.isLogin)
		{
			loginButton.setEnabled(false);
			logoutButton.setEnabled(true);
		}else{
			loginButton.setEnabled(true);
			logoutButton.setEnabled(false);
		}
		
		
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		try {
			//自动登录的功能记录
			if(autoLoginChBox.isSelected())
			{	FlowAppMainFrame.autoLogin=true;//这是为了让flowdisplay的按钮不可用	,传递全局信息		
				if(!UseInfo.userName.equals(""))
					Configure.WriteProperties("lastLogin", UseInfo.userName);
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
