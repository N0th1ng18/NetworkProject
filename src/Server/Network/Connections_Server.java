package Server.Network;

public class Connections_Server {
	
	
	private Connection_Server[] connections;
	private int numConnections;
	
	public Connections_Server(int MAX_CONNECTIONS) {
		connections = new Connection_Server[MAX_CONNECTIONS];
		numConnections = 0;
	}
	
	public void addConnection(Connection_Server c) {
		connections[numConnections] = c;
		numConnections++;
	}
	
	public void removeConnection(int index) {//left packed
		connections[index] = null;
		for(int i=0; i < connections.length; i++) {
			if(connections[i] == null && i < connections.length -1) {
				connections[i] = connections[i+1];
			}
		}
		numConnections--;
	}
	
	public void removeConnections() {
		connections = new Connection_Server[connections.length];
		numConnections = 0;
	}
	
	public boolean isConnected(int ID) {
		for(int i=0; i < numConnections; i++) {
			if(ID == connections[i].getID()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public int getNumConnections() {
		return numConnections;
	}

	public Connection_Server get(int i) {
		return connections[i];
	}
	
	public Connection_Server get_UsingID(int clientID) {
		for(int i=0; i < numConnections; i++) {
			if(clientID == connections[i].getID()) {
				return connections[i];
			}
		}
		
		return null;
	}
	
	public int getIndex_UsingID(int clientID) {
		for(int i=0; i < numConnections; i++) {
			if(clientID == connections[i].getID()) {
				return i;
			}
		}
		
		return -1;
	}
}
