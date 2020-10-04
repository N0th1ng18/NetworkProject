package Server.Network;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import Client.Entities.Entities;
import Server.Server;
import Server.ServerLoop;
import Server.Entities.ServerEntities;

public class ServerProtocol {
	
	/*
	 * msgType
	 * 	To Client
	 * 		0 - Incorrect Password
	 *		1 - Disconnected
	 * 		2 - Lobby
	 * 		3 - Game Started -> data(input ... of all players)
	 * 		
	 */
	
	private static ByteBuffer buffer;
	
	private static final int SIZE_OF_BYTE = 1;
	private static final int SIZE_OF_INT = 4;
	private static final int SIZE_OF_FLOAT = 4;
	private static final int SIZE_OF_DOUBLE = 8;
	
	public static int clientIDGenerator = 0;
	
	/*
	 * ---------------------------------------------------------------------------------------------------------------------------
	 * SEND
	 * ---------------------------------------------------------------------------------------------------------------------------
	 */
	
	//Server Disconnect
	public static byte[] getBytes_Disconnect_Packet() {
		//System.out.println("Server: -> Server Disconnecting.");
		
		buffer = ByteBuffer.allocate(SIZE_OF_BYTE);
		buffer.put((byte)1);
		return buffer.array();
	}
	
	//Lobby
	public static byte[] getBytes_Lobby_Packet(int clientID) {
		//System.out.println("Server: -> Lobby Information to "+clientID+".");
		
		int sizeOfConBuf = 0;
		for(int i=0; i < Server.gameState.connections.getNumConnections(); i++) {
			sizeOfConBuf += SIZE_OF_INT; //Integer for ID
			sizeOfConBuf += SIZE_OF_INT; //Integer for name length
			sizeOfConBuf += Server.gameState.connections.get(i).getName().length();
			sizeOfConBuf += SIZE_OF_INT; //Integer for Color
		}
		
		buffer = ByteBuffer.allocate(SIZE_OF_BYTE + SIZE_OF_INT + SIZE_OF_INT + sizeOfConBuf);
		
		buffer.put((byte)2);
		buffer.putInt(clientID);
		buffer.putInt(Server.gameState.connections.getNumConnections());
		
		for(int i=0; i < Server.gameState.connections.getNumConnections(); i++) {
			buffer.putInt(Server.gameState.connections.get(i).getID());
			buffer.putInt(Server.gameState.connections.get(i).getName().length());
			buffer.put(Server.gameState.connections.get(i).getName().getBytes());
			buffer.putInt(Server.gameState.connections.get(i).getColor());
		}
		
		return buffer.array();
	}

	//Game
	public static byte[] getBytes_Game_Packet(int clientID) {
		//System.out.println("Server: -> Game to "+clientID+".");
		
		int sizeOfConBuf = 0;
		//Connections
		for(int i=0; i < Server.gameState.connections.getNumConnections(); i++) {
			sizeOfConBuf += SIZE_OF_INT; 												//clientID
			sizeOfConBuf += SIZE_OF_INT; 												//name length
			sizeOfConBuf += Server.gameState.connections.get(i).getName().length();		//Name
			sizeOfConBuf += SIZE_OF_INT; 												//Color
			sizeOfConBuf += SIZE_OF_INT;												//Tick Number
			sizeOfConBuf += SIZE_OF_FLOAT; 												//PosX
			sizeOfConBuf += SIZE_OF_FLOAT; 												//PosY
			sizeOfConBuf += SIZE_OF_INT; 												//Size
			sizeOfConBuf += SIZE_OF_BYTE;												//finished
			if(Server.gameState.connections.get(i).player.finished == (byte)1) {
				sizeOfConBuf += SIZE_OF_DOUBLE;											//finishTime
			}
		}
		
		buffer = ByteBuffer.allocate(
				SIZE_OF_BYTE 															//msgType
				+ SIZE_OF_INT 															//ClientID
				+ SIZE_OF_DOUBLE														//GameTime
				+ SIZE_OF_INT 															//numConnections
				+ sizeOfConBuf															//Connections
				+ SIZE_OF_FLOAT															//Map PosX
				+ SIZE_OF_FLOAT															//Map PosY
				);
		
		buffer.put((byte)3);															//msgType
		buffer.putInt(clientID);														//ClientID
		buffer.putDouble(ServerLoop.time);												//Gametime
		buffer.putInt(Server.gameState.connections.getNumConnections());				//numConnections
		
		for(int i=0; i < Server.gameState.connections.getNumConnections(); i++) {
			buffer.putInt(Server.gameState.connections.get(i).getID());					//clientID
			buffer.putInt(Server.gameState.connections.get(i).getName().length());		//name Length
			buffer.put(Server.gameState.connections.get(i).getName().getBytes());		//name
			buffer.putInt(Server.gameState.connections.get(i).getColor());				//color
			buffer.putInt(Server.tick);													//tick
			buffer.putFloat(Server.gameState.connections.get(i).player.pos.x);			//posX
			buffer.putFloat(Server.gameState.connections.get(i).player.pos.y);			//posY
			buffer.putInt(Server.gameState.connections.get(i).player.size);				//Size
			buffer.put(Server.gameState.connections.get(i).player.finished);			//finished
			if(Server.gameState.connections.get(i).player.finished == (byte)1) {
				buffer.putDouble(Server.gameState.connections.get(i).player.finishTime);//finishTime
			}
		}
		
		buffer.putFloat(ServerEntities.map.pos.x);										//Map_posX
		buffer.putFloat(ServerEntities.map.pos.y);										//Map_posY
		
		return buffer.array();
	}
	
