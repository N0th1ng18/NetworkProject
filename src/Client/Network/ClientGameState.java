package Client.Network;

import Tools.Timer;
import Client.Network.Connections;
import Client.Network.Connection;

public class ClientGameState {
	private static final int MAX_CONNECTIONS = 10;
	public static final int CONNECTION_TIMOUT_TIME = 5;
	
	public Timer connectionRequestTimer;
	
	//Buffers
	public Connections connections;
	
	public ClientGameState() {
		connections = new Connections(MAX_CONNECTIONS);
		connectionRequestTimer = new Timer();
	}
	
	public void addConnection(Connection c) {
		connections.addConnection(c);
	}
	
	public String toString() {
		String s = "";
		for(int i=0; i < connections.getNumConnections(); i++) {
			if(i==0) {
				s+= "(Client)"+"\t"+"(ID)"+"\t"+"(NAME)"+"\t"+"(COLOR)"+"\n";
			}
			s += "\t\t";
			s += connections.get(i).getID() + "\t";
			s += connections.get(i).getName() + "\t";
			s += connections.get(i).player.getColor() + "\t";
			s += "\n";
		}
		return s;
	}
	
	
}
