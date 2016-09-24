package gui;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
public class MusicPlayerWithDialog implements ActionListener, WindowListener {

	private AudioStream as;	
	private JDialog controlPanel;
	private boolean asCompenont;
	private boolean state;
	
	/**
	 * 
	 * @param musicStream ��������wav�����ļ���
	 * @param asCompenont �Ƿ���Ϊ���,���������Ļ�,��ı�״̬
	 * @param state �������ĳ�ʼ״̬,���Ž���������״̬
	 * @throws IOException
	 */
	public MusicPlayerWithDialog(InputStream musicStream,boolean asCompenont,boolean state) throws IOException {
		this.asCompenont=asCompenont;
		this.state = state;
		as=new AudioStream(musicStream);
	}
	
	/**
	 * 
	 * @param f wav�ļ�
	 * @param asCompenont �Ƿ���Ϊ���,���������Ļ�,��ı�״̬
	 * @param state �������ĳ�ʼ״̬,���Ž���������״̬
	 * @throws IOException
	 */
	public MusicPlayerWithDialog(File f,boolean asCompenont,boolean state) throws IOException {
		this(new FileInputStream(f),asCompenont,state);
	}
	public MusicPlayerWithDialog(String musicDirectory)
	{
		try {
			InputStream in=this.getClass().getClassLoader().getResourceAsStream(musicDirectory);
			as=new AudioStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void play()
	{
		AudioPlayer.player.start(as);
	}
	public void close() throws IOException
	{
		AudioPlayer.player.stop(as);
		as.close();
	}
	public void showControlPanel(JFrame parent,String message)
	{
		controlPanel=new JDialog();
		controlPanel.setAlwaysOnTop(true);
		controlPanel.setModalityType(ModalityType.TOOLKIT_MODAL);
		controlPanel.setLocationRelativeTo(parent);
		controlPanel.setLayout(new GridLayout(2, 1));
		JLabel temp=new JLabel(message);
		temp.setForeground(Color.red);
		controlPanel.add(temp);
		JButton button=new JButton("ֹͣ");
		button.addActionListener(this);
		controlPanel.add(button);
		controlPanel.addWindowListener(this);
		controlPanel.setSize(40, 90);
		
		controlPanel.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("ֹͣ"))
		{	try {				
				if(asCompenont)
					DisplayControlPanel.alarmhasSet=false;
				this.close();
				controlPanel.dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
		try {
			if(asCompenont)
			{
				state = !state;
			}
			this.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

}
