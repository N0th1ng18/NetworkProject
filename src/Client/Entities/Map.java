package Client.Entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import Client.Client;
import Tools.Images;
import Tools.Maps;
import Tools.Timer;
import Tools.Vec2;
import Client.UI.UIManager;


public class Map {
	public int identity = -1;
	
	//From Server
	public Vec2 pos_Srv;
	
	//Image
	public Vec2 offset;
	public Vec2 scale;

	
	
	
	public Map() {
		offset = new Vec2(0f,0f);
		scale = new Vec2(1.0f,1.0f);
		pos_Srv = new Vec2(0.0f, 0.0f);
	}

	public void update(double time, double dt) {
			

	}

	public void render(Graphics2D g2, double alpha) {
		
		AffineTransform oldTransform = g2.getTransform();
		/************************************************/
		
			switch(UIManager.getScreen()) {
			case 4://Game
				
				//g2.scale(scale.x, scale.y);
				g2.translate(pos_Srv.x, pos_Srv.y);
				g2.drawImage(Maps.maps.get(0), (int)(offset.x), (int)(offset.y), null);
				
				
				break;
			}

		/***************************************************/	
		g2.setTransform(oldTransform);
		
	}
	
}
