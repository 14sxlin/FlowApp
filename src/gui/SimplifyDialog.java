package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import readwrite.WebStatus;

@SuppressWarnings("serial")
public class SimplifyDialog extends JDialog implements WindowListener,ActionListener {
	public JTextField usedText,remainText;//用来显示流量的数值
	public JLabel statusLabel;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	private JPanel jPanel;
	private WebStatus ws;
	public SimplifyDialog( WebStatus ws,boolean inCheck,Timer timer) {
		this.ws = ws;
		jPanel=new JPanel();
    	jPanel.setBorder(new TitledBorder("流量"));
    	gridbag=new GridBagLayout();
    	jPanel.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("已使用:"),constraints);    	
    	constraints=new GridBagConstraints(1, 1, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	usedText=new JTextField();
    	usedText.setEditable(false);
    	jPanel.add(usedText, constraints);
    	constraints=new GridBagConstraints(2, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("M"), constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("剩余:"), constraints);    	
    	constraints=new GridBagConstraints(1, 2, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	remainText=new JTextField();
    	remainText.setForeground(Color.red);
    	remainText.setEditable(false);
    	jPanel.add(remainText, constraints);    	
    	constraints=new GridBagConstraints(2, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("M"), constraints);
    	
    	constraints=new GridBagConstraints(0, 3, 2, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	statusLabel=new JLabel("未登录",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	jPanel.add(statusLabel, constraints);
    	
    	if(ws.loginStatus==1)
    		this.setTitle(ws.userName);
    	this.setSize(120, 120);
    	Dimension d=getToolkit().getScreenSize();
    	this.setLocation(d.width-150,0);
    	this.add(jPanel);
    	this.setAlwaysOnTop(true);
    	
    	this.setLoginStatus();
    	
    	if(inCheck)
    		this.addWindowListener(this);
    	else this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	this.setVisible(true);
	}
	
	public void setLoginStatus()
    {
			if(!ws.isWebLost)
	    	{	if(ws.loginStatus==1)
	    		{	statusLabel.setForeground(Color.green);
	    		statusLabel.setText("已登录");
	    		}
	    		else if(ws.loginStatus==0)
	    		{
	    			statusLabel.setForeground(Color.red);
	    			statusLabel.setText("未登录");
	    		}else {
	    			statusLabel.setForeground(Color.blue);
	    		statusLabel.setText("用户名或密码错误");
	    	}
	    	if(ws.useOut)
	    	{
	    		statusLabel.setForeground(Color.blue);
	    		statusLabel.setText("流量已用完");
	    	}}
			else {
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("已断网");
			}
    }

	public void setTexts()
    {	if(!ws.isWebLost)
    	{	this.setTitle(ws.userName);
	    	this.usedText.setText(""+ws.usedAmount);
	    	this.remainText.setText(""+ws.remainAmount);
	    	this.setLoginStatus();
    	}else
    	{
    		this.setTitle(ws.userName);
	    	this.usedText.setText("");
	    	this.remainText.setText("");
	    	this.setLoginStatus();
    	}
    }
	

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		if(FlowAppMainFrame.chekboxItem[1].isSelected())
			FlowAppMainFrame.chekboxItem[1].setSelected(false);
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowStateChanged(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
