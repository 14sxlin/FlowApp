package lin.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import lin.readwrite.AccountManager;


@SuppressWarnings("serial")
/*
 * ��������޸ĵ�ѡ���ʱ����ʾ�����
 */
public class UpdateAccountDialog extends SelectDialog {
	private JPasswordField pswField;
	public UpdateAccountDialog(JFrame jframe,AccountManager am)   {
		super(jframe,am,new GridLayout(4, 1));
		super.setTitle("�޸�����");
		pswField = new JPasswordField();
		this.add(pswField);
		JPanel tempPanel=new JPanel();
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(jframe);
		this.setSize(200, 200);
		this.setResizable(true);	
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	protected void sureButtonAction() {
		int index = accountSelectCombo.getSelectedIndex();
		am.update(index, am.usernameList.get(index), new String(pswField.getPassword()));
		am.updateFile();
		this.dispose();
		JOptionPane.showMessageDialog(super.parent, "�޸ĳɹ�");
	}
}
