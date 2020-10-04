package Client.Network;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import Client.Client;
import Client.Entities.Camera;
import Client.Entities.Entities;
import Client.Entities.Label;
import Client.Entities.Object;
import Client.Entities.ScoreBoard;
import Client.UI.UIManager;
import Client.UI.WinnerScreen;
import Server.Network.SocketData;
import Tools.Timer;

public class ClientProtocol {
	
	/*
	 * msgType
	 * 	To Server
	 * 		0 - new Connection
	 * 		1 - Disconnected
	 * 		2 - Lobby -> Updates to keep connection
	 * 		3 - Game Started -> data(Inputs ... of client player)
	 * 		
	 */
	private static ByteBuffer buffer;

	private static final int SIZE_OF_BYTE = 1;
	private static final int SIZE_OF_INT = 4;
	private static final int SIZE_OF_FLOAT = 4;
	private static final int SIZE_OF_DOUBLE = 8;
	
	private static ClientPacket gamePacket = new ClientPacket();
	
	/*
	 * To Server Protocol
	 */
	//Request to Connect
	public static byte[] getBytes_ConnectionRequest_Packet(String name, String password) {
		//System.out.println("Client "+ Client.clientID + ": -> Request To Connect.");
		
		buffer = ByteBuffer.allocate(SIZE_OF_BYTE + SIZE_OF_INT + name.length() + SIZE_OF_INT + password.length());
		
		buffer.put((byte)0);
		buffer.putInt(name.length());
		buffer.put(name.getBytes());
		buffer.putInt(password.length());
		buffer.put(password.getBytes());
		
		return buffer.array();
	}
	//Request to Disconnect
	public static byte[] getBytes_Disconnect_Packet(int clientID) {
		//System.out.println("Client "+ Client.clientID + ": -> Request to Disconnect");
		
		buffer = ByteBuffer.allocate(SIZE_OF_BYTE + SIZE_OF_INT);
		buffer.put((byte)1);
		buffer.putInt(clientID);
		
		return buffer.array();
	}
	
	//Lobby
	public static byte[] getBytes_Lobby_Packet(int clientID) {
		//System.out.println("Client "+ Client.clientID + ": -> Sending Lobby Info.");
		
		buffer = ByteBuffer.allocate(
				SIZE_OF_BYTE 											//Message Type
				+ SIZE_OF_INT 											//Client ID
				+ (SIZE_OF_INT + Client.rawData.getName().length()) 	//Name Length & Name
				+ SIZE_OF_INT											//Color
				+ SIZE_OF_INT);											//isReady
		
		buffer.put((byte)2);
		buffer.putInt(clientID);
		buffer.putInt(Client.rawData.getName().length());
		buffer.put(Client.rawData.getName().getBytes());	
		buffer.putInt(Client.rawData.getColor());
		buffer.putInt(Client.rawData.getReady());
		
		return buffer.array();
	}
	
