package Server;
import java.awt.Canvas;

abstract public class ServerLoop extends Canvas{
	private static final long serialVersionUID = 1L;
	
	//Loop Variables
	int FPS;
	int UPDATES;
	double updatesPerSecond = 30.0;	//Also Packets Per Second
	public volatile boolean running = true;
	boolean showFPS = true;
	boolean limitFPS = true;
	public static double time;
	
	
	/*
	 * Abstract Methods
	 */
	abstract void update(double time, double dt);
	abstract void render(double alpha);
	
	/*
	 * Loop
	 */
	void loop() {

		/**********************************************/
		time = 0.0;
		double dt = 1.0/updatesPerSecond;
		
		double accumulator = 0.0;
		double alpha = accumulator / dt;
		
		double newFrameTime = getNanoTimeInSeconds();
		double oldFrameTime = getNanoTimeInSeconds();
		double frameTime;
		/***********************************************/
		
		/*FPS*/
		double fps = getNanoTimeInSeconds();
		int renderCounter = 0;
		int updateCounter = 0;
		/***************************************/
		while(running) {
			newFrameTime = getNanoTimeInSeconds();
			frameTime = newFrameTime - oldFrameTime;
			oldFrameTime = newFrameTime;
			
			/*
			 * Spiral of Death
			 */
				if(frameTime > 2.5) {
					frameTime = 2.5;
				}
			//*****************************
			
			accumulator += frameTime;/*Adding length of frame*/
			
			while(accumulator >= dt) {
				
				update(time, dt);
				
				if(showFPS == true) {
					updateCounter++;
				}
				
				time += dt;
				accumulator -= dt;/*subtracting dt intervals*/
			}
			
			alpha = accumulator / dt;
			
			render(alpha);
			
			if(showFPS == true) {
				renderCounter++;
			}
			
			//Sleep if update goal is too easy
			/*
			if(limitFPS == true && (dt - accumulator) * 1000.0 >= 5.0) {//if loop has more than 5 milli extra time after updates
				try {
					Thread.sleep((long)((dt - accumulator) * 1000.0));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}*/
			
			if(showFPS == true && (getNanoTimeInSeconds() - fps) >= 1.0) {
				FPS = renderCounter;
				UPDATES = updateCounter;
				renderCounter = 0;
				updateCounter = 0;
				fps = getNanoTimeInSeconds();
			}
			
		}
	}
	
	
	/*
	 * Other Methods
	 */
	private double getNanoTimeInSeconds() {
		return System.nanoTime()/(1000000000.0);
	}
}
