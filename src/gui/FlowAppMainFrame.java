package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import readwrite.AccountManager;
import readwrite.Configure;
import readwrite.ResourcePath;
import readwrite.WebStatus;
import tool.MyLogger;

@SuppressWarnings("serial")
public class FlowAppMainFrame extends JFrame implements ActionListener, ItemListener, WindowListener{
	private ButtonAreaPanel buttonPanel;
	private FlowDisplayPanel displayPanel;
	private JSplitPane split;
	private JMenuBar menubar;
	private JMenu menu[];
	private JMenuItem menuItem[];
	public static JCheckBoxMenuItem chekboxItem[];
	public String[] strMenu= {"账号管理","功能"};
	public String[] strMenuItem={"修改","删除","设置自登账号","清空"};
	public String[] strCheckboxItem={"保持最前","精简面板"};
	public static SimplifyDialog simplifyDialog;
	public static boolean autoSelect;
	public static boolean autoLogin;
	public static boolean inside=false;//内部的组件
	public static AccountManager am;
	public static WebStatus ws;
	public FlowAppMainFrame() {
		Configure.setFilePath(ResourcePath.CONFIGPATH);
		am = new AccountManager(ResourcePath.JARPATH,"account.txt");
		try {
			ws = new WebStatus(ResourcePath.SERVERPATH);
		} catch (IOException e) {
			MyLogger.setLogger(this.getClass());
			MyLogger.fatal(e.getMessage());
		}
		//GUI界面
		this.setTitle("流量");
		this.setBounds(400, 200, 200, 330);
		this.setResizable(false);
	
		menubar=new JMenuBar();
		this.setJMenuBar(menubar);
		menu=new JMenu[strMenu.length];
		for(int i=0;i<strMenu.length;i++)
		{	menu[i]=new JMenu(strMenu[i]);
			menubar.add(menu[i]);
		}
		menuItem=new JMenuItem[strMenuItem.length];
		for(int i=0;i<strMenuItem.length;i++)
		{
			menuItem[i]=new JMenuItem(strMenuItem[i]);
			menuItem[i].addActionListener(this);
			if(i==3)
				menu[0].addSeparator();
			menu[0].add(menuItem[i]);
		}
		chekboxItem=new JCheckBoxMenuItem[strCheckboxItem.length];
		for(int i=0;i<strCheckboxItem.length;i++)
		{
			chekboxItem[i]=new JCheckBoxMenuItem(strCheckboxItem[i]);
			chekboxItem[i].addItemListener(this);
			menu[1].add(chekboxItem[i]);
		}
		chekboxItem[0].setSelected(true);
		
		//流量展示区域
		displayPanel=new FlowDisplayPanel(true, am,ws);
		split=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.add(displayPanel);
		split.setOneTouchExpandable(true);
		//下面的按钮区域
		buttonPanel=new ButtonAreaPanel(this, am,ws);
		split.add(buttonPanel);	
		this.add(split);
		

		//设置2个状态
		this.getAutoLogin();
		this.getAutoSelect();
		
		this.setAlwaysOnTop(true);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		//System.out.println("Class= "+this.getClass().getResource("").getPath());
		//System.out.println("ClassLoad="+this.getClass().getClassLoader().getResource("").getPath());
		//System.out.println("SystemLoad="+ClassLoader.getSystemResource("").getPath());
		/*
		 * 导出的jar包输出结果
		 * Class= lin/gui/
			ClassLoad=
			SystemLoad=/C:/Users/think/Desktop/
		 */
	}
	
	//获取自动登录的状态
	public  void getAutoLogin()
	{
		autoLogin = Configure.GetValueByKey("autoLogin").equals("true")?true:false;
		if(autoLogin)
		{	
			buttonPanel.autoLoginChBox.setSelected(true);
		}	
	}

	//获取自动切换状态
	public void getAutoSelect()
	{
		try {
			autoSelect = Configure.GetValueByKey("autoSelect").equals("true")?true:false;
			if(autoSelect)
				buttonPanel.autoSelectChBox.setSelected(true);
			else buttonPanel.autoSelectChBox.setSelected(false);
		} catch (NullPointerException e) {
			MyLogger.fatal(e.getMessage());
			Configure.createDefaultFile();
		}

	}
 
	public void actionPerformed(ActionEvent e) {	
		
		//设置自动登录的账号
		if(e.getActionCommand().equals("设置自登账号")) {
			if (am.usernameList.size()!=0) {
				MyLogger.debug(getClass(), "in main panel");
				new SetDefaultLoginAccount(this,am.usernames); 
			}else JOptionPane.showMessageDialog(this, "请先添加账号");
		}
		
		//修改选项
		if(e.getActionCommand().equals("修改"))
		{	
			if (am.usernameList.size()!=0) {
				new UpdateAccountDialog(this,am);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : am.usernameList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "没有可更改的账号");
		}
		
		//删除选项
		if(e.getActionCommand().equals("删除"))
		{
			if (am.usernameList.size()!=0) {
				new DropDialog(this,am);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : am.usernameList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "没有可删除的账号");
		}
		
		//清空
		if(e.getActionCommand().equals("清空"))
		{
			if (am.usernameList.size()!=0) {
				if (JOptionPane.showConfirmDialog(this, "确定要清空?", "清空",
						JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
					File f = new File(this.getClass().getClassLoader().getResource("").getPath() + "account.txt");
					if (f.exists())
						if (f.delete())
							try {
								f.createNewFile();
								buttonPanel.accountSelectCombo.removeAllItems();
							} catch (IOException e1) {
								MyLogger.setLogger(this.getClass());
								MyLogger.fatal("清空账户失败");
								JOptionPane.showMessageDialog(this, "清空账号失败");
							}
				} 
			}else JOptionPane.showMessageDialog(this, "没有可以删除的账号");
		}
	}

	public void itemStateChanged(ItemEvent e) {
		//始终在前功能的检查
		if(e.getSource()==chekboxItem[0]&&chekboxItem[0].isSelected())
		{
			this.setAlwaysOnTop(true);
			this.setVisible(true);
		}else {			
			this.setAlwaysOnTop(false);
			this.setVisible(true);
		}
		
		//显示精简面板
		if(chekboxItem[1].isSelected()&&simplifyDialog==null)
				simplifyDialog=new SimplifyDialog(ws,true,displayPanel.timer);
		if(!chekboxItem[1].isSelected()&&simplifyDialog!=null)
			{	simplifyDialog.dispose();
				simplifyDialog=null;
			}
	}

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
		
	}

	public void windowClosing(WindowEvent arg0) {
		//没有设置自动登录的时候自动记录当前的账号作为
		//没有设置默认账号的时候登录的账号
		try {
			if(autoLogin)
			{	
				Configure.WriteProperties("defaultUser", ws.userName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			MyLogger.fatal(e.getMessage());
		}
	}

	public void windowDeactivated(WindowEvent arg0) {
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		
	}

	public void windowIconified(WindowEvent arg0) {
		
	}

	public void windowOpened(WindowEvent e) {
	}

	public static void main(String[] args) {
		MyLogger.loadConfigure();
		MyLogger.setLogger(FlowAppMainFrame.class);
		MyLogger.info("hello there");
		new FlowAppMainFrame();
	}
	
}
