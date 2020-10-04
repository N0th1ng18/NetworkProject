package Server.Entities;

import Tools.Input;
import Tools.Vec2;


public class ServerObject {

	int identity = -1;
	
	public Vec2 pos;
	public Vec2 vel;
	public Vec2 accel;
	
	//Input
	public Input input;
	
	//Player Attributes
	public int size = 6;
	private float mass;
	private Vec2 netForce;
	private float movForce;
	private float movTheta;
	public float movFriction;
	private float movAcceleration;
	
	//Current Tick
	public int tick;
	
	//Finish
	public byte finished;
	public double finishTime;
	
	//Enabled
	public boolean enabled;
	
	public ServerObject(float posX, float posY) {
		this.pos = new Vec2(posX, posY);
		this.vel = new Vec2(0f, 0f);
		this.accel = new Vec2(0f, 0f);
		this.netForce = new Vec2(0f, 0f);
		input = new Input();
		
		
		mass = 5.0f;
		movAcceleration = 20.0f;//20
		movFriction = 1.6f;
		
		tick = -1;
		
		finished = (byte)0;
		finishTime = -1;
		
		enabled = false;
	}

	public void update(double time, double dt) {
		//System.out.println(input.getW() + "-" + input.getS() + "-" + input.getA() + "-" + input.getD());
		if(enabled == true) {	
			//Input
			switch(input.getW() + "-" + input.getS() + "-" + input.getA() + "-" + input.getD()) {
			case "0-0-0-0":
				movForce = 0.0f;
				break;
			case "0-0-0-1"://D
				movTheta = 90.0f;
				movForce = movAcceleration;
				break;
			case "0-0-1-0"://A
				movTheta = 270.0f;
				movForce = movAcceleration;
				break;
			case "0-0-1-1":
				movForce = 0.0f;
				break;
			case "0-1-0-0"://S
				movTheta = 0.0f;
				movForce = movAcceleration;
				break;
			case "0-1-0-1":
				movTheta = 45.0f;
				movForce = movAcceleration;
				break;
			case "0-1-1-0":
				movTheta = 315.0f;
				movForce = movAcceleration;
				break;
			case "0-1-1-1":
				movTheta = 0.0f;
				movForce = movAcceleration;
				break;
			case "1-0-0-0"://W
				movTheta = 180.0f;
				movForce = movAcceleration;
				break;
			case "1-0-0-1":
				movTheta = 135.0f;
				movForce = movAcceleration;
				break;
			case "1-0-1-0":
				movTheta = 225.0f;
				movForce = movAcceleration;
				break;
			case "1-0-1-1":
				movTheta = 180.0f;
				movForce = movAcceleration;
				break;
			case "1-1-0-0":
				movForce = 0.0f;
				break;
			case "1-1-0-1":
				movTheta = 90.0f;
				movForce = movAcceleration;
				break;
			case "1-1-1-0":
				movTheta = 270.0f;
				movForce = movAcceleration;
				break;
			case "1-1-1-1":
				movForce = 0.0f;
				break;
			}
			

			resetNetForce();
			//Movement
			addForce(movForce, movTheta);
			//Friction
			addFriction(-vel.x * movFriction, -vel.y * movFriction);
			
			accel.x = netForce.x / mass;
			accel.y = netForce.y / mass;
			vel.x = vel.x + accel.x;
			vel.y = vel.y + accel.y;
			pos.x = pos.x + vel.x;
			pos.y = pos.y + vel.y;
		}
		
	}
	
	public void addForce(float force, float theta){
		theta = (float)Math.toRadians(theta);
		
		netForce.x = (float) (netForce.x + (force * Math.sin(theta)));
		netForce.y = (float) (netForce.y + (force * Math.cos(theta)));
	}
	
	public void addFriction(float x, float y){
		
		netForce.x = netForce.x + x;
		netForce.y = netForce.y + y;
	}
	
	public void addForceVec(float x, float y){
		
		netForce.x = netForce.x + x;
		netForce.y = netForce.y + y;
	}
	
	public void resetNetForce(){
		netForce.x = 0.0f;
		netForce.y = 0.0f;
	}
	
}
