package gui;

import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import readwrite.AccountManager;

@SuppressWarnings("serial")
public abstract class SelectDialog extends JDialog {
	protected  JComboBox<String> accountSelectCombo;
	protected JButton sureButton;
	protected JButton cancelButton;
	protected AccountManager am;
	protected JFrame parent;
	public SelectDialog(JFrame parent,AccountManager am,LayoutManager gl)   {
		this.parent = parent;
		this.am = am;
		this.setLayout(gl);
		this.setTitle("选择账号");	
		accountSelectCombo=new JComboBox<String>(am.usernames);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("确定");
		sureButton.addActionListener(e->{
			sureButtonAction();
		});
		cancelButton=new JButton("取消");
		cancelButton.addActionListener(e->{
			this.dispose();
		});
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(parent);
		this.setSize(200, 200);
		this.setResizable(true);	
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	abstract protected void sureButtonAction();
}
