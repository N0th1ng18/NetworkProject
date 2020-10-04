package Tools;

public class Timer {
	
	long start;
	
	public Timer() {
		
	}
	
	public void start() {
		start = getCurrentTime();
	}
	
	public double getSeconds() {
		return ((double) (getCurrentTime() - start)) / 1e9;
	}
	
	private long getCurrentTime() {
		return System.nanoTime();
	}
}