	//Game
	public static byte[] getBytes_Game_Packet(int clientID) {
		//System.out.println("Client "+ Client.clientID + ": -> Sending Lobby Info.");
		
		//Create Packet
		gamePacket.setMsgType((byte)3);
		gamePacket.setClientID(clientID);
		gamePacket.setTick(Client.tick);
		gamePacket.setName(Client.rawData.getName());
		gamePacket.setColor(Client.rawData.getColor());
		gamePacket.setW(Client.rawData.input.getW());
		gamePacket.setA(Client.rawData.input.getA());
		gamePacket.setS(Client.rawData.input.getS());
		gamePacket.setD(Client.rawData.input.getD());
		
	//Init Buffer Size
		int bufferSize = SIZE_OF_BYTE 											//Message Type
						+ SIZE_OF_INT 											//Client ID	
						+ SIZE_OF_INT											//Tick Number
						+ SIZE_OF_INT											//hasChanged
						;
		if(getBit(gamePacket.getHasChanged(), 0) == 1 && getBit(gamePacket.getHasChanged(), 1) == 1) {
			bufferSize += (SIZE_OF_INT + Client.rawData.getName().length());	//Name Length & Name 
		}
		
		if(getBit(gamePacket.getHasChanged(), 2) == 1) {
			bufferSize += SIZE_OF_INT;											//Color
		}
		if(getBit(gamePacket.getHasChanged(), 3) == 1) {
			bufferSize += SIZE_OF_INT;											//W
		}
		if(getBit(gamePacket.getHasChanged(), 4) == 1) {
			bufferSize += SIZE_OF_INT;											//A
		}
		if(getBit(gamePacket.getHasChanged(), 5) == 1) {
			bufferSize += SIZE_OF_INT;											//S
		}
		if(getBit(gamePacket.getHasChanged(), 6) == 1) {
			bufferSize += SIZE_OF_INT;											//D
		}
						
	//Allocate Buffer
		buffer = ByteBuffer.allocate(bufferSize);
	//Store known attributes	
		//Message Type
		buffer.put(gamePacket.getMsgType());
		//ClientID
		buffer.putInt(gamePacket.getClientID());
		//Tick
		buffer.putInt(gamePacket.getTick());
		//hasChanged
		buffer.putInt(gamePacket.getHasChanged());
	//Store dependable attributes	
		//Name
		if(getBit(gamePacket.getHasChanged(), 0) == 1 && getBit(gamePacket.getHasChanged(), 1) == 1) {
			buffer.putInt(gamePacket.getName().length());
			buffer.put(gamePacket.getName().getBytes());
		}
		//Color
		if(getBit(gamePacket.getHasChanged(), 2) == 1) {
			buffer.putInt(gamePacket.getColor());
		}
		//Input
		if(getBit(gamePacket.getHasChanged(), 3) == 1) {
			buffer.putInt(gamePacket.getW());
		}
		if(getBit(gamePacket.getHasChanged(), 4) == 1) {
			buffer.putInt(gamePacket.getA());
		}
		if(getBit(gamePacket.getHasChanged(), 5) == 1) {
			buffer.putInt(gamePacket.getS());
		}
		if(getBit(gamePacket.getHasChanged(), 6) == 1) {
			buffer.putInt(gamePacket.getD());
		}
	//Reset packet	
		gamePacket.resetHasChanged();
		
		return buffer.array();
	}
	
	//GameOver
	public static byte[] getBytes_GameOver_Packet(int clientID) {
		//System.out.println("Client "+ Client.clientID + ": -> Sending Lobby Info.");
		
	//Create Packet
		gamePacket.setMsgType((byte)4);
		gamePacket.setClientID(clientID);
		
	//Init Buffer Size
		int bufferSize = SIZE_OF_BYTE 											//Message Type
						+ SIZE_OF_INT 											//Client ID	
						;
						
	//Allocate Buffer
		buffer = ByteBuffer.allocate(bufferSize);
	//Store known attributes	
		//Message Type
		buffer.put(gamePacket.getMsgType());
		//ClientID
		buffer.putInt(gamePacket.getClientID());
		
	//Reset packet	
		gamePacket.resetHasChanged();
		
		return buffer.array();
	}
	