	public static byte[] getBytes_GameOver_Packet(int clientID) {
		//Get Winner
		int id_of_Winner = -1;
		double lowestTime = Double.MAX_VALUE;
		for(int i=0; i < Server.gameState.connections.getNumConnections(); i++){
			if(Server.gameState.connections.get(i).player.finished == (byte)1 && Server.gameState.connections.get(i).player.finishTime < lowestTime) {
				id_of_Winner = Server.gameState.connections.get(i).getID();
				lowestTime = Server.gameState.connections.get(i).player.finishTime;
			}
		}
		
		if(id_of_Winner == -1) {
			System.out.println("Error: No Winner!");
			return null;
		}
		
		buffer = ByteBuffer.allocate(
				SIZE_OF_BYTE 																	//msgType
				+ SIZE_OF_INT 																	//ClientID
				//Winner
				+ SIZE_OF_INT 																	//ClientID 
				+ SIZE_OF_INT 																	//name length
				+ Server.gameState.connections.get_UsingID(id_of_Winner).getName().length()		//Name
				+ SIZE_OF_INT 																	//Color
				+ SIZE_OF_INT 																	//Size
				);
		
		buffer.put((byte)4);																		//msgType
		buffer.putInt(clientID);																	//ClientID
		//Winner
		buffer.putInt(id_of_Winner);																	//clientID
		buffer.putInt(Server.gameState.connections.get_UsingID(id_of_Winner).getName().length());		//name Length
		buffer.put(Server.gameState.connections.get_UsingID(id_of_Winner).getName().getBytes());		//name
		buffer.putInt(Server.gameState.connections.get_UsingID(id_of_Winner).getColor());				//color
		buffer.putInt(Server.gameState.connections.get_UsingID(id_of_Winner).player.size);				//Size
		
		
		return buffer.array();
	}
	
	
	/*
	 * ---------------------------------------------------------------------------------------------------------------------------
	 * RECEIVE
	 *  ---------------------------------------------------------------------------------------------------------------------------
	 */
	public static void processPacket(SocketData socketData, String serverPassword, DatagramSocket socket) {
		if(socketData == null) {
			return;
		}
		
		InetAddress address = socketData.address;
		int port = socketData.port;
		byte[] data = socketData.data;
		int cursor = 1;//skip first byte
		
		int clientID;
		int tick;
		int hasChanged;
		int nameLength;
		String name = "";
		int color = -1;
		int ready;
		int index;
		int W = -1;
		int A = -1;
		int S = -1;
		int D = -1;
		
		switch(data[0]) {
		//-----------------------------------------------------------------------------------------------------
		case (byte)0:	//Request to Connect From Client
			//System.out.println("Server: <- Request to Connect.");
		
			nameLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			name = new String(data, cursor, nameLength, StandardCharsets.US_ASCII);
			cursor += nameLength;
			int passwordLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			String password = new String(data, cursor, passwordLength, StandardCharsets.US_ASCII);
			cursor += passwordLength;
			
			
			if(password.compareTo(serverPassword) == 0) {
				//Password Correct
				Connection_Server connection = new Connection_Server(address, port, name);
				connection.startTimer();
				connection.setID(generateClientID());

				Server.gameState.addConnection(connection);
				
			}//else{
				//Password is incorrect
				//Client side will not receive Lobby packet
			//}
					
			break;
		//-----------------------------------------------------------------------------------------------------
		case (byte)1:	//Disconnect
		
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			
			index = Server.gameState.connections.getIndex_UsingID(clientID);
			if(index >= 0) {
				Server.gameState.connections.removeConnection(Server.gameState.connections.getIndex_UsingID(clientID));
			}else {
				System.out.println("Connection To Remove Was Not Found.");
			}
			break;
		//-----------------------------------------------------------------------------------------------------
		case (byte)2:	//Lobby
			//System.out.println("Server: <- Client's Lobby Info.");
		
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			nameLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			name = new String(data, cursor, nameLength, StandardCharsets.US_ASCII);
			cursor += nameLength;
			color = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			ready = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			
			//Save to Server State & ResetTimer
			
			index = Server.gameState.connections.getIndex_UsingID(clientID);
			
			if(index >= 0) {
				Server.gameState.connections.get(index).setName(name);
				Server.gameState.connections.get(index).setColor(color);
				Server.gameState.connections.get(index).setReady(ready);
				if(Server.gameState.connections.get(index) != null)
					Server.gameState.connections.get(index).startTimer();
			}
			break;
		//-----------------------------------------------------------------------------------------------------
		case (byte)3:	//Game
			
			//clientID
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			//Tick
			tick = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			//hasChanged
			hasChanged = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			//System.out.println(hasChanged);
			
			//nameLength & name
			if(getBit(hasChanged, 0) == 1 && getBit(hasChanged, 1) == 1) {
				nameLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				name = new String(data, cursor, nameLength, StandardCharsets.US_ASCII);
				cursor += nameLength;
			}
			//color
			if(getBit(hasChanged, 2) == 1) {
				color = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
			}
			//W
			if(getBit(hasChanged, 3) == 1) {
				W = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
			}
			//A
			if(getBit(hasChanged, 4) == 1) {
				A = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
			}
			//S
			if(getBit(hasChanged, 5) == 1) {
				S = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
			}
			//D
			if(getBit(hasChanged, 6) == 1) {
				D = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
			}
			
			//Save to Server State & ResetTimer
			index = Server.gameState.connections.getIndex_UsingID(clientID);
			
			if(index >= 0) {
				//Only Store Most Current Input
				if(tick > Server.gameState.connections.get(index).player.tick) {
					
					Server.gameState.connections.get(index).player.tick = tick;
					
					if(getBit(hasChanged, 0) == 1 && getBit(hasChanged, 1) == 1) {
						Server.gameState.connections.get(index).setName(name);
					}
					if(getBit(hasChanged, 2) == 1) {
						Server.gameState.connections.get(index).setColor(color);
					}
					if(getBit(hasChanged, 3) == 1) {
						Server.gameState.connections.get(index).player.input.setW(W);
					}
					if(getBit(hasChanged, 4) == 1) {
						Server.gameState.connections.get(index).player.input.setA(A);
					}
					if(getBit(hasChanged, 5) == 1) {
						Server.gameState.connections.get(index).player.input.setS(S);
					}
					if(getBit(hasChanged, 6) == 1) {
						Server.gameState.connections.get(index).player.input.setD(D);
					}
				}
				
				if(Server.gameState.connections.get(index) != null)
					Server.gameState.connections.get(index).startTimer();
			}
			break;
		//-----------------------------------------------------------------------------------------------------
		case (byte)4:
			//clientID
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			
			index = Server.gameState.connections.getIndex_UsingID(clientID);
			
			if(Server.gameState.connections.get(index) != null)
				Server.gameState.connections.get(index).startTimer();
		
			break;
		}
	}
	
	private static int generateClientID() {
		return clientIDGenerator++;
	}
	
	private static int getBit(int num, int pos) {
	    return (num >> pos) & 1;
	}
	
	

}
