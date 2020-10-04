package Client;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Client.UI.HostServerScreen;
import Client.UI.LoadingScreen;
import Client.UI.LobbyScreen;
import Client.UI.MenuScreen;
import Client.UI.MultiplayerScreen;
import Client.UI.UIManager;

public class KeyInput implements KeyListener{
	/*
	 * Movement
	 */
	public static boolean W;
	public static boolean A;
	public static boolean S;
	public static boolean D;
	
	public KeyInput() {
		W = false;
		A = false;
		S = false;
		D = false;
	}
	
	public static void update() {
		
		switch(UIManager.getScreen()) {
		case 4://Game
			Client.rawData.input.setW(W ? 1 : 0);
			Client.rawData.input.setA(A ? 1 : 0);
			Client.rawData.input.setS(S ? 1 : 0);
			Client.rawData.input.setD(D ? 1 : 0);
			break;
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				W = true;
				break;
			case KeyEvent.VK_A:
				A = true;
				break;
			case KeyEvent.VK_S:
				S = true;
				break;
			case KeyEvent.VK_D:
				D = true;
				break;
			case KeyEvent.VK_ESCAPE:
				Client.running = false;
				break;
		}
		
	}

	public void keyReleased(KeyEvent e) {
		
			switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
				W = false;
				break;
			case KeyEvent.VK_A:
				A = false;
				break;
			case KeyEvent.VK_S:
				S = false;
				break;
			case KeyEvent.VK_D:
				D = false;
				break;
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
