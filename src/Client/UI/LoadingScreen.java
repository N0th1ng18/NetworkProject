package Client.UI;

import Client.Client;
import Client.Network.ClientGameState;

public class LoadingScreen {
	
	public static boolean isLoaded = false;
	
	public static void load() {
		isLoaded = true;
	}
	
	public static void unload() {
		isLoaded = false;
	}
	
	
	public static void checkConnectionStatus() {
		if(Client.connectionStatus != 2 && Client.gameState.connectionRequestTimer.getSeconds() > ClientGameState.CONNECTION_TIMOUT_TIME) {
			Client.connectionStatus = 1;
		}
		
		switch(Client.connectionStatus) {
		case 1: //Connection Timeout
			System.out.println("Connection Lost: "+ 1);
			Client.connectionStatus = 0;
			Client.terminateSocketListener();
			
			UIManager.goTo(UIManager.MULTIPLAYER_SCREEN);
			MultiplayerScreen.failedToConnect();
			MultiplayerScreen.incorrectPassword();
			break;
		case 2: //Connected
			Client.connectionStatus = 0;
			UIManager.goTo(UIManager.LOBBY_SCREEN);
			break;
		case 3: //Server Disconnected
			System.out.println("Connection Lost: "+ 3);
			Client.connectionStatus = 0;
			UIManager.goTo(UIManager.MULTIPLAYER_SCREEN);
			break;
		}
		
	}
	

}
