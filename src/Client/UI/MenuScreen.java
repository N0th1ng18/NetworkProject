package Client.UI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Client.Client;
import Client.MouseInput;
import Client.Entities.Animation;
import Client.Entities.Button;
import Client.Entities.Entities;
import Client.Entities.Label;
import Client.Entities.Object;
import Tools.Images;

public class MenuScreen {
	
	private static Font font = new Font("TimesRoman", Font.PLAIN, 60);
	static Random rand = new Random();
	
	private static Object[] guys;
	private static Button multiplayer;
	private static Button settings;
	private static Button quit;
	private static Label title;
	
	public static boolean isLoaded = false;
	
	public static void load() {
		isLoaded = true;
		
		
		BufferedImage imgRGB = Images.get(0);
		//pixel array
		guys = new Object[(((Client.gWidth-600+100)/200) * ((Client.gHeight+100)/200))];
		int iterator = 0;
		for(int i=0; i < ((Client.gWidth-600+100)/200); i++) {
			for(int j=0; j < ((Client.gHeight+100)/200); j++) {
				Object guy = new Object(rand.nextInt(3));
				guys[iterator] = guy;
				guy.pos.x = i * 200f + 500 + 100;
				guy.pos.y = j * 200f + 50;
				
				Animation redGuyAnimation = new Animation(4, 0);
				redGuyAnimation.addImage(imgRGB, 0, 0, 100, 100);
				redGuyAnimation.addImage(imgRGB, 101, 0, 100, 100);
				redGuyAnimation.addImage(imgRGB, 202, 0, 100, 100);
				redGuyAnimation.addImage(imgRGB, 303, 0, 100, 100);
				guy.addAnimation(redGuyAnimation);	
				
				Animation blueGuyAnimation = new Animation(4, 0);
				blueGuyAnimation.addImage(imgRGB, 0, 102, 100, 100);
				blueGuyAnimation.addImage(imgRGB, 101, 102, 100, 100);
				blueGuyAnimation.addImage(imgRGB, 202, 102, 100, 100);
				blueGuyAnimation.addImage(imgRGB, 303, 102, 100, 100);
				guy.addAnimation(blueGuyAnimation);
				
				Animation greenGuyAnimation = new Animation(4, 0);
				greenGuyAnimation.addImage(imgRGB, 0, 203, 100, 100);
				greenGuyAnimation.addImage(imgRGB, 101, 203, 100, 100);
				greenGuyAnimation.addImage(imgRGB, 202, 203, 100, 100);
				greenGuyAnimation.addImage(imgRGB, 303, 203, 100, 100);
				guy.addAnimation(greenGuyAnimation);
				
				
				Entities.addObject(guy);
				iterator++;
			}
		}
		
		multiplayer = new Button(100, 250, 400, 100, Color.GRAY, Color.BLACK, "Multiplayer", font);
		Entities.addButton(multiplayer);
		
		settings = new Button(100, 400, 400, 100, Color.GRAY, Color.BLACK, "Settings", font);
		Entities.addButton(settings);
		
		quit = new Button(100, 550, 400, 100, Color.GRAY, Color.BLACK, "Quit", font);
		Entities.addButton(quit);
		
		title = new Label(Images.get(1), 100, 80, 400, 100);
		Entities.addLabel(title);
	}
	
	public static void unload() {
		isLoaded = false;
		
		for(int i=0; i < guys.length; i++) {
			Entities.removeObject(guys[i]);
		}
		guys = null;
		Entities.removeButton(multiplayer);
		Entities.removeButton(settings);
		Entities.removeButton(quit);
		Entities.removeLabel(title);
	}
	
	public static void setAnimations(float x, float y) {
		//Object Animation Based On Mouse
		for(int i=0; i < Entities.objectCount; i++) {
			float dx = Entities.objects[i].pos.x + 50 - x;
			float dy = Entities.objects[i].pos.y + 50 - y;
			
			if(dx >= 0 && dy >= 0) {
				Entities.objects[i].setAnimationImage(Entities.objects[i].animationSelector, 0);
			}else if(dx >= 0 && dy < 0) {
				Entities.objects[i].setAnimationImage(Entities.objects[i].animationSelector, 1);
			}else if(dx < 0 && dy < 0) {
				Entities.objects[i].setAnimationImage(Entities.objects[i].animationSelector, 3);
			}else if(dx < 0 && dy >= 0) {
				Entities.objects[i].setAnimationImage(Entities.objects[i].animationSelector, 2);
			}
		}
	}
	
	public static void checkButtons(float x, float y) {
		//Check Buttons
		for(int i=0; i < Entities.buttonCount; i++) {
			if(Entities.buttons[i].checkBounds(x, y) == true) {
				Entities.buttons[i].setBColor(Color.WHITE);
				
				if(MouseInput.leftPressed == true) {
					switch(Entities.buttons[i].identity) {
					case 1://Multiplayer
						UIManager.goTo(UIManager.MULTIPLAYER_SCREEN);
						MouseInput.leftPressed = false;
						break;
					case 2://Settings
						UIManager.goTo(UIManager.SETTING_SCREEN);
						MouseInput.leftPressed = false;
						break;
					case 3://Quit
						
						Client.running = false;
						MouseInput.leftPressed = false;
						break;
					}
				}
			}else {
				Entities.buttons[i].setDefaultColor();
			}
		}
	}
	

}
