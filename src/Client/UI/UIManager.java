package Client.UI;

import Client.Entities.Entities;

public class UIManager {
	//UI
	private static int screenType;
	/*  Mode
	 *  case -1:  Loading Screen
	 * 	case  0:  Menu Screen
	 *	case  1:  Multiplayer Screen
	 *	case  2:  HostServerScreen
	 *	case  3:  LobbyScreen
	 *	case  4:  Game
	 *  case  5:  Winner Screen
	 *  case  6:  Settings
	 */
	//-----------------------------------------------
	public static final int LOADING_SCREEN = -1;
	public static final int MENU_SCREEN = 0;
	public static final int MULTIPLAYER_SCREEN = 1;
	public static final int HOSTSERVER_SCREEN = 2;
	public static final int LOBBY_SCREEN = 3;
	public static final int GAME = 4;
	public static final int WINNER_SCREEN = 5;
	public static final int SETTING_SCREEN = 6;
	
	
	public static void goTo(int i) {
		//unload
		switch(screenType) {
		case -1://Loading Screen
			if(LoadingScreen.isLoaded)
				LoadingScreen.unload();
			break;
		case 0://Menu Screen
			if(MenuScreen.isLoaded)
				MenuScreen.unload();
			break;
		case 1://Multiplayer Screen
			if(MultiplayerScreen.isLoaded)
				MultiplayerScreen.unload();
			break;
		case 2://HostServer Screen
			if(HostServerScreen.isLoaded)
				HostServerScreen.unload();
			break;
		case 3://Lobby Screen
			if(LobbyScreen.isLoaded)
				LobbyScreen.unload();
			break;
		case 4://Game
				Entities.removeAll();
			break;
		case 5://Winner Screen
			if(WinnerScreen.isLoaded)
				WinnerScreen.unload();
			break;
		case 6://SettingScreen
			if(SettingScreen.isLoaded)
				SettingScreen.unload();
			break;
		}
		
		
		//load
		switch(i) {
		case -1://Loading Screen
			if(!LoadingScreen.isLoaded)
				LoadingScreen.load();
			break;
		case 0://Menu Screen
			if(!MenuScreen.isLoaded)
				MenuScreen.load();
			break;
		case 1://Multiplayer Screen
			if(!MultiplayerScreen.isLoaded)
				MultiplayerScreen.load();
			break;
		case 2://HostServer Screen
			if(!HostServerScreen.isLoaded)
				HostServerScreen.load();
			break;
		case 3://Lobby Screen
			if(!LobbyScreen.isLoaded)
				LobbyScreen.load();
			break;
		case 4://Game
			break;
		case 5://WinnerScreen
			if(!WinnerScreen.isLoaded)
				WinnerScreen.load();
			break;
		case 6://SettingScreen
			if(!SettingScreen.isLoaded)
				SettingScreen.load();
			break;
			
		}
		
		screenType = i;
	}
	
	public static int getScreen() {
		return screenType;
	}
	
	public static void init(int s) {
		screenType = s;
	}
	
}
