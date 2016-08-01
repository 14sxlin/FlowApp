package tool;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.junit.Before;
import org.junit.Test;

public class TestTimeControll {
	private TimerControl controll;
	
	@Before
	public void setUp() {
		controll = new TimerControl();
		Timer timer1= new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("provoke1");
			}
		});
		Timer timer2= new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("provoke2");
			}
		});
		controll.addTimer(timer1);
		controll.addTimer(timer2);
		
	}
	@Test
	public void test() {
		controll.setTimerMode(TimerControl.FAST_MODE);
//		controll.setTimerMode(TimerControl.MID_MODE);
		while(true)
		{
			controll.start();
			controll.setTimerMode(TimerControl.SLOW_MODE);
		}
		
	}

}
