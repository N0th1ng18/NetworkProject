package Server.Entities;
import java.awt.Color;
import java.awt.Graphics2D;

import Server.Server;
import Tools.Images;
import Tools.Maps;

public class ServerEntities {
	
	public static int MAX_OBJECTS = 1000;
	public static int MAX_TEXTBOXES = 10;
	public static int MAX_IMAGES = 100;
	public static int MAX_ANIMATIONS = 10;

	
	/*Objects*/
	public static ServerObject[] objects;//should be players & objects are seperate for future updates
	public static int objectCount;
	/*Map*/
	public static Map_Server map;
	
	public static void init() {
		/*Objects*/
		/*LEFT HANDED SYSTEM*/
		objects = new ServerObject[MAX_OBJECTS];
		objectCount = 0;
		//Map
		map = new Map_Server(0f, 0f, Maps.maps.get(0));
	}
	
	public static void update(double time, double dt) {
		//Map
		map.update(time, dt);
		
		//Disable movement for Start of game
		for(int i=0; i < Server.gameState.connections.getNumConnections(); i++) {
			if(Server.gameState.connections.get(i).player.enabled == false && time > Server.START_TIME && Server.gameState.connections.get(i).player.finished == (byte)0) {
				Server.gameState.connections.get(i).player.enabled = true;
			}
		}
		
		
		//UPDATE OBJECTS		
		for(int i=0; i < objectCount; i++) {
			objects[i].update(time, dt);
		}
		//Collision
		collisionDetection();
		sampleMapColor(time);
		
		
	}
	
	public static void collisionDetection() {
		for(int i=0; i < objectCount; i++) {
			/*Map Edge*/
				int x = (int)objects[i].pos.x;
				int y = (int)objects[i].pos.y;
				
				if(x < 0) {
					objects[i].pos.x = 0;
					objects[i].vel.x = -objects[i].vel.x;
				}
				if(x >= map.img.getWidth()) {
					objects[i].pos.x = map.img.getWidth() -1;
					objects[i].vel.x = -objects[i].vel.x;
				}
				
				if(y < 0) {
					objects[i].pos.y = 0;
					objects[i].vel.y = -objects[i].vel.y;
				}
				if(y >= map.img.getHeight()) {
					objects[i].pos.y = map.img.getHeight() -1;
					objects[i].vel.y = -objects[i].vel.y;
				}
			/**************************************************/
		}

	}
	
	public static void sampleMapColor(double time) {
		for(int i=0; i < objectCount; i++) {
			//Sample Map Color
			int x = (int)objects[i].pos.x;
			int y = (int)objects[i].pos.y;
			//if sample position is inside map bounds
			
			if(x >= 0 && x < map.img.getWidth() && y >= 0 && y < map.img.getHeight()) {
				//Get Color
				Color color = new Color(map.img.getRGB(x, y));
				
				//White to Black Colors
				float defaultFric = 2.0f;
				if(color.getRed() == color.getGreen() && color.getGreen() == color.getBlue()) {
					objects[i].movFriction = 5.0f * ((-(float)color.getRGB()) / (-(float)Color.BLACK.getRGB())) + defaultFric;
				}else {
					objects[i].movFriction = defaultFric;
				}
				
				//Purple
				if(color.getRed() == 163 && color.getGreen() == 73 && color.getBlue() == 164 && objects[i].enabled == true) {
					objects[i].finished = (byte)1;
					objects[i].finishTime = time - Server.START_TIME;
					objects[i].enabled = false;
				}
				
			}else {
				objects[i].movFriction = 7.0f;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void addObject(ServerObject o) {
		objects[objectCount] = o;
		objectCount++;
		o.identity = objectCount;
	}
	public static void removeObject(ServerObject o) {
		if(o == null || o.identity < 0 && objectCount <= 0) {
			return;
		}
		
		objects[o.identity] = null;
		objectCount--;
		
		/*
		 * Iterate through objects array to fill in the removed object.
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
	
	
	
	public static void removeAll() {
		objects = new ServerObject[MAX_OBJECTS];
		objectCount = 0;
	}
	
}
