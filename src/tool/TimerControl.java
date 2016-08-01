package tool;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class TimerControl {
	private List<Timer> timerList;
	public static final int SLOW_MODE = 5000;
	public static final int MID_MODE = 3000;
	public static final int FAST_MODE = 1000;
	
	public TimerControl() {
		timerList = new ArrayList<>();
	}
	
	public void addTimer(Timer timer)
	{
		timerList.add(timer);
	}
	
	public void setTimerMode(int mode) {
		for(Timer timer:timerList)
		{
			timer.setDelay(mode);
		}
	}
	
	public void start() {
		for(Timer timer:timerList)
		{
			timer.start();
		}
	}
	
	public int getTimerCount() {
		return timerList.size();
	}
}
