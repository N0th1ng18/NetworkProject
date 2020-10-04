package Client.Entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Client.Client;
import Client.UI.UIManager;


public class Camera {
	public int identity = -1;
	
	public Object obj;	
	
	//Transform
	AffineTransform transform;
	Graphics2D g2;
	
	
	public Camera(Object obj) {
		
		this.obj = obj;
		
	}

	public void update(double time, double dt) {
		//Follow other players that havent finished
		if(obj != null && obj.finishTime >= 0) {
			for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
				if(Client.gameState.connections.get(i).player.finishTime < 0) {
					obj = Client.gameState.connections.get(i).player;
				}
			}
		}
		
	}
	

	public void render(Graphics2D g2, double alpha) {
		transform = g2.getTransform();
		/************************************************/
		
		
			switch(UIManager.getScreen()) {
			case 4://Game
				
				//Follow Object
				g2.translate(-(obj.posI.x + obj.size/2) + Client.gWidth/2, -(obj.posI.y + obj.size/2) + Client.gHeight/2);
				
				
				break;
			}

		/***************************************************/	
		
	}
	

	
	
	public void setTransform(Graphics2D g2) {
		g2.setTransform(transform);
	}
	
}
