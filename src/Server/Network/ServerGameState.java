package Server.Network;

public class ServerGameState {
	private static final int MAX_CONNECTIONS = 10;
	
	/* Mode
	 * 	case 0:  Menu Screen
	 *	case 1:  Multiplayer Screen
	 *	case 2:  HostServerScreen
	 *	case 3:  LobbyScreen
	 *	case 4:  Game
	 *	case 5:  GameOver
	 */
	private int screenType;
	
	//Buffers
	public Connections_Server connections;
	
	public ServerGameState() {
		connections = new Connections_Server(MAX_CONNECTIONS);
		
		screenType = 3;//Lobby
	}
	
	public void addConnection(Connection_Server c) {
		connections.addConnection(c);
	}

	public int getScreenType() {
		return screenType;
	}

	public void setScreenType(int screenType) {
		this.screenType = screenType;
	}
	
	
	
	
	public String toString() {
		String s = "";
		for(int i=0; i < connections.getNumConnections(); i++) {
			if(i==0) {
				s+= "(Server)"+"\t"+"(ID)"+"\t"+"(NAME)"+"\t"+"(COLOR)"+"\t"+"(READY)"+"\t"+"(POSITION)"+"\n";
			}
			s += "\t\t";
			s += connections.get(i).getID() + "\t";
			s += connections.get(i).getName() + "\t";
			s += connections.get(i).getColor() + "\t";
			s += connections.get(i).getReady() + "\t";
			if(connections.get(i).player != null)
				s += "(" + connections.get(i).player.pos.x + "," + connections.get(i).player.pos.y + ")" + "\t";
			s += "\n";
		}
		return s;
	}
	
}