	public static void processPacket(SocketData socketData) {
		if(socketData == null) {
			return;
		}
		
		InetAddress address = socketData.address;
		int port = socketData.port;
		byte[] data = socketData.data;
		int cursor = 1;//skip first byte
		
		int clientID;
		int numConnections;
		Connection connection;
		
		double finishTime = -1;
		double gameTime;
		

		switch(data[0]) {
		//-----------------------------------------------------------------------------------------------------
		case (byte)1:	//Server Disconnected
			//System.out.println("Client "+ Client.clientID + ": <- Server has Disconnected.");
			Client.connectionStatus = 3;//Server Disconnected
			if(UIManager.getScreen() != -1)
				UIManager.goTo(UIManager.LOADING_SCREEN);
			break;
		//-----------------------------------------------------------------------------------------------------
		case (byte)2:	//Lobby
			//System.out.println("Client "+ Client.clientID + ": <- Lobby Information.");
			//Timer for packets per second
			if(Client.packetsPerSecondTimer == null) {
				Client.packetsPerSecondTimer = new Timer();
				Client.packetsPerSecondTimer.start();//Started once when connection is made
			}
			//Timer for server connection timout
			if(Client.serverTimeoutTimer == null) {
				Client.serverTimeoutTimer = new Timer();
			}
			
			if(UIManager.getScreen() != UIManager.LOBBY_SCREEN) {
				UIManager.goTo(UIManager.LOADING_SCREEN);
			}
			
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			numConnections = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			
			Client.clientID = clientID;
			Client.connectionStatus = 2;//Connected

			for(int i=0; i < numConnections; i++) {
				//Read In Data
				int iD = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				int nameLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				String name = new String(data, cursor, nameLength, StandardCharsets.US_ASCII);
				cursor += nameLength;
				int color = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				
				//Save Data
				int index = Client.gameState.connections.getIndex_UsingID(iD);
				if(index >= 0) {//update this client's info
					Client.gameState.connections.get(i).setName(name);
					Client.gameState.connections.get(i).player.setColor(color);
					Client.gameState.connections.get(i).setUpdated(true);
				}else {//add this client's info to Client GameState
					connection = new Connection(iD, name);
					connection.player.setColor(color);
					connection.setUpdated(true);
					Client.gameState.addConnection(connection);
				}
			}
			
			//Remove unUpdated Connections
			removeUnupdatedClientsInLobby();
			//Reset Server TimeOut Timer
			Client.serverTimeoutTimer.start();
			break;
			//-----------------------------------------------------------------------------------------------------
		case (byte)3:	//Game started
			if(UIManager.getScreen() != UIManager.GAME) {
				UIManager.goTo(UIManager.GAME);
				for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
					Entities.addObject(Client.gameState.connections.get(i).player);//adds/creates all players
				}
				//Reset Tick for Game
				Client.tick = 0;
				//Create GameTime Label
				Label l = new Label("", 52, -130, Client.WIDTH, 100, new Font("TimesRoman", Font.PLAIN, 100), Color.ORANGE);
				l.centerY = false;
				Entities.addLabel(l);
				//ScoreBoard
				Entities.scoreBoard = new ScoreBoard(Client.gWidth - 312, 10);
				//Reset Finish Times
				resetPlayersFinishTime();
			}
			
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			gameTime = ByteBuffer.wrap(data, cursor, SIZE_OF_DOUBLE).getDouble();
			cursor += SIZE_OF_DOUBLE;
			numConnections = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			
			Client.clientID = clientID;
			
			if(gameTime <= 5) {
				Entities.labels[0].str = (5 - (int)gameTime)+"";
			}else if(gameTime <= 5.9) {
				Entities.labels[0].str = "GO!";
			}else {
				Entities.labels[0].str = "";
			}
			
			Client.connectionStatus = 2;//Connected
			
			for(int i=0; i < numConnections; i++) {
				//Read In Data
				int iD = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				int nameLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				String name = new String(data, cursor, nameLength, StandardCharsets.US_ASCII);
				cursor += nameLength;
				int color = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				int tick = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				float posX = ByteBuffer.wrap(data, cursor, SIZE_OF_FLOAT).getFloat();
				cursor += SIZE_OF_FLOAT;
				float posY = ByteBuffer.wrap(data, cursor, SIZE_OF_FLOAT).getFloat();
				cursor += SIZE_OF_FLOAT;
				int size = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
				cursor += SIZE_OF_INT;
				byte finished = ByteBuffer.wrap(data, cursor, SIZE_OF_BYTE).get();
				cursor += SIZE_OF_BYTE;
				if(finished == (byte)1) {
					finishTime = ByteBuffer.wrap(data, cursor, SIZE_OF_DOUBLE).getDouble();
					cursor += SIZE_OF_DOUBLE;
				}
				
				//Save Data
				int index = Client.gameState.connections.getIndex_UsingID(iD);
				if(index >= 0) {//update this client's info
					//if finished update finish time no matter what.
					if(finished == (byte)1) {
						Client.gameState.connections.get(i).player.finishTime = finishTime;
					}
					//Only render newest Packet
					if(tick > Client.gameState.connections.get(i).player.tick) {
						Client.gameState.connections.get(i).player.tick = tick;
						
						Client.gameState.connections.get(i).setName(name);
						Client.gameState.connections.get(i).player.setColor(color);
						//Interpolation -> save old position
						Client.gameState.connections.get(i).player.resetNumClientUpdates();
						Client.gameState.connections.get(i).player.posOld_Srv.x = Client.gameState.connections.get(i).player.pos_Srv.x;
						Client.gameState.connections.get(i).player.posOld_Srv.y = Client.gameState.connections.get(i).player.pos_Srv.y;
						//----------------------------------------
						Client.gameState.connections.get(i).player.pos_Srv.x = posX;
						Client.gameState.connections.get(i).player.pos_Srv.y = posY;
						Client.gameState.connections.get(i).player.size = size;
						//Let player know if it is the client's player
						if(clientID == iD) {
							Client.gameState.connections.get(i).player.isClient = true;
							Entities.camera = new Camera(Client.gameState.connections.get(i).player);
						}else
							Client.gameState.connections.get(i).player.isClient = false;
						//-----------------------------------------------
					}
					Client.gameState.connections.get(i).setUpdated(true);
				}else {//add this client's info to Client GameState
					connection = new Connection(iD, name);
					connection.player.setColor(color);
					//Interpolation -> set old pos to same pos
					connection.player.resetNumClientUpdates();
					connection.player.posOld_Srv.x = posX;
					connection.player.posOld_Srv.y = posY;
					//----------------------------------------
					connection.player.pos_Srv.x = posX;
					connection.player.pos_Srv.y = posY;
					connection.player.size = size;
					//Let player know if it is the client's player
					if(clientID == iD)
						connection.player.isClient = true;
					else
						connection.player.isClient = false;
					//-----------------------------------------------
					connection.setUpdated(true);
					Entities.addObject(connection.player);
					Client.gameState.addConnection(connection);
				}
			}
			
			//Map
			float map_PosX = ByteBuffer.wrap(data, cursor, SIZE_OF_FLOAT).getFloat();
			cursor += SIZE_OF_FLOAT;
			float map_PosY = ByteBuffer.wrap(data, cursor, SIZE_OF_FLOAT).getFloat();
			cursor += SIZE_OF_FLOAT;
			
			Entities.map.pos_Srv.x = map_PosX;
			Entities.map.pos_Srv.y = map_PosY;
			
			//Remove unUpdated Connections
			removeUnupdatedClientsInGame();
			Client.serverTimeoutTimer.start();
			break;
			
		case (byte)4://Game Over
			if(UIManager.getScreen() != UIManager.WINNER_SCREEN) {
				unReadyClient();
				//ScoreBoard
				Entities.scoreBoard = new ScoreBoard(Client.gWidth/2 - 150, Client.gHeight/2 - 80);
				Entities.removeAll();
				UIManager.goTo(UIManager.WINNER_SCREEN);
			}
			
			clientID = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			
			Client.connectionStatus = 2;//Connected
			
			//Winner
			int id_of_Winner = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			int nameLength = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			String name = new String(data, cursor, nameLength, StandardCharsets.US_ASCII);
			cursor += nameLength;
			int color = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			int size = ByteBuffer.wrap(data, cursor, SIZE_OF_INT).getInt();
			cursor += SIZE_OF_INT;
			WinnerScreen.setWinnerName(name);
			
			//Save Data
			Client.gameState.connections.get_UsingID(id_of_Winner).setName(name);
			Client.gameState.connections.get_UsingID(id_of_Winner).player.setColor(color);
			Client.gameState.connections.get_UsingID(id_of_Winner).player.size = size;
			//-----------------------------------------------
			
			//All players are updated
			for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
				Client.gameState.connections.get(i).setUpdated(true);
			}
			
