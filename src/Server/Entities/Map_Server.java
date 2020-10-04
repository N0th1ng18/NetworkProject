package Server.Entities;

import java.awt.image.BufferedImage;

import Tools.Input;
import Tools.Vec2;


public class Map_Server {
	int identity = -1;
	
	public Vec2 pos;
	public Vec2 vel;
	
	BufferedImage img;
	
	public Map_Server(float posX, float poxY, BufferedImage img) {
		this.pos = new Vec2(posX, poxY);
		this.vel = new Vec2(0f, 0f);
		this.img = img;

	}

	public void update(double time, double dt) {
		
		//pos.x = pos.x + vel.x;
		//pos.y = pos.y + vel.y;
		
	}
	
}
