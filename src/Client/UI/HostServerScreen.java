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
import Server.Server;

public class HostServerScreen {
	
	private static Font font = new Font("TimesRoman", Font.PLAIN, 60);
	
	/*
	 * MULTIPLAYER SCREEN
	 */
	private static Button startServer;
	private static Button setPassword;
	private static Button backButton;
	
	public static String password = "OFF";
	public static boolean isLoaded = false;
	
	public static void load() {
		isLoaded = true;

		startServer = new Button(100, 250, 400, 100, Color.GRAY, Color.BLACK, "Start Server", font);
		Entities.addButton(startServer);
		
		setPassword = new Button(550, 250, 400, 100, Color.GRAY, Color.BLACK, "Set Password", font);
		Entities.addButton(setPassword);
		
		backButton = new Button(100, 550, 400, 100, Color.GRAY, Color.BLACK, "Back", font);
		Entities.addButton(backButton);
	}
	
	public static void unload() {
		isLoaded = false;
		
		Entities.removeButton(startServer);
		Entities.removeButton(setPassword);
		Entities.removeButton(backButton);
	}
	
	public static void checkButtons(float x, float y) {
		for(int i=0; i < Entities.buttonCount; i++) {
			if(Entities.buttons[i].checkBounds(x, y) == true) {
				Entities.buttons[i].setBColor(Color.WHITE);
				
				if(MouseInput.leftPressed == true) {
					MouseInput.leftPressed = false;
					switch(Entities.buttons[i].identity) {
					case 1://Start Server
						if(Client.serverIsRunning == false) {//If server isnt running
							Client.spawnServer();
							Client.openSocket();
							try {
								Client.requestToJoinServer(InetAddress.getLocalHost());
							} catch (UnknownHostException e) {
								System.out.println("Client/HostServerScreen/checkButtons:  Local Host Name Could Not be Resolved into an Address");
							}
							
							//Go To Loading Screen
							UIManager.goTo(UIManager.LOADING_SCREEN);
						}
						
						break;
						
					case 2://Set Password
						HostServerScreen.setServerPassword();
						break;
						
					case 3://Back
						UIManager.goTo(UIManager.MULTIPLAYER_SCREEN);
						break;
					}
				}
			}else {
				Entities.buttons[i].setDefaultColor();
			}
		}
	}
	
	
	public static void setServerPassword() {
        String string = JOptionPane.showInputDialog(Client.frame, "", "Server Password", JOptionPane.PLAIN_MESSAGE);
        if(string != null && string.compareTo("") != 0) {
        	Entities.buttons[1].setText(string);
        	Client.serverPassword = string;
        }
	}

}
