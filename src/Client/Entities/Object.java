package Client.Entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import Client.Client;
import Tools.Timer;
import Tools.Vec2;
import Client.UI.UIManager;


public class Object {

	public int identity = -1;
	
	private String name;
	
	//Client Prediction - Not Implemented
		public boolean isClient;
		//Position
		public Vec2 pos;
		public Vec2 vel;
		public Vec2 accel;
		
		public int size;
		private float mass;
		private Vec2 netForce;
		private float movForce;
		private float movTheta;
		private float movFriction;
		private float movAcceleration;
	
	//From Server
	public Vec2 pos_Srv;
	public Vec2 posOld_Srv;
		
	//Interpolation
	public Vec2 posI;
	public int total_num_Client_Updates;
	public int current_Client_Update;
	
	//Image
	public Vec2 offset;
	public Vec2 scale;
	
	//Player Color
	private int color = 0; //0-red, 1-green, 2-blue
	
	//Animation
	private Animation[] animations = new Animation[Entities.MAX_ANIMATIONS];
	private int animationCount = 0;
	public int animationSelector;
	
	//Random Numbers
	Random rand;
	Random rand2;
	
	//Other
	int tickCount = 0;
	
	//Lastest Tick
	public int tick;
	
	//Finish Time
	public double finishTime;
	
	//Render Details
	AlphaComposite transparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);

	
	
	
	public Object() {
		offset = new Vec2(0f,0f);
		scale = new Vec2(1.0f,1.0f);
		pos = new Vec2(0.0f, 0.0f);
		pos_Srv = new Vec2(0.0f, 0.0f);
		posI = new Vec2(0f, 0f);
		vel = new Vec2(0.0f, 0.0f);
		accel = new Vec2(0f, 0f);
		posOld_Srv = new Vec2(0f, 0f);
		
		
		rand = new Random();
		rand2 = new Random();
		this.animationSelector = 0;
		
		color = 0;
		
		this.netForce = new Vec2(0f, 0f);
		mass = 10.0f;
		movAcceleration = 105.0f;
		movFriction = 2.0f;
		
		tick = -1;
		

		
		finishTime = -1.0;
	}
	public Object(int animationSelector) {
		offset = new Vec2(0f,0f);
		scale = new Vec2(1.0f,1.0f);
		pos = new Vec2(0.0f, 0.0f);
		pos_Srv = new Vec2(0.0f, 0.0f);
		posI = new Vec2(0f, 0f);
		vel = new Vec2(0.0f, 0.0f);
		accel = new Vec2(0f, 0f);
		posOld_Srv = new Vec2(0f, 0f);
		
		
		rand = new Random();
		rand2 = new Random();
		this.animationSelector = animationSelector;
		
		color = 0;
		
		this.netForce = new Vec2(0f, 0f);
		mass = 10.0f;
		movAcceleration = 105.0f;
		movFriction = 2.0f;
		
		tick = -1;
	}

	public void update(double time, double dt) {
		tickCount++;
		
		switch(UIManager.getScreen()) {
		case 0://Menu Screen
			if(tickCount % 120 == rand2.nextInt(120)) {
				animationSelector = rand.nextInt(3);
			}
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4://Game
			
			//Count dt intervals that have passed between server updates for interpolation
			current_Client_Update++;
			break;
		}
		
		

	}

	public void render(Graphics2D g2, double alpha, double dt) {
		
		AffineTransform oldTransform = g2.getTransform();
		/************************************************/
		
		
			switch(UIManager.getScreen()) {
			case 0://Menu Screen
	
				g2.scale(scale.x, scale.y);
				g2.translate(pos.x, pos.y);
				g2.drawImage(animations[animationSelector].getImg(), (int)(offset.x), (int)(offset.y), 100, 100, null);
				
				break;
			case 1:
				break;
			case 2:
				break;
			case 3://Lobby Screen
				
				g2.scale(scale.x, scale.y);
				g2.translate(pos.x, pos.y);
				g2.drawImage(animations[animationSelector].getImg(), (int)(offset.x), (int)(offset.y), 100, 100, null);
				
				
				break;
			case 4://Game
				
				switch(color) {
				case 0:
					g2.setColor(Color.RED);
					break;
				case 1:
					g2.setColor(Color.GREEN);
					break;
				case 2:
					g2.setColor(Color.BLUE);
					break;
				}

				/*
				 * Interpolation - Smoothly transitions between server updates
				 */
				posI.x = (float) (posOld_Srv.x + ((pos_Srv.x - posOld_Srv.x) / total_num_Client_Updates)*(alpha + current_Client_Update));
				posI.y = (float) (posOld_Srv.y + ((pos_Srv.y - posOld_Srv.y) / total_num_Client_Updates)*(alpha + current_Client_Update));
				
				
				g2.scale(scale.x, scale.y);
				g2.translate(posI.x, posI.y);
				
				//player object
				g2.fillRect(-size/2, -size/2, size, size);
				
				
				//Name
				g2.setComposite(transparent);
				Vec2 namePos = centerText(name, 0 - size/2, 0 - size/2 - 2*size, size, size, g2);
				g2.drawString(name, namePos.x, namePos.y);
				g2.setComposite(Client.defaultTrans);
				
				
				break;
			}

		/***************************************************/	
		g2.setTransform(oldTransform);
		
		/*
		AffineTransform oldTransform2 = g2.getTransform();
		g2.translate(pos_Srv.x, pos_Srv.y);
		//player object
		g2.setColor(Color.BLUE);
		g2.fillRect(-size/2, -size/2, size, size);
		g2.setTransform(oldTransform2);
		*/
		
	}
	
	public void addAnimation(Animation a) {
		animations[animationCount] = a;
		animationCount++;
	}
	
	public void playAnimation(int aNum, double time, double frameRatePerSec) {
		animations[aNum].play(time, frameRatePerSec);
	}
	
	public void setAnimationImage(int aNum, int imageNum) {
		animations[aNum].setImg(imageNum);
	}
	
	
	
	
	
	
	
	
	public void addForce(float force, float theta){
		theta = (float)Math.toRadians(theta);
		
		netForce.x = (float) (netForce.x + (force * Math.sin(theta)));
		netForce.y = (float) (netForce.y + (force * Math.cos(theta)));
	}
	
	public void addForceVec(float x, float y){
		
		netForce.x = netForce.x + x;
		netForce.y = netForce.y + y;
	}
	
	public void resetNetForce(){
		netForce.x = 0.0f;
		netForce.y = 0.0f;
	}
	
	
	
	//Getters and Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	//Used for interpolation
	public void resetNumClientUpdates() {
		total_num_Client_Updates = current_Client_Update + 1;
		current_Client_Update = -1;
	}
	
	public Vec2 centerText(String str, int x, int y, int w, int h, Graphics2D g2) {
		Vec2 string = new Vec2(0f, 0f);
		FontMetrics met = g2.getFontMetrics();
		string.x = x + (w - met.stringWidth(str)) / 2;
		string.y = y + ((h - met.getHeight()) / 2) + met.getAscent();
		
		return string;
	}
	
}
