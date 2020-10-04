package Client;
import java.awt.Canvas;

abstract public class GameLoop extends Canvas{
	private static final long serialVersionUID = 1L;
	
	//Loop Variables
	int FPS;
	int UPDATES;
	double updatesPerSecond = 60.0;
	public static boolean running = true;
	boolean showFPS = true;
	boolean limitFPS = true;
	
	
	/*
	 * Abstract Methods
	 */
	abstract void update(double time, double dt);
	abstract void render(double alpha, double dt);
	
	/*
	 * Loop
	 */
	void loop() {

		/**********************************************/
		double time = 0.0;
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
					frameTime = 2.5;//protect against large amounts of updates on slow framtimes
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
			
			alpha = accumulator / dt;//get ratio of accumulator and dt
			render(alpha, dt);
			
			if(showFPS == true) {
				renderCounter++;
			}
			
			
			//try {
			//	Thread.sleep(5);
			//} catch (InterruptedException e) {
			//	e.printStackTrace();
			//}
			//Sleep if update goal is too easy
			/*
			if(limitFPS == true && (dt - accumulator) * 1000.0 >= 5.0) {//if loop has more than 5 milli extra time after updates
				try {
					Thread.sleep((long)((dt - accumulator) * 1000.0));
					System.out.println((dt - accumulator) * 1000.0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			
			
			
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
