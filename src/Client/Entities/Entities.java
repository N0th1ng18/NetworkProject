package Client.Entities;
import java.awt.Graphics2D;

public class Entities {
	
	public static int MAX_OBJECTS = 1000;
	public static int MAX_BUTTONS = 10;
	public static int MAX_LABELS = 100;
	public static int MAX_TEXTBOXES = 10;
	public static int MAX_IMAGES = 10;
	public static int MAX_ANIMATIONS = 10;
	
	/*Objects*/
	public static Object[] objects;
	public static int objectCount;
	/*Cameras*/
	public static Camera camera;
	/*Buttons*/
	public static Button[] buttons;
	public static int buttonCount;
	/*Labels*/
	public static Label[] labels;
	public static int labelCount;
	/*Maps*/
	public static Map map;
	/*ScoreBoard*/
	public static ScoreBoard scoreBoard;
	
	public static void init() {
		/*Objects*/
		/*LEFT HANDED SYSTEM*/
		objects = new Object[MAX_OBJECTS];
		objectCount = 0;
		buttons = new Button[MAX_BUTTONS];
		buttonCount = 0;
		labels = new Label[MAX_LABELS];
		labelCount = 0;
		scoreBoard = null;
		map = new Map();
	}
	
	public static void update(double time, double dt) {
		//UPDATE MAPS
		map.update(time, dt);
		//UPDATE CAMERA
		if(camera != null)
			camera.update(time, dt);
		//UPDATE OBJECTS
		for(int i=0; i < objectCount; i++) {
			objects[i].update(time, dt);
		}
		//UPDATE SCOREBOARD
		if(scoreBoard != null)
			scoreBoard.update(time, dt);
		
	}
	
	public static void render(Graphics2D g2, double alpha, double dt) {
		
		if(camera != null)
			camera.render(g2, alpha);
				
				/*Render*/
				
				map.render(g2, alpha);
				
				for(int i=0; i < objectCount; i++) {
					objects[i].render(g2, alpha, dt);
				}
				
				for(int i=0; i < buttonCount; i++) {
					buttons[i].render(g2, alpha);
				}
				
				for(int i=0; i < labelCount; i++) {
					labels[i].render(g2, alpha);
				}

				//-------------------------------
				
		if(camera != null)
			camera.setTransform(g2);
		
		if(scoreBoard != null)
			scoreBoard.render(g2, alpha);
		
	}
	
	
	
	
	
	
	public static void addObject(Object player) {
		objects[objectCount] = player;
		objectCount++;
		player.identity = objectCount;
	}
	
	public static void removeObject(Object o) {
		if(o == null || o.identity < 0 || objectCount <= 0) {
			return;
		}
		
		objects[o.identity] = null;
		objectCount--;
		
		/*
		 * Iterate through array to fill in the removed object.
		 */

		for(int i=o.identity; i < objects.length - o.identity; i++) {
			if(objects[i] == null && objects[i+1] == null) {
				break;
			}
			
			objects[i] = objects[i+1];
			objects[i+1] = null;
			objects[i].identity -= 1;
		}
		
	}
	
	public static void addButton(Button b) {
		buttons[buttonCount] = b;
		buttonCount++;
		b.identity = buttonCount;
	}
	public static void removeButton(Button b) {
		buttons[b.identity] = null;
		buttonCount--;
		
		/*
		 * Iterate through array to fill in the removed object.
		 */
		for(int i=b.identity; i < buttons.length - b.identity; i++) {
			if(buttons[i] == null && buttons[i+1] == null) {
				break;
			}
			
			buttons[i] = buttons[i+1];
			buttons[i+1] = null;
			buttons[i].identity -= 1;
		}
	}
	
	public static void addLabel(Label l) {
		labels[labelCount] = l;
		labelCount++;
		l.identity = labelCount;
	}
	public static void removeLabel(Label l) {
		labels[l.identity] = null;
		labelCount--;
		
		/*
		 * Iterate through array to fill in the removed object.
		 */
		for(int i=l.identity; i < labels.length - l.identity; i++) {
			if(labels[i] == null && labels[i+1] == null) {
				break;
			}
			
			labels[i] = labels[i+1];
			labels[i+1] = null;
			labels[i].identity -= 1;
		}
	}
	
	
	public static void removeAll() {
		objects = new Object[MAX_OBJECTS];
		objectCount = 0;
		buttons = new Button[MAX_BUTTONS];
		buttonCount = 0;
		labels = new Label[MAX_LABELS];
		labelCount = 0;
		//scoreBoard = null;
		map = new Map();
		camera = null;
	}
	
}
