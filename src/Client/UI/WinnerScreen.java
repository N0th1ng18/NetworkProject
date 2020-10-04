package Client.UI;

import java.awt.Color;
import java.awt.Font;

import Client.Client;
import Client.Entities.Entities;
import Client.Entities.Label;

public class WinnerScreen {
	public static boolean isLoaded = false;
	
	private static Label winnerName;
	
	public static void load() {
		isLoaded = true;
		//Create GameTime Label
		winnerName = new Label("", Client.gWidth/2,  Client.gHeight/2 -200, Client.WIDTH, Client.HEIGHT, new Font("TimesRoman", Font.PLAIN, 110), Color.ORANGE);
		Entities.addLabel(winnerName);
		
	}
	
	public static void unload() {
		isLoaded = false;
		Entities.removeLabel(winnerName);
	}
	
	public static void setWinnerName(String name) {
		Entities.labels[0].str = "The Winner is "+name+"!";
	}
}
