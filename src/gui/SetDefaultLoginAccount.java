package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import readwrite.Configure;
import tool.MyLogger;

@SuppressWarnings("serial")
public class SetDefaultLoginAccount extends JDialog implements ActionListener{
	private JComboBox<String> accountSelectCombo;
	private JButton sureButton,cancelButton;
	public SetDefaultLoginAccount(JPanel context,String[] accouts) {
		this(accouts);
		this.setLocationRelativeTo(context);
		this.setVisible(true);
	}
	public SetDefaultLoginAccount(JFrame context,String[] accouts) {
		this(accouts);
		this.setLocationRelativeTo(context);
		this.setVisible(true);
	}

	
	public SetDefaultLoginAccount(String[] accouts) {
		this.setLayout(new GridLayout(2, 1));
		accountSelectCombo=new JComboBox<String>(accouts);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("ȷ��");
		cancelButton=new JButton("ȡ��");
		sureButton.addActionListener(this);
		cancelButton.addActionListener(this);
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setSize(120, 100);
		this.setResizable(false);
		this.setTitle("�Ե��˺�");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e) {
		String a=e.getActionCommand();
		if(a.equals("ȷ��"))
		{	String name = (String) accountSelectCombo.getSelectedItem();
			try {
				Configure.WriteProperties("defaultUser", name);
			} catch (IOException e1) {
				MyLogger.fatal(getClass(), e1.getMessage()+ " set defaultUser fail" );
				e1.printStackTrace();
			}
			this.dispose();
		}
		if(a.equals("ȡ��"))
			this.dispose();
	}
}
