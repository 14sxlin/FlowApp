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
	public String[] strMenu= {"�˺Ź���","����"};
	public String[] strMenuItem={"�޸�","ɾ��","�����Ե��˺�","���"};
	public String[] strCheckboxItem={"������ǰ","�������"};
	public static SimplifyDialog simplifyDialog;
	public static boolean autoSelect;
	public static boolean autoLogin;
	public static boolean inside=false;//�ڲ������
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
		//GUI����
		this.setTitle("����");
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
		
		//����չʾ����
		displayPanel=new FlowDisplayPanel(true, am,ws);
		split=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.add(displayPanel);
		split.setOneTouchExpandable(true);
		//����İ�ť����
		buttonPanel=new ButtonAreaPanel(this, am,ws);
		split.add(buttonPanel);	
		this.add(split);
		

		//����2��״̬
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
		 * ������jar��������
		 * Class= lin/gui/
			ClassLoad=
			SystemLoad=/C:/Users/think/Desktop/
		 */
	}
	
	//��ȡ�Զ���¼��״̬
	public  void getAutoLogin()
	{
		autoLogin = Configure.GetValueByKey("autoLogin").equals("true")?true:false;
		if(autoLogin)
		{	
			buttonPanel.autoLoginChBox.setSelected(true);
		}	
	}

	//��ȡ�Զ��л�״̬
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
		
		//�����Զ���¼���˺�
		if(e.getActionCommand().equals("�����Ե��˺�")) {
			if (am.usernameList.size()!=0) {
				MyLogger.debug(getClass(), "in main panel");
				new SetDefaultLoginAccount(this,am.usernames); 
			}else JOptionPane.showMessageDialog(this, "��������˺�");
		}
		
		//�޸�ѡ��
		if(e.getActionCommand().equals("�޸�"))
		{	
			if (am.usernameList.size()!=0) {
				new UpdateAccountDialog(this,am);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : am.usernameList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "û�пɸ��ĵ��˺�");
		}
		
		//ɾ��ѡ��
		if(e.getActionCommand().equals("ɾ��"))
		{
			if (am.usernameList.size()!=0) {
				new DropDialog(this,am);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : am.usernameList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "û�п�ɾ�����˺�");
		}
		
		//���
		if(e.getActionCommand().equals("���"))
		{
			if (am.usernameList.size()!=0) {
				if (JOptionPane.showConfirmDialog(this, "ȷ��Ҫ���?", "���",
						JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
					File f = new File(this.getClass().getClassLoader().getResource("").getPath() + "account.txt");
					if (f.exists())
						if (f.delete())
							try {
								f.createNewFile();
								buttonPanel.accountSelectCombo.removeAllItems();
							} catch (IOException e1) {
								MyLogger.setLogger(this.getClass());
								MyLogger.fatal("����˻�ʧ��");
								JOptionPane.showMessageDialog(this, "����˺�ʧ��");
							}
				} 
			}else JOptionPane.showMessageDialog(this, "û�п���ɾ�����˺�");
		}
	}

	public void itemStateChanged(ItemEvent e) {
		//ʼ����ǰ���ܵļ��
		if(e.getSource()==chekboxItem[0]&&chekboxItem[0].isSelected())
		{
			this.setAlwaysOnTop(true);
			this.setVisible(true);
		}else {			
			this.setAlwaysOnTop(false);
			this.setVisible(true);
		}
		
		//��ʾ�������
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
		//û�������Զ���¼��ʱ���Զ���¼��ǰ���˺���Ϊ
		//û������Ĭ���˺ŵ�ʱ���¼���˺�
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
