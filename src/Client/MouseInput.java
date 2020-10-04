package Client;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Client.Entities.Object;
import Client.UI.HostServerScreen;
import Client.UI.LoadingScreen;
import Client.UI.LobbyScreen;
import Client.UI.MenuScreen;
import Client.UI.MultiplayerScreen;
import Client.UI.SettingScreen;
import Client.UI.UIManager;

public class MouseInput implements MouseListener{
	public static float x,y;
	public static boolean leftPressed;
	public static boolean rightPressed;
	public static boolean middlePressed;
	public static boolean entered;
	
	public MouseInput() {
		
	}
	
	public static void select(Object o) {
		
	}
	
	public static void deselect() {
		
	}
	
	public static void update() {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation(); 
		int mainFrame_x = Client.frame.getComponent(0).getLocationOnScreen().x;
		int mainFrame_y = Client.frame.getComponent(0).getLocationOnScreen().y;
		
		x = mouseLocation.x - mainFrame_x;
		y = mouseLocation.y - mainFrame_y;
		
		switch(UIManager.getScreen()) {
		case -1://Loading Screen
			LoadingScreen.checkConnectionStatus();
			break;
		case 0://Menu Screen
			MenuScreen.setAnimations(x, y);
			MenuScreen.checkButtons(x, y);
			break;
		case 1://Multiplayer Screen
			MultiplayerScreen.checkButtons(x,y);
			break;
		case 2://HostServerScreen
			HostServerScreen.checkButtons(x, y);
			break;	
		case 3://LobbyScreen
			LobbyScreen.setAnimations(x, y);
			LobbyScreen.checkButtons(x, y);
			if(UIManager.getScreen() == 3)
				LobbyScreen.updatePlayers();
			break;
		case 4://Game
			break;
		case 6://SettingScreen
			SettingScreen.checkButtons(x, y);
			break;
		}
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getButton() == 1) {
			leftPressed = true;
		}
		
		if(arg0.getButton() == 2) {
			middlePressed = true;
		}
		
		if(arg0.getButton() == 3) {
			rightPressed = true;
		}
		
		x = arg0.getX();
		y = arg0.getY();
	}

	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getButton() == 1) {
			leftPressed = false;
		}
		if(arg0.getButton() == 2) {
			middlePressed = false;
		}
		if(arg0.getButton() == 3) {
			rightPressed = false;
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		entered = true;
	}

	public void mouseExited(MouseEvent arg0) {
		entered = false;
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

}
