package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
//import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import readwrite.MusicList;
import readwrite.ResourcePath;
import tool.MusicPlayer;

@SuppressWarnings("serial")
public class AlarmSettingDialog extends JDialog implements ActionListener {
	public JSpinner alarmSpin;
	public JComboBox<String> alarmCombo;
	public JButton tryLisButton;
	public JButton sureButton;
	private JButton cancelButton;
	private SpinnerNumberModel model;
//	private MusicChooser musicChooser;
	private int maxFlow=800;//��¼��������,Ĭ��800M
	public File musicFile;
	private MusicList musicList;
	private MusicPlayer music;
	public static String musicPath;
	public static int alarmAmount;
	private JFrame parent;
	public AlarmSettingDialog(JFrame parent,int maxFlow) throws UnsupportedEncodingException {
		this.parent = parent;
		//�������
		this.setTitle("��������");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setBounds(450, 250, 200, 200);
		this.setAlwaysOnTop(true);
		this.setLayout(new GridLayout(4, 1));
		this.maxFlow=maxFlow;
//		musicChooser=new MusicChooser();		
		model=new SpinnerNumberModel((this.maxFlow-100)/100*100, 0, this.maxFlow, 50);
		alarmSpin=new JSpinner(model);
		
		JPanel temppanel=new JPanel();
		temppanel.add(new JLabel("���Ѷ��:"));
		temppanel.add(alarmSpin);
		temppanel.add(new JLabel("M"));
		this.add(temppanel);
		
		this.add(new JLabel("��������:"));
		JPanel panel=new JPanel();
		musicList=new MusicList(ResourcePath.JARPATH);
		if(musicList.strMusicList.length==0)
		{	JOptionPane.showMessageDialog(parent, "��ǰ�ļ���û��(.wav)�����ļ�");
			this.dispose();
		}	
		else
		{	alarmCombo=new JComboBox<String>(musicList.strMusicList);
			panel.add(alarmCombo);
			tryLisButton=new JButton("����");
			panel.add(tryLisButton);
			tryLisButton.addActionListener(this);
//		alarmSpin.setMaximumSize(new Dimension(280, 50));
//		alarmSpin.setPreferredSize(new Dimension(50, 28));
		alarmCombo.setPreferredSize(new Dimension(120, 28));
			this.add(panel);
			JPanel panel1=new JPanel();
			sureButton=new JButton("ȷ��");
			sureButton.addActionListener(this);
			cancelButton=new JButton("ȡ��");
			cancelButton.addActionListener(this);
			panel1.add(sureButton);
			panel1.add(cancelButton);
			this.add(panel1);
			this.setResizable(false);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setVisible(true);
		}
	}
	
	public void setMaxFlow(int maxFlow)
	{
		this.maxFlow=maxFlow;
		model.setMaximum(this.maxFlow);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("ȡ��"))
			this.dispose();
		if(e.getActionCommand().equals("����"))
		{
			String name=(String) alarmCombo.getSelectedItem();
			if(name!=null)
			{	try {
				music=new MusicPlayer(musicList.hashMap.get(name),true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				music.play();
				music.showControlPanel(parent, "�밴ֹͣ�ر�����");
			}
		}
		if(e.getActionCommand().equals("ȷ��"))
		{
			musicPath=(String) alarmCombo.getSelectedItem();
//System.out.println(musicIndex);
			try{	alarmAmount=(Integer) alarmSpin.getValue();}
			catch(NumberFormatException ex) { JOptionPane.showMessageDialog(this, "�������");		};
			this.dispose();
			ButtonAreaPanel.alarmhasSet=true;
		}	
	}

}
