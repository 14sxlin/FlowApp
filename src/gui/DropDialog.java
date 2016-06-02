package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import readwrite.AccountManager;

@SuppressWarnings("serial")
public class DropDialog extends SelectDialog {

	public DropDialog(JFrame jframe, AccountManager am) {
		super(jframe, am,new GridLayout(2, 1));
		super.setSize(150,120);
		super.setVisible(true);
	}

	@Override
	protected void sureButtonAction() {
		int index = accountSelectCombo.getSelectedIndex();
		String name = am.drop(index);
		am.updateFile();
		this.dispose();
		JOptionPane.showMessageDialog(super.parent, "É¾³ýÁË "+name);
	}

}
