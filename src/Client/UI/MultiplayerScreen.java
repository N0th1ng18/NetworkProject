package Client.UI;
import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import Client.Client;
import Client.MouseInput;
import Client.Entities.Button;
import Client.Entities.Entities;

public class MultiplayerScreen {
	
	private static Font font = new Font("TimesRoman", Font.PLAIN, 60);
	
	/*
	 * MULTIPLAYER SCREEN
	 */
	private static Button hostServer;
	private static Button joinServer;
	private static Button backButton;
	private static Button playerName;
	private static Button ipAddressButton;
	private static Button passwordButton;
	
	public static boolean isLoaded = false;
	
	public static void load() {
		isLoaded = true;
		
		hostServer = new Button(100, 250, 400, 100, Color.GRAY, Color.BLACK, "Host Server", font);
		Entities.addButton(hostServer);
		
		joinServer = new Button(100, 400, 400, 100, Color.GRAY, Color.BLACK, "Join Server", font);
		Entities.addButton(joinServer);
		
		backButton = new Button(100, 550, 400, 100, Color.GRAY, Color.BLACK, "Back", font);
		Entities.addButton(backButton);
		
		playerName = new Button(100, 100, 400, 100, Color.GRAY, Color.BLACK, Client.name, font);
		Entities.addButton(playerName);
		
		ipAddressButton = new Button(550, 400, 400, 100, Color.GRAY, Color.BLACK, Client.IP, font);
		Entities.addButton(ipAddressButton);
		
		passwordButton = new Button(1000, 400, 400, 100, Color.GRAY, Color.BLACK, Client.serverPassword, font);
		Entities.addButton(passwordButton);
	}
	
	public static void unload() {
		isLoaded = false;
		
		Entities.removeButton(hostServer);
		Entities.removeButton(joinServer);
		Entities.removeButton(backButton);
		Entities.removeButton(playerName);
		Entities.removeButton(ipAddressButton);
		Entities.removeButton(passwordButton);
	}
	
	
	public static void checkButtons(float x, float y) {
		for(int i=0; i < Entities.buttonCount; i++) {
			if(Entities.buttons[i].checkBounds(x, y) == true) {
				Entities.buttons[i].setBColor(Color.WHITE);
				
				if(MouseInput.leftPressed == true) {
					MouseInput.leftPressed = false;
					switch(Entities.buttons[i].identity) {
					case 1://Host Server
						UIManager.goTo(UIManager.HOSTSERVER_SCREEN);
						break;
					case 2://Join Server
						Client.openSocket();
						try {
							Client.requestToJoinServer(InetAddress.getByName(Client.IP));
						} catch (UnknownHostException e) {
							System.out.println("Client/HostServerScreen/checkButtons:  Local Host Name Could Not be Resolved into an Address");
						}
						
						//Go To Loading Screen
						UIManager.goTo(UIManager.LOADING_SCREEN);
						break;
					case 3://Back
						UIManager.goTo(UIManager.MENU_SCREEN);//MenuScreen
						break;
					case 4://Player Name
						MultiplayerScreen.setPlayerName();
						break;
					case 5://IP Address to Connect to
						MultiplayerScreen.setIPAddress();
						break;
					case 6://Server Password
						MultiplayerScreen.setServerPassword();
						break;
					}
				}
			}else {
				Entities.buttons[i].setDefaultColor();
			}
		}
	}
	
	public static void setPlayerName() {
        String string = JOptionPane.showInputDialog(Client.frame, "", "Player Name", JOptionPane.PLAIN_MESSAGE);
        if(string != null && string.compareTo("") != 0) {
        	if(string.length() > Client.MAX_NAME_LENGTH) {
        		string = string.substring(0, Client.MAX_NAME_LENGTH);
        	}
        	Entities.buttons[3].setText(string);
        	Client.name = string;
        	Client.rawData.setName(string);
        }
	}
	
	public static void setIPAddress() {
        String string = JOptionPane.showInputDialog(Client.frame, "Example: XXX.XXX.XXX.XXX", "IP Address", JOptionPane.PLAIN_MESSAGE);
        if(string != null && string.compareTo("") != 0) {
        	Entities.buttons[4].setText(string);
        	Client.IP = string;
        	Entities.buttons[4].setDefaultColor(Color.GRAY);
        }
	}
	
	public static void setServerPassword() {
        String string = JOptionPane.showInputDialog(Client.frame, "", "Server Password", JOptionPane.PLAIN_MESSAGE);
        if(string != null && string.compareTo("") != 0) {
        	Entities.buttons[5].setText(string);
        	Client.serverPassword = string;
        	Entities.buttons[5].setDefaultColor(Color.GRAY);
        }
	}

	public static void failedToConnect() {
		Entities.buttons[4].setDefaultColor(Color.RED);
	}
	public static void incorrectPassword() {
		Entities.buttons[5].setDefaultColor(Color.RED);
	}

}
