package Client.UI;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import Client.Client;
import Client.MouseInput;
import Client.Entities.Animation;
import Client.Entities.Button;
import Client.Entities.Entities;
import Client.Entities.Label;
import Client.Entities.Object;
import Tools.Images;
import Tools.IpChecker;

public class LobbyScreen {
	
	private static Font font = new Font("TimesRoman", Font.PLAIN, 60);
	static Random rand = new Random();
	
	private static Object[] guys;
	private static Label[] guyLabels;
	private static Button showInternalIP;
	private static Button showExternalIP;
	private static Button changeColor;
	private static Button backButton;
	private static Button readyButton;
	
	private static boolean isShowingInternalIP;
	private static boolean isShowingExternalIP;
	
	public static boolean isLoaded = false;
	public static String externalIP;
	
	public static void load() {
		isLoaded = true;
		
		BufferedImage imgRGB = Images.get(0);
		guys = new Object[Client.gameState.connections.getNumConnections()];
		guyLabels = new Label[Client.gameState.connections.getNumConnections()];
		for(int i=0; i < guys.length; i++) {
			Object guy = new Object();
			guys[i] = guy;
			guy.pos.x = i * 200f + 500f + 100f;
			guy.pos.y = 250f;
			
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
			
			Label guyLabel = new Label(Client.gameState.connections.get(i).getName(), i * 200 + 600, 200, 100, 1, font, Color.WHITE);
			guyLabels[i] = guyLabel;
			Entities.addLabel(guyLabel);
		}
		
		showInternalIP = new Button(100, 100, 400, 100, Color.GRAY, Color.BLACK, "Internal IP", font);
		isShowingExternalIP = false;
		Entities.addButton(showInternalIP);
		
		showExternalIP = new Button(100, 250, 400, 100, Color.GRAY, Color.BLACK, "External IP", font);
		isShowingInternalIP = false;
		Entities.addButton(showExternalIP);
		
		changeColor = new Button(100, 400, 400, 100, Color.GRAY, Color.BLACK, "Change Color", font);
		Entities.addButton(changeColor);
		
		backButton = new Button(100, 550, 400, 100, Color.GRAY, Color.BLACK, "Back", font);
		Entities.addButton(backButton);

		readyButton = new Button(1000, 550, 400, 100, Color.RED, Color.BLACK, "Ready", font);
		Entities.addButton(readyButton);
	}
	
	public static void unload() {
		isLoaded = false;
		
		for(int i=0; i < guys.length; i++) {
			Entities.removeObject(guys[i]);
		}
		guys = null;
		for(int i=0; i < guyLabels.length; i++) {
			Entities.removeLabel(guyLabels[i]);
		}
		guyLabels = null;
		Entities.removeButton(showInternalIP);
		Entities.removeButton(showExternalIP);
		Entities.removeButton(changeColor);
		Entities.removeButton(backButton);
		Entities.removeButton(readyButton);
	}
	
	public static void updatePlayers() {
		//-------Clear Guys------------------
		if (guys != null) {
			for(int i=0; i < guys.length; i++) {
				Entities.removeObject(guys[i]);
			}
			guys = null;
		}
		if(guyLabels != null) {
			for(int i=0; i < guyLabels.length; i++) {
				Entities.removeLabel(guyLabels[i]);
			}
			guyLabels = null;
		}
		//------------------------------------
		
		BufferedImage imgRGB = Images.get(0);
		guys = new Object[Client.gameState.connections.getNumConnections()];
		guyLabels = new Label[Client.gameState.connections.getNumConnections()];
		for(int i=0; i < guys.length; i++) {
			Object guy = new Object(Client.gameState.connections.get(i).player.getColor());

			guys[i] = guy;
			guy.pos.x = i * 200f + 500f + 100f;
			guy.pos.y = 250f;
			
			Animation redGuyAnimation = new Animation(4, 0);
			redGuyAnimation.addImage(imgRGB, 0, 0, 100, 100);
			redGuyAnimation.addImage(imgRGB, 101, 0, 100, 100);
			redGuyAnimation.addImage(imgRGB, 202, 0, 100, 100);
			redGuyAnimation.addImage(imgRGB, 303, 0, 100, 100);
			guy.addAnimation(redGuyAnimation);	
			
			Animation greenGuyAnimation = new Animation(4, 0);
			greenGuyAnimation.addImage(imgRGB, 0, 203, 100, 100);
			greenGuyAnimation.addImage(imgRGB, 101, 203, 100, 100);
			greenGuyAnimation.addImage(imgRGB, 202, 203, 100, 100);
			greenGuyAnimation.addImage(imgRGB, 303, 203, 100, 100);
			guy.addAnimation(greenGuyAnimation);
			
			Animation blueGuyAnimation = new Animation(4, 0);
			blueGuyAnimation.addImage(imgRGB, 0, 102, 100, 100);
			blueGuyAnimation.addImage(imgRGB, 101, 102, 100, 100);
			blueGuyAnimation.addImage(imgRGB, 202, 102, 100, 100);
			blueGuyAnimation.addImage(imgRGB, 303, 102, 100, 100);
			guy.addAnimation(blueGuyAnimation);
			
			Entities.addObject(guy);
			
			Label guyLabel = new Label(Client.gameState.connections.get(i).getName(), i * 200 + 600, 200, 100, 1, font, Color.WHITE);
			guyLabels[i] = guyLabel;
			Entities.addLabel(guyLabel);
		}
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
		for(int i=0; i < Entities.buttonCount; i++) {
			if(Entities.buttons[i].checkBounds(x, y) == true) {
				if(Entities.buttons[i].identity != 5) {//basic button color switch excect for ready button
					Entities.buttons[i].setBColor(Color.WHITE);
				}
				
				if(MouseInput.leftPressed == true) {
					MouseInput.leftPressed = false;
					switch(Entities.buttons[i].identity) {
					case 1://showInternalIP
						LobbyScreen.showInternalIP();
						break;
					case 2://showExternalIP
						LobbyScreen.showExternalIP();
						break;
					case 3://ChangeColor
						Client.rawData.setColor((Client.rawData.getColor() + 1) % 3);
						break;
					case 4://Back
						try {
							Client.disconnectFromServer(InetAddress.getByName(Client.IP));
						} catch (UnknownHostException e) {
							System.out.println("LobbyScreen/checkButtons(): UnknownHostException");
						}
						Client.terminateSocketListener();
						Client.terminateServer();

						UIManager.goTo(UIManager.MULTIPLAYER_SCREEN);
						break;
					case 5://Ready
						readyUp();
						break;
					}
				}
			}else {
				Entities.buttons[i].setDefaultColor();
			}
		}
	}
	
	public static void readyUp() {
		if(Client.rawData.getReady() == 1){
			Client.rawData.setReady(0);
			readyButton.setDefaultColor(Color.RED);
			changeColor.enable();
		}else {
			Client.rawData.setReady(1);
			readyButton.setDefaultColor(Color.GREEN);
			changeColor.disable();
			
		}
	}
	
	public static void showInternalIP() {
        if(isShowingInternalIP == false) {
        	String internalIP = "";
        	try {
				internalIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
        	Entities.buttons[0].setText(internalIP);
        	isShowingInternalIP = true;
        }else {
        	Entities.buttons[0].setText("Internal IP");
        	isShowingInternalIP = false;
        }
	}
	
	public static void showExternalIP() {
        if(isShowingExternalIP == false) {
        	Entities.buttons[1].setText(externalIP);
        	isShowingExternalIP = true;
        }else {
        	Entities.buttons[1].setText("External IP");
        	isShowingExternalIP = false;
        }
	}

}
