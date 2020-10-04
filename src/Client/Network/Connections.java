package Client.Network;


public class Connections {
	
	
	private Connection[] connections;
	private int numConnections;
	
	public Connections(int MAX_CONNECTIONS) {
		connections = new Connection[MAX_CONNECTIONS];
		numConnections = 0;
	}
	
	public void addConnection(Connection c) {
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
		connections = new Connection[connections.length];
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

	public Connection get(int i) {
		return connections[i];
	}
	
	public Connection get_UsingID(int clientID) {
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
