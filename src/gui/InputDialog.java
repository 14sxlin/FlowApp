package gui;

import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import readwrite.AccountManager;


@SuppressWarnings("serial")
public abstract class InputDialog extends JDialog {
	public static final int ADD = 0;
	public static final int UPDATE = 1;
	public static final String TYPE[]= {"添加账户","修改账户"};
	protected String inputName;
	protected String inputPsw;
	protected JTextField userNameInput;
	protected JPasswordField passwordInput;
	protected JButton sureButton;
	protected JButton cancalButton;
	protected AccountManager am;
	protected JFrame parent;
	
	public InputDialog(JFrame parent,AccountManager am,int type) {
		this.am = am;
		this.parent  = parent;
		this.setAlwaysOnTop(true);
		this.setTitle(TYPE[type]);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setBounds(400, 200, 180, 180);

		JPanel panel1=new JPanel();		
		panel1.add(new JLabel("用户名:"));
		panel1.add((userNameInput=new JTextField(15)));
		
		JPanel panel2=new JPanel();
		panel2.add(new JLabel("密码:"));
		panel2.add((passwordInput=new JPasswordField(15)));
		
		JPanel panel3=new JPanel();
		sureButton=new JButton("确认");
		cancalButton=new JButton("取消");
		sureButton.addActionListener(e->{
			sureAction();
		});
		//取消按钮
		cancalButton.addActionListener(e->{
			this.dispose();
		});
		panel3.add(sureButton);
		panel3.add(cancalButton);
		
		JPanel panel0=new JPanel(new GridLayout(3, 1));
		panel0.add(panel1);
		panel0.add(panel2);
		panel0.add(panel3);
		this.add(panel0);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
	}
	
	abstract protected void sureAction();
}