			//Reset Server TimeOut Timer
			removeUnupdatedClientsInGame();
			Client.serverTimeoutTimer.start();
			break;
		//-----------------------------------------------------------------------------------------------------
			
		}
		
	}
	
	private static void resetPlayersFinishTime() {
		for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
			//Remove players that have not finished yet
			if(Client.gameState.connections.get(i).player.finishTime >= 0)
				Client.gameState.connections.get(i).player.finishTime = -1.0;
		}
		
	}
	private static void unReadyClient() {
		Client.rawData.setReady(0);
	}
	
	public static void removeUnupdatedClientsInLobby() {
		//Removed Clients that were not updated
		for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
			if(Client.gameState.connections.get(i).isUpdated() == false) {
				Client.gameState.connections.removeConnection(i);
			}else {
				Client.gameState.connections.get(i).setUpdated(false);
			}
		}
	}
	
	public static void removeUnupdatedClientsInGame() {
		//Removed Clients that were not updated
		for(int i=0; i < Client.gameState.connections.getNumConnections(); i++) {
			if(Client.gameState.connections.get(i).isUpdated() == false) {
				Entities.removeObject(Client.gameState.connections.get(i).player);
				Client.gameState.connections.removeConnection(i);
			}else {
				Client.gameState.connections.get(i).setUpdated(false);
			}
		}
	}

	
	private static int getBit(int num, int pos) {
	    return (num >> pos) & 1;
	}
}
