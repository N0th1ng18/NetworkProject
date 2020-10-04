package Client.Entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import Client.Client;
import Client.UI.UIManager;

public class ScoreBoard {
	private Label title;
	private Label title_pos;
	private Label title_name;
	private Label title_time;
	
	
	//newly added
	private ArrayList<P_Score> pscore;
	
	private Label[]	player_pos;
	private Label[]	player_name;
	private Label[]	player_time;
	
	private int x;
	private int y;
	private int w = 300;
	private int h = 100;
	private int lineGap = 20;
	private int tabSpace = w/3;
	private Font font = new Font("TimesRoman", Font.PLAIN, 20);
	private Color color = Color.WHITE;
	
	//Render Details
	AlphaComposite transparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);

	
	
	public ScoreBoard(int x, int y) {
		this.x = x;
		this.y = y;
		
		title = new Label("SCORE", x, y, w, h, font, color);
		title.centerY = false;
		title_pos  = new Label("Pos", x + 4					, y + (1*lineGap), w, h, font, color);
		title_pos.centerX = false;
		title_pos.centerY = false;
		title_name  = new Label("Name", x + (1*tabSpace) - 50, y + (1*lineGap), w, h, font, color);
		title_name.centerX = false;
		title_name.centerY = false;
		title_time  = new Label("Time", x + (3*tabSpace) - 47, y + (1*lineGap), w, h, font, color);
		title_time.centerX = false;
		title_time.centerY = false;
		
		player_pos = new Label[Client.gameState.connections.getNumConnections()];
		player_name = new Label[Client.gameState.connections.getNumConnections()];
		player_time = new Label[Client.gameState.connections.getNumConnections()];
		
		pscore = new ArrayList<P_Score>();
		
	}
	
	public void update(double time, double dt) {
		switch(UIManager.getScreen()) {
		case 4://Game
			//Reset
			pscore = new ArrayList<P_Score>();
			
			//Create ArrayList<P_Score>
			for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
				//Remove players that have not finished yet
				if(Client.gameState.connections.get(i).player.finishTime >= 0)
					pscore.add(new P_Score(Client.gameState.connections.get(i).getID(), Client.gameState.connections.get(i).getName(), Client.gameState.connections.get(i).player.finishTime));
			}
			
			//Sort List based on finish time using BUBBLE SORT
			for(int i=0; i < pscore.size() -1; i++) {
				for(int j=0; j < pscore.size() - i - 1; j++) {
					if(pscore.get(j).time > pscore.get(j+1).time) {
						P_Score temp = new P_Score(pscore.get(j).iD, pscore.get(j).name, pscore.get(j).time);
						P_Score j_p1 = new P_Score(pscore.get(j+1).iD, pscore.get(j+1).name, pscore.get(j+1).time);
		
						pscore.set(j, j_p1);
						pscore.set(j+1, temp);
					}
				}
			}
			
			for(int i=0; i < pscore.size(); i++) {
				player_pos[i] = new Label(""+(i+1), x+4, y + ((i+2) * lineGap), w, h, font, color);
				player_pos[i].centerX = false;
				player_pos[i].centerY = false;
				
				player_name[i] = new Label(
						pscore.get(i).name.substring(0, Math.min(Client.MAX_NAME_LENGTH, pscore.get(i).name.length()))
						, x + (1*tabSpace) - 50, y + ((i+2) * lineGap), w, h, font, color);
				player_name[i].centerX = false;
				player_name[i].centerY = false;
				
				String time_round = String.format("%.3f", pscore.get(i).time);
				
				player_time[i] = new Label(time_round.substring(0, Math.min(time_round.length(), 5)), x + (3*tabSpace) - 48, y + ((i+2) * lineGap), w, h, font, color);
				player_time[i].centerX = false;
				player_time[i].centerY = false;
			}
			
			
			break;
			
		case 5://Game Over
			
			//Reset
			pscore = new ArrayList<P_Score>();
			
			//Create ArrayList<P_Score>
			for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
				//Remove players that have not finished yet
				if(Client.gameState.connections.get(i).player.finishTime >= 0)
					pscore.add(new P_Score(Client.gameState.connections.get(i).getID(), Client.gameState.connections.get(i).getName(), Client.gameState.connections.get(i).player.finishTime));
			}
			
			//Sort List based on finish time using BUBBLE SORT
			for(int i=0; i < pscore.size() -1; i++) {
				for(int j=0; j < pscore.size() - i - 1; j++) {
					if(pscore.get(j).time > pscore.get(j+1).time) {
						P_Score temp = new P_Score(pscore.get(j).iD, pscore.get(j).name, pscore.get(j).time);
						P_Score j_p1 = new P_Score(pscore.get(j+1).iD, pscore.get(j+1).name, pscore.get(j+1).time);
		
						pscore.set(j, j_p1);
						pscore.set(j+1, temp);
					}
				}
			}
			
			for(int i=0; i < pscore.size(); i++) {
				player_pos[i] = new Label(""+(i+1), x+4, y + ((i+2) * lineGap), w, h, font, color);
				player_pos[i].centerX = false;
				player_pos[i].centerY = false;
				
				player_name[i] = new Label(
						pscore.get(i).name.substring(0, Math.min(Client.MAX_NAME_LENGTH, pscore.get(i).name.length()))
						, x + (1*tabSpace) - 50, y + ((i+2) * lineGap), w, h, font, color);
				player_name[i].centerX = false;
				player_name[i].centerY = false;
				
				String time_round = String.format("%.3f", pscore.get(i).time);
				
				player_time[i] = new Label(time_round.substring(0, Math.min(time_round.length(), 5)), x + (3*tabSpace) - 48, y + ((i+2) * lineGap), w, h, font, color);
				player_time[i].centerX = false;
				player_time[i].centerY = false;
			}
			
			break;
		}
	}
	
	public void render(Graphics2D g2, double alpha) {
		AffineTransform oldTransform = g2.getTransform();
		/************************************************/
		
		
			switch(UIManager.getScreen()) {
			case 4://Game
				g2.setComposite(transparent);
				g2.setColor(Color.LIGHT_GRAY);
				g2.fillRoundRect(x, y-4, w+1, ((pscore.size()+2) * lineGap)+4, 10, 10);
				g2.setComposite(Client.defaultTrans);
				
				title.render(g2, alpha);
				title_pos.render(g2, alpha);
				title_name.render(g2, alpha);
				title_time.render(g2, alpha);
				
				g2.drawLine(x, y+lineGap-4, x+w,y+lineGap-4);
				
				for(int i=0; i < pscore.size(); i++) {
					player_pos[i].render(g2, alpha);
					player_name[i].render(g2, alpha);
					player_time[i].render(g2, alpha);
				}
				
				break;
			case 5://GameOver
				g2.setComposite(transparent);
				g2.setColor(Color.LIGHT_GRAY);
				g2.fillRoundRect(x, y-4, w+1, ((pscore.size()+2) * lineGap)+4, 10, 10);
				g2.setComposite(Client.defaultTrans);
				
				title.render(g2, alpha);
				title_pos.render(g2, alpha);
				title_name.render(g2, alpha);
				title_time.render(g2, alpha);
				
				g2.drawLine(x, y+lineGap-4, x+w,y+lineGap-4);
				
				for(int i=0; i < pscore.size(); i++) {
					player_pos[i].render(g2, alpha);
					player_name[i].render(g2, alpha);
					player_time[i].render(g2, alpha);
				}
				
				break;
			}

		/***************************************************/	
		g2.setTransform(oldTransform);

	}
	
}

class P_Score{
	int iD;
	String name;
	double time;
	
	public P_Score(int iD, String name, double time) {
		this.iD = iD;
		this.name = name;
		this.time = time;
	}
}





















