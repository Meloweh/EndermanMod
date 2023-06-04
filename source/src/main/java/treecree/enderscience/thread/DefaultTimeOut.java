package treecree.enderscience.thread;

import java.util.Random;

public class DefaultTimeOut {
	private long val;
	private long duration;
	private boolean toggled;
	
	public boolean isToggled() {
		return toggled;
	}

	public DefaultTimeOut(final long duration){
		reset(duration);
	}
	
	private final long getCurrentTimeVal() {
		return System.currentTimeMillis();
	}
	
	public void restart() {
		val = getCurrentTimeVal();
		this.toggled = true;
	}
	
	public void reset() {
		reset(this.duration);
	}
	
	public void reset(final long duration) {
		this.val = 0;
		this.duration = duration;
		this.toggled = false;
	}
	
	public boolean finished() {
		final long delta = getCurrentTimeVal() - val;
		
		this.toggled = !(this.duration <= delta);
		
		return !this.toggled;
	}
}
