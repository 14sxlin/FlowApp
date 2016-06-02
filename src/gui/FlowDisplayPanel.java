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

import readwrite.AccountManager;
import readwrite.ResourcePath;
import readwrite.WebStatus;
import tool.RequestSender;

@SuppressWarnings("serial")
public class FlowDisplayPanel extends JPanel implements ActionListener {
	/*
	 * 记得退出的时候把时间关掉
	 */
	public JTextField usedText,totalText,remainText,nameText;//用来显示流量的数值
	public JLabel statusLabel;
	public JButton logoutButton;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	public  Timer timer;
	private WebStatus ws;
	private AccountManager am;
	//参数显示要不要精简模式,测试时用false
    public FlowDisplayPanel(boolean simplify,AccountManager am,WebStatus ws) {
    	this.am = am;
    	this.ws = ws;
    	//流量展示区
    	this.setBorder(new TitledBorder("流量"));
    	gridbag=new GridBagLayout();
    	this.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("用户名:"), constraints);    	
    	constraints=new GridBagConstraints(1, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	nameText=new JTextField();
    	nameText.setEditable(false);
    	this.add(nameText, constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("已使用流量:"),constraints);    	
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
    	this.add(new JLabel("总流量:"), constraints);    	
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
    	this.add(new JLabel("剩余流量:"), constraints);
    	
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
    	statusLabel=new JLabel("未登录",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	this.add(statusLabel, constraints);
    	
    	constraints=new GridBagConstraints(0, 5, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	logoutButton=new JButton("退出登录");
    	logoutButton.addActionListener(this);
    	logoutButton.setEnabled(false);
    	this.add(logoutButton, constraints);
    	
    	if (!ws.isWebLost) {
			ws.loadHtml();
			timer = new Timer(1000, this);
			if (ws.loginStatus == 1)
				timer.setDelay(ws.timer.getDelay());
			timer.start();
		}else
		{
//System.out.println("weblost");
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("已断网");
			logoutButton.setEnabled(false);			
		}
	}
    
    //设置显示的数据
    public void setTexts()
    {	if(!ws.isWebLost)
    	{
    		this.nameText.setText(ws.userName);
    		this.usedText.setText(""+ws.usedAmount);
    		this.totalText.setText(""+ws.totalAmount);
    		this.remainText.setText(""+ws.remainAmount);
    		this.setLoginStatus();
    	}
    }
    
    //设置状态标签显示的内容 包括已登录,未登录,用户名或密码错误,流量已用完,已断网
    public void setLoginStatus()
    {
    	if(!ws.isWebLost)	
		{	if(ws.loginStatus==1)
			{	statusLabel.setForeground(Color.green);
				statusLabel.setText("已登录");
				logoutButton.setEnabled(true);
			}
			else if(ws.loginStatus==0)
			{
				statusLabel.setForeground(Color.red);
				statusLabel.setText("未登录");
				logoutButton.setEnabled(false);
			}else {
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("用户名或密码错误");
				logoutButton.setEnabled(false);
			}
			if(ws.useOut)
			{
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("流量已用完");
				logoutButton.setEnabled(false);
			}
			if(FlowAppMainFrame.autoLogin)
			{	logoutButton.setEnabled(false);
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
			ButtonAreaPanel.loginButton.setEnabled(false);
			
		}
    }
   
	public void actionPerformed(ActionEvent e) {
		
		//设置面板的数据
		this.setLoginStatus();
		//自动切换账号登录
		if(ws.useOut&&FlowAppMainFrame.autoSelect)
		{
			//记录试了几个账号
			int time=0;
			
			//如果是登录着,发送退出的信息
			if(ws.loginStatus==1)
				try {
					new RequestSender().logout(ResourcePath.SERVERPATH);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//用别的账号登录
			while(time<am.usernameList.size())		
			{
				String key=am.usernameList.get(time);
				try {
					new RequestSender().login(ResourcePath.SERVERPATH, 
							am.accountMap.get(key));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				time++;
				//如果登上了而且该账号流量没用完的话,就退出
				if(ws.loginStatus==1&&!ws.useOut)
					break;
			}
			
			//如果全部账号用完了,还是没登上,可能是用户名或密码错误
			if(time==am.usernameList.size()&&ws.loginStatus==0)
			{		JOptionPane.showMessageDialog(this, "已经没有可用的账号了");
					FlowAppMainFrame.autoSelect=false;}
			
			//如果全部账号用完了,还是没流量
			if(time==am.usernameList.size()&&ws.useOut)
			{	JOptionPane.showMessageDialog(this, "你拥有的账号流量全部用完了");
				FlowAppMainFrame.autoSelect=false;
			}
			
		}
		
		//更新精简面板的数据
		if(FlowAppMainFrame.simplifyDialog!=null)
			FlowAppMainFrame.simplifyDialog.setTexts();
		this.setTexts();
		if(e.getSource()==logoutButton)
			try {
				new RequestSender().logout(ResourcePath.SERVERPATH);					
				this.setTexts();
				ButtonAreaPanel.loginButton.setEnabled(true);			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "发送退出信息失败");
			}
		
		//检查退出登录的按钮
		if(ws.loginStatus==0)
			logoutButton.setEnabled(false);
	}
}
