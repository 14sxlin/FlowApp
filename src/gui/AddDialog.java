package gui;

import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import readwrite.AccountManager;

@SuppressWarnings("serial")
public class AddDialog extends InputDialog {
	public AddDialog(JFrame parent,AccountManager am) throws UnsupportedEncodingException  {
		super(parent,am, InputDialog.ADD);
		super.sureButton.removeActionListener(null);
	}

	@Override
	protected void sureAction() {
		inputName = userNameInput.getText().trim();
		inputPsw = new String(passwordInput.getPassword()).trim();
		if(inputName.length()>=4&&
				inputPsw.length()!=0)
		{	
			am.add(inputName, inputPsw);
			am.updateFile();
			this.dispose();
		}
		else JOptionPane.showMessageDialog(parent, "请输入正确的内容");
		JOptionPane.showMessageDialog(parent, "添加成功");
		
	}
}
